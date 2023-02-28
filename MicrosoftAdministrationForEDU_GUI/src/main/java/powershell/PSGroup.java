package powershell;

import guipackage.ObservableGroup;
import guipackage.ObservableUser;
import org.json.JSONArray;
import org.json.JSONObject;
import school.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PSGroup extends PS{
    public static List<Group> getGroupList() throws IOException {
        try {
            String response = getConsole().execute("Get-MsolGroup -All | ConvertTo-Json", 500);// | Format-List ");
            if (response.trim().length() < 3) {
                response = "[]";
            }
            if (response.trim().startsWith("{")) {
                response = "[" + response + "]";
            }
            response = "{\"items\":" + response + "}";
            Files.writeString(Path.of("response.txt"), response, StandardCharsets.UTF_8);

            JSONArray json_array = new JSONObject(response).getJSONArray("items");
            ArrayList<Group> list = new ArrayList<>();
            //System.out.println(json_array.length());
            for (int i = 0; i < json_array.length(); i++) {

                JSONObject json_group = json_array.getJSONObject(i);

                Group group = Group.fromExisting();
                group.setObjectId(json_group.isNull("ObjectId") ? "" : (String) json_group.get("ObjectId"));
                group.setGroupName(json_group.isNull("DisplayName") ? "" : (String) json_group.get("DisplayName"));
                group.setDescription(json_group.isNull("Description") ? "" : (String) json_group.get("Description"));
                group.setEmail_address(json_group.isNull("EmailAddress") ? "" : (String) json_group.get("EmailAddress"));
                group.setGroup_type(json_group.isNull("GroupType") ? "" : json_group.get("GroupType").toString());
                //group.setUsers(this.getGroupMembers(group.getObject_id());
                group.startSyncronizing();
                list.add(group);
            }

            return list;
        } catch (Exception e){
            throw new IOException(e.getMessage());
        }
    }

    //TODO sistemare per prendere membri e owners, meglio usare AzureADGroup (member e owner)
    public static List<String> getGroupMembers(String ObjectId) throws IOException {
        try {
            String response = getConsole().execute("Get-AzureADGroupMember -ObjectId " + ObjectId + "| ConvertTo-Json", 300);// | Format-List ");
            //System.out.println(response);
            if (response.trim().length() < 3) {
                response = "[]";
            }
            if (response.trim().startsWith("{")) {
                response = "[" + response + "]";
            }
            response = "{\"items\":" + response + "}";
            //Files.writeString(Path.of("response.txt"), response, StandardCharsets.UTF_8);

            JSONArray json_array = new JSONObject(response).getJSONArray("items");

            ArrayList<String> list = new ArrayList<>();
            //System.out.println(json_array.length());
            for (int i = 0; i < json_array.length(); i++) {

                JSONObject json_group = json_array.getJSONObject(i);
                list.add(json_group.isNull("ObjectId") ? "" : (String) json_group.get("ObjectId"));
            }

            return list;
        }catch (Exception e){
            throw new IOException(e.getMessage());
        }
    }

    /**public static List<Group> loadGroupsWithMembers(List<Group> gruppi, List<User> utenti){
        for(Group gruppo : gruppi){
            gruppo.stopSyncronizing();

            List<String> object_ids_members

            gruppo.startSyncronizing();
        }
    }*/ //TODO da continuare

    public static List<String> getGroupOwners(String ObjectId) throws IOException {
        try {
            String response = getConsole().execute("Get-AzureADGroupOwner -ObjectId " + ObjectId + "| ConvertTo-Json");// | Format-List ");
            //System.out.println(response);
            if (response.trim().length() < 3) {
                response = "[]";
            }
            if (response.trim().startsWith("{")) {
                response = "[" + response + "]";
            }
            response = "{\"items\":" + response + "}";
            Files.writeString(Path.of("response.txt"), response, StandardCharsets.UTF_8);

            JSONArray json_array = new JSONObject(response).getJSONArray("items");

            ArrayList<String> list = new ArrayList<>();
            //System.out.println(json_array.length());
            for (int i = 0; i < json_array.length(); i++) {

                JSONObject json_group = json_array.getJSONObject(i);
                list.add(json_group.isNull("ObjectId") ? "" : (String) json_group.get("ObjectId"));
            }

            return list;
        } catch (Exception e){
            throw new IOException(e.getMessage());
        }
    }

    public static String CreateGroup(Group g)
    {
        //System.out.println(g.getGroupName());
        String response= getConsole().execute("New-AzureADGroup -MailEnabled $false -MailNickName '"+ g.getEmail_address() +"' -DisplayName '"+ g.getGroupName() +"' -Description '"+g.getDescription()+"' -SecurityEnabled $true | ConvertTo-Json", 400);

        if(response.trim().length()<3){
            response="[]";
        }
        if(response.trim().startsWith("{")){
            response = "[" + response + "]";
        }
        response = "{\"items\":"+response+"}";
        //System.out.println(response);
        JSONArray json_array = new JSONObject(response).getJSONArray("items");
        JSONObject json_group = json_array.getJSONObject(0);
        String ObjectId=(String) json_group.get("ObjectId");
        return ObjectId;

    }

    public static void addOwner(Group g, User u) {
        String response = getConsole().execute("Add-AzureADGroupOwner -ObjectId \""+g.getObjectId()+"\" -RefObjectId \""+ u.getObjectId()+"\"");
    }
    
    public static void addMember(Group g, User u) {
        String response = getConsole().execute("Add-AzureADGroupMember -ObjectId \""+ g.getObjectId() +"\" -RefObjectId \""+ u.getObjectId()+"\"" );
        System.out.println(response);
    }


    //TODO aggiungere template team EDU
    public static String CreateTeam(String nomeTeam, String description, User owner, String template)
    {
        String command = "New-Team  -DisplayName  "+
                "\"" + nomeTeam +"\"" + " -Description "+"\"" + description+ "\"";
        if(template.length()>0){
            command+= " -Template \"" + template + "\"";
        }
        if(owner!=null)
        {
            command+= " -Owner " + owner.getObjectId();
        }
        command+= " | ConvertTo-Json";
        //System.out.println(command);
        String response = getConsole().execute(command, 2000);

        if(response.trim().length()<3){
            response="[]";
        }
        if(response.trim().startsWith("{")){
            response = "[" + response + "]";
        }
        response = "{\"items\":"+response+"}";
        //System.out.println(response);
        JSONArray json_array = new JSONObject(response).getJSONArray("items");
        JSONObject json_group = json_array.getJSONObject(0);
        String ObjectId=(String) json_group.get("GroupId");
        return ObjectId;
    }
    public static void addOwner(ObservableGroup g, User u) {
        String response = getConsole().execute("Add-AzureADGroupOwner -ObjectId  "+g.getObjectId()+" -RefObjectId "+ u.getObjectId());
    }

    public static void addMember(ObservableGroup g, User u) {
        String response = getConsole().execute("Add-AzureADGroupMember -ObjectId  "+ g.getObjectId() +"  -RefObjectId "+ u.getObjectId() );
    }

    public static void addMember(Team team, User u){
        String response = getConsole().execute("Add-TeamUser -GroupId  "+team.getObjectId()+" -User "+u.getObjectId()+" | ConvertTo-Json" );
    }

    public static void createTeamAssemblea(HashMap<String, List<User>> ClassiStudenti)
    {
        //List<User> list=ClassiStudenti.get();

        for(String classe: ClassiStudenti.keySet())
        {
            Team t=new Team(classe, classe, null, "");
            //System.out.println("CLASSE: "+classe);
            List<User> listUser=ClassiStudenti.get(classe);
            for(User u: listUser)
            {
                //System.out.println("UTENTE: "+ u.getDisplayName());
                if(u.getTitle().equals("Studente"))
                {

                    t.addMember(u);
                }
                if(u.getTitle().equals("Teacher"))
                {
                    t.addOwner(u);

                }
            }
            /*for(User u: t.getMembers())
            {
                System.out.println("MEMBRI: "+u);
            }
            for(User u: t.getOwners())
            {
                System.out.println("OWNER: " +u);
            }*/
        }

    }

    public static void remove_member(ObservableGroup g, ObservableUser user)
    {
        String response = getConsole().execute("Remove-AzureADGroupMember -ObjectId  "+g.getObjectId()+" -MemberId "+user.getObjectId()+" | ConvertTo-Json" );
    }

    public static void remove_owner(ObservableGroup g, ObservableUser user)
    {
        String response = getConsole().execute("Remove-AzureADGroupOwner -ObjectId  "+g.getObjectId()+" -MemberId "+user.getObjectId()+" | ConvertTo-Json" );
    }

    public static void remove_member(String g, User user)
    {
        String response = getConsole().execute("Remove-AzureADGroupMember -ObjectId  "+g+" -MemberId "+user.getObjectId()+" | ConvertTo-Json" );
    }

    public static void remove_owner(String g, User user)
    {
        String response = getConsole().execute("Remove-AzureADGroupOwner -ObjectId  "+g+" -OwnerId "+user.getObjectId()+" | ConvertTo-Json" );
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        PS.connect();
        List<Student> listS = PSUsers.getStudentList();

        for(Student s : listS){
            System.out.println(s);
        }

        List<User> listUtenti=new ArrayList<>();
        for(Student s: listS)
        {
            listUtenti.add(s);
        }
        List<Teacher> listT=PSUsers.getTeacherList();
        //System.out.println(listT.get(0));
        String obj=CreateTeam( "Collegio", "prova", listT.get(0), "");
        //System.out.println(obj);
    }


    public static void delete_group(String objid)
    {
        String response = getConsole().execute("Remove-AzureADGroup -objectid "+objid );
    }

    public static void changename(Group group, String group_name) {
        String response = getConsole().execute("Set-AzureADGroup -objectid "+group.getObjectId() +" -DisplayName \""+group_name+"\"");
    }

    public static void changedescription(Group group, String description) {

        String response = getConsole().execute("Set-AzureADGroup -objectid "+group.getObjectId() +" -Description \""+description+"\"");
    }
}
