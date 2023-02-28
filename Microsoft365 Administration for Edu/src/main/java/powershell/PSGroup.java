package powershell;

import org.json.JSONArray;
import org.json.JSONObject;
import school.Group;
import school.Teacher;
import school.User;
import school.Student;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PSGroup extends PS{
    public static List<Group> getGroupList() throws IOException {
        String response = getConsole().execute("Get-MsolGroup -All | ConvertTo-Json");// | Format-List ");
        if(response.length()<3){
            response="[]";
        }
        if(response.startsWith("{")){
            response = "[" + response + "]";
        }
        response = "{\"items\":"+response+"}";
        Files.writeString(Path.of("response.txt"), response, StandardCharsets.UTF_8);

        JSONArray json_array = new JSONObject(response).getJSONArray("items");
        ArrayList<Group> list= new ArrayList<>();
        System.out.println(json_array.length());
        for(int i = 0; i<json_array.length(); i++){

            JSONObject json_group = json_array.getJSONObject(i);

            Group group = new Group();
            group.setObjectId(json_group.isNull("ObjectId")?"": (String) json_group.get("ObjectId"));
            group.setGroup_name(json_group.isNull("DisplayName")?"": (String) json_group.get("DisplayName"));
            group.setDescription( json_group.isNull("Description")? "" : (String) json_group.get("Description"));
            group.setEmail_address( json_group.isNull("EmailAddress")? "" : (String) json_group.get("EmailAddress"));
            group.setGroup_type(json_group.isNull("GroupType")? "" : json_group.get("GroupType").toString());
            //group.setUsers(this.getGroupMembers(group.getObject_id());
            list.add(group);
        }

        return list;
    }

    public static List<String> getGroupMembers(String ObjectId) throws IOException {
        String response = getConsole().execute("Get-MsolGroupMember -GroupObjectId "+ ObjectId + "| ConvertTo-Json");// | Format-List ");
        System.out.println(response);
        response = "{\"items\":"+response+"}";
        Files.writeString(Path.of("response.txt"), response, StandardCharsets.UTF_8);

        JSONArray json_array = new JSONObject(response).getJSONArray("items");

        ArrayList<String> list= new ArrayList<>();
        System.out.println(json_array.length());
        for(int i = 0; i<json_array.length(); i++){

            JSONObject json_group = json_array.getJSONObject(i);
            list.add(json_group.isNull("ObjectId")?"": (String) json_group.get("ObjectId"));
        }

        return list;
    }


    public static String CreateGroup(Group g)
    {
        System.out.println(g.getGroup_name());
        String response= getConsole().execute("New-AzureADGroup -MailEnabled $false -MailNickName \""+ g.getEmail_address() +"\" -DisplayName '"+ g.getGroup_name() +"' -Description '"+g.getDescription()+"' -SecurityEnabled $true | ConvertTo-Json");

        if(response.length()<3){
            response="[]";
        }
        if(response.startsWith("{")){
            response = "[" + response + "]";
        }
        response = "{\"items\":"+response+"}";
        System.out.println(response);
        JSONArray json_array = new JSONObject(response).getJSONArray("items");
        JSONObject json_group = json_array.getJSONObject(0);
        String ObjectId=(String) json_group.get("ObjectId");

        g.setObjectId(ObjectId);

        for(User member: g.getUsers()){
            addMember(g,member);
        }

        for(User owner: g.getOwners()){
            addOwner(g, owner);
        }

        return ObjectId;
        //System.out.println(response);
    }

    public static void addOwner(Group g, User u) {
        String response = getConsole().execute("Add-AzureADGroupOwner -ObjectId  "+g.getObjectId()+" -RefObjectId "+ u.getObjectId());
    }
    
    public static void addMember(Group g, User u) {
        String response = getConsole().execute("Add-AzureADGroupMember -ObjectId  "+ g.getObjectId() +"  -RefObjectId "+ u.getObjectId() );
    }


    //TODO aggiungere template team EDU
    public static String CreateTeam(List<User> list, String nomeTeam, String description, User owner)
    {
        String command = "New-Team -Owner "+owner.getObjectId()+" -DisplayName  "+
                "\"" + nomeTeam.replace("-","")+"\"" + " -Description "+"\"" + description+"\"" + " | ConvertTo-Json";
        System.out.println(command);
        String response = getConsole().execute(command );
        if(response.length()<3){
            response="[]";
        }
        if(response.startsWith("{")){
            response = "[" + response + "]";
        }
        response = "{\"items\":"+response+"}";
        System.out.println(response);
        JSONArray json_array = new JSONObject(response).getJSONArray("items");
        JSONObject json_group = json_array.getJSONObject(0);
        String ObjectId=(String) json_group.get("GroupId");
        for(int i = 0; i<list.size(); i++)
        {
            response = getConsole().execute("Add-TeamUser -GroupId  "+ObjectId+" -User "+list.get(i).getObjectId()+" | ConvertTo-Json" );
        }
        return ObjectId;

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        PS.connect();
        List<Student> listS = PSUsers.getStudentList();
        List<User> listUtenti=new ArrayList<>();
        for(Student s: listS)
        {
            listUtenti.add(s);
        }
        List<Teacher> listT=PSUsers.getTeacherList();
        System.out.println(listT.get(0));
        String obj=CreateTeam(listUtenti, "Collegio", "prova", listT.get(0));
        System.out.println(obj);
    }

}
