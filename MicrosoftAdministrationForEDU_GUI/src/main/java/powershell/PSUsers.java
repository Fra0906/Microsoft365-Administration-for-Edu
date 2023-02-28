package powershell;

import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import school.Student;
import school.Teacher;
import school.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class PSUsers extends PS {

    public static List<Student> getStudentList() throws IOException {
        try {
            String response = getConsole().execute("$users = Get-MsolUser -Title \"Student\"", 1000);// | Format-List ");
            response += getConsole().execute("$users | Select-Object FirstName, Lastname, Department, Office, " +
                    "AlternateEmailAddress, BlockCredential, DisplayName, UserPrincipalName, ObjectID, " +
                    "@{label=\"License\"; expression={if($_.Licenses.AccountSkuId){$_.Licenses.AccountSkuId} " +
                    "else {\"null\"}}} | ConvertTo-Json");

            if (response.trim().length() < 3) {
                return new ArrayList<Student>();
            }
            if (response.trim().startsWith("{")) {
                response = "[" + response + "]";
            }
            response = "{\"items\":" + response + "}";
            //Files.writeString(Path.of("response.txt"), response, StandardCharsets.UTF_8);

            JSONArray json_array = new JSONObject(response).getJSONArray("items");
            ArrayList<Student> list = new ArrayList<>();
            //System.out.println(json_array.length());
            for (int i = 0; i < json_array.length(); i++) {

                JSONObject json_student = json_array.getJSONObject(i);

                Student student = Student.fromExisting();
                student.setFirstName(json_student.isNull("FirstName") ? "" : (String) json_student.get("FirstName"));
                student.setLastName(json_student.isNull("LastName") ? "" : (String) json_student.get("LastName"));
                student.setSchool(json_student.isNull("Department") ? "" : (String) json_student.get("Department"));
                student.setClassroom(json_student.isNull("Office") ? "" : (String) json_student.get("Office"));
                student.setAlternateEmailAddress(json_student.isNull("AlternateEmailAddress") ? "" : (String) json_student.get("AlternateEmailAddress"));
                student.setBlockCredential(json_student.isNull("BlockCredential") ? false : (boolean) json_student.get("BlockCredential"));
                student.setDisplayName(json_student.isNull("DisplayName") ? "" : (String) json_student.get("DisplayName"));
                student.setUserPrincipalName(json_student.isNull("UserPrincipalName") ? "" : (String) json_student.get("UserPrincipalName"));
                student.setObjectId(json_student.isNull("ObjectId") ? "" : (String) json_student.get("ObjectId"));
                student.setLicense(json_student.isNull("License") ? "" : (String) json_student.get("License"));
                student.startSyncronizing();
                list.add(student);
            }

            return list;
        } catch (JSONException e){
            throw new IOException(e.getMessage());
        }
    }

    public static List<Teacher> getTeacherList() throws IOException {
        try {
            String response = getConsole().execute("$users = Get-MsolUser -Title \"Teacher\"", 1000);// | Format-List ");
            response += getConsole().execute("$users | Select-Object FirstName, Lastname, Department, Office, " +
                    "AlternateEmailAddress, BlockCredential, DisplayName, UserPrincipalName, ObjectID, " +
                    "@{label=\"License\"; expression={if($_.Licenses.AccountSkuId){$_.Licenses.AccountSkuId} " +
                    "else {\"null\"}}} | ConvertTo-Json");
            //System.out.println("Lunghezza " + response.length());
            if (response.trim().length() < 3) {
                response = "[]";
            }
            if (response.trim().startsWith("{")) {
                response = "[" + response + "]";
            }
            response = "{\"items\":" + response + "}";
            //Files.writeString(Path.of("response.txt"), response, StandardCharsets.UTF_8);
            //System.out.println(response);


            JSONArray json_array = new JSONObject(response).getJSONArray("items");
            ArrayList<Teacher> list = new ArrayList<>();
            //System.out.println(json_array.length());
            for (int i = 0; i < json_array.length(); i++) {

                JSONObject json_teacher = json_array.getJSONObject(i);

                Teacher teacher = Teacher.fromExisting();
                teacher.setFirstName(json_teacher.isNull("FirstName") ? "" : (String) json_teacher.get("FirstName"));
                teacher.setLastName(json_teacher.isNull("LastName") ? "" : (String) json_teacher.get("LastName"));
                teacher.setDepartment(json_teacher.isNull("Department") ? "" : (String) json_teacher.get("Department"));
                teacher.setOffice(json_teacher.isNull("Office") ? "" : (String) json_teacher.get("Office"));
                teacher.setAlternateEmailAddress(json_teacher.isNull("AlternateEmailAddress") ? "" : (String) json_teacher.get("AlternateEmailAddress"));
                teacher.setBlockCredential(json_teacher.isNull("BlockCredential") ? false : (boolean) json_teacher.get("BlockCredential"));
                teacher.setDisplayName(json_teacher.isNull("DisplayName") ? "" : (String) json_teacher.get("DisplayName"));
                teacher.setUserPrincipalName(json_teacher.isNull("UserPrincipalName") ? "" : (String) json_teacher.get("UserPrincipalName"));
                teacher.setObjectId(json_teacher.isNull("ObjectId") ? "" : (String) json_teacher.get("ObjectId"));
                teacher.setLicense(json_teacher.isNull("License") ? "" : (String) json_teacher.get("License"));
                teacher.startSyncronizing();

                list.add(teacher);
            }

            return list;
        } catch (Exception e){
            throw new IOException(e.getMessage());
        }
    }

    public static void changeUserPassword(String user_principal_name, String password){
        String response = getConsole().execute("Set-MsolUserPassword -UserPrincipalName \""+ user_principal_name + "\" -NewPassword \""+password+"\"");
        
    }

    public static void ModifyUser(String ObjectId, User user) {
        String s = user.getAlternateEmailAddress();
        if (s.length() > 0) {
            String response = getConsole().execute("Get-MsolUser | where ObjectId -eq " + ObjectId + "  | Set-MsolUser -AlternateEmailAddresses " + s + " | ConvertTo-Json");
        }
        s = user.getDepartment();
        if (s.length() > 0) {
            String response = getConsole().execute("Get-MsolUser | where ObjectId -eq " + ObjectId + "  | Set-MsolUser -Department " + s + " | ConvertTo-Json");
        }
        s = user.getDisplayName();
        if (s.length() > 0) {
            String response = getConsole().execute("Get-MsolUser | where ObjectId -eq " + ObjectId + "  | Set-MsolUser -DisplayName " + s + " | ConvertTo-Json");
        }
        s = user.getFirstName();
        if (s.length() > 0) {
            String response = getConsole().execute("Get-MsolUser | where ObjectId -eq " + ObjectId + "  | Set-MsolUser -FirstName " + s + " | ConvertTo-Json");
        }
        s = user.getLastName();
        if (s.length() > 0) {
            String response = getConsole().execute("Get-MsolUser | where ObjectId -eq " + ObjectId + "  | Set-MsolUser -LastName " + s + " | ConvertTo-Json");
        }
        s = user.getUserPrincipalName();
        if (s.length() > 0) {
            String response = getConsole().execute("Get-MsolUser | where ObjectId -eq " + ObjectId + "  | Set-MsolUser -UserPrincipalName  " + s + " | ConvertTo-Json");
        }
        s = user.getOffice();
        if (s.length() > 0) {
            String response = getConsole().execute("Get-MsolUser | where ObjectId -eq " + ObjectId + "  | Set-MsolUser -Office  " + s + " | ConvertTo-Json");
        }
        s = user.getTitle();
        if (s.length() > 0) {
            String response = getConsole().execute("Get-MsolUser | where ObjectId -eq " + ObjectId + "  | Set-MsolUser -Title  " + s + " | ConvertTo-Json");
        }
        String response = getConsole().execute("Get-MsolUser | where ObjectId -eq " + ObjectId + "  | Set-MsolUser -BlockCredential   " + user.isBlockCredential() + " | ConvertTo-Json");


    }

    public static void DeleteUser(String ObjectId) throws IOException {
        String response = getConsole().execute("Remove-AzureADUser -ObjectId " + ObjectId);// | Format-List ");
    }

    public static JSONObject AddUser(User s) {
        //System.out.println("ok");
        if(s.getUserPrincipalName()==null | s.getFirstName()==null | s.getLastName()==null | s.getDisplayName()==null)
        {
            //System.out.println("Mancano requisiti fondamentali");
            return null;
        }
        else
        {
            //System.out.println(s.getUserPrincipalName()+"-"+s.getFirstName()+"-"+s.getLastName()+"-"+s.getTitle());
            String response = getConsole().execute("New-MsolUser -UserPrincipalName '"+ s.getUserPrincipalName() +"' -FirstName '"+ s.getFirstName() +"' -LastName '"+s.getLastName()+"' -Title '"+s.getTitle()+"' -DisplayName '"+s.getDisplayName()+"' -UsageLocation IT | ConvertTo-Json", 400);

            if(response.trim().length()<3){
                response="[]";
            }
            if(response.trim().startsWith("{")){
                response = "[" + response + "]";
            }
            response = "{\"items\":"+response+"}";
            //System.out.println(response);
            try {
                JSONArray json_array = new JSONObject(response).getJSONArray("items");
                JSONObject json_group = json_array.getJSONObject(0);
                String ObjectId=(String) json_group.get("ObjectId");
                s.setObjectId(ObjectId);
                return json_group;

            } catch (JSONException e){
                System.out.println(e.getMessage());
                //System.out.println(response);
                try {
                    Files.writeString(Path.of("response.txt"), response, StandardCharsets.UTF_8);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
            return null;
            //return ObjectId;

        }
    }

    public static void setDepartment(User u){
        if (u.getDepartment() != null)
        {
            String response = getConsole().execute("Set-MsolUser -UserPrincipalName '" + u.getUserPrincipalName() + "' -Department '" + u.getDepartment() + "'");
        }
    }

    public static void setOffice(User u){
        if (u.getOffice() != null)
        {
            String response = getConsole().execute("Set-MsolUser -UserPrincipalName '" + u.getUserPrincipalName() + "' -Office '" + u.getOffice() + "'");
            //System.out.println(response);
        }
    }

    public static void setDisplayName(User u){
        if (u.getDisplayName() != null)
        {
            String response = getConsole().execute("Set-MsolUser -UserPrincipalName '" + u.getUserPrincipalName() + "' -DisplayName '" + u.getDisplayName() + "'");
        }
    }

    public static void setFirstName(User u){
        if (u.getDisplayName() != null)
        {
            String response = getConsole().execute("Set-MsolUser -UserPrincipalName '" + u.getUserPrincipalName() + "' -FirstName '" + u.getFirstName() + "'");
        }
    }

    public static void setLastName(User u){
        if (u.getDisplayName() != null)
        {
            String response = getConsole().execute("Set-MsolUser -UserPrincipalName '" + u.getUserPrincipalName() + "' -LastName '" + u.getLastName() + "'");
        }
    }


    /**
    public static void AddTeacher(Teacher t) {
        if (t.getUserPrincipalName() == null | t.getFirstName() == null | t.getLastName() == null | t.getDisplayName() == null) {
            System.out.println("Mancano requisiti fondamentali");
        } else {
            //System.out.println(s.getUserPrincipalName()+"-"+s.getFirstName()+"-"+s.getLastName()+"-"+s.getTitle());
            String response = getConsole().execute("New-MsolUser -UserPrincipalName '" + t.getUserPrincipalName() + "' -FirstName '" + t.getFirstName() + "' -LastName '" + t.getLastName() + "' -Title '" + t.getTitle() + "' -DisplayName '" + t.getDisplayName() + "' | ConvertTo-Json");
            if (t.getDepartment() != null) //materie
            {
                response = getConsole().execute("Set-MsolUser -UserPrincipalName '" + t.getUserPrincipalName() + "' -Department '" + t.getDepartment() + "'");
            }
            if (t.getOffice() != null) {
                response = getConsole().execute("Set-MsolUser -UserPrincipalName '" + t.getUserPrincipalName() + "' -Office '" + t.getOffice() + "'");
            }
            if (t.getAlternateEmailAddress() != null) {
                response = getConsole().execute("Set-MsolUser -UserPrincipalName '" + t.getUserPrincipalName() + "' -AlternateEmailAdrress '" + t.getAlternateEmailAddress() + "'");
            }
        }
    }*/


    public static void BlockAccount(String ObjectId, boolean block) {
        String response = PS.getConsole().execute("Set-MsolUser -ObjectId " + ObjectId + " -BlockCredential $"+block+" | ConvertTo-Json");
    }

    public static void BlockAccount(String ObjectId) {
        BlockAccount(ObjectId, true);
    }

    public static void BlockTitleAccounts(String Title) {
        String response = PS.getConsole().execute("Get-MsolUser | where Title -eq " + Title + "  | Set-MsolUser -BlockCredential  $true | ConvertTo-Json");
    }

    public static void BlockSchoolAccounts(String School) {
        String response = PS.getConsole().execute("Get-MsolUser | where Department -eq " + School + "  | Set-MsolUser -BlockCredential  $true | ConvertTo-Json");
    }

    public static void BlockClassAccounts(String ClassRoom) {
        String response = PS.getConsole().execute("Get-MsolUser | where Office -eq " + ClassRoom + "  | Set-MsolUser -BlockCredential  $true | ConvertTo-Json");
    }


    public static List<User> AddStudentFromCSV(String fileName) throws IOException {

        //String response = getConsole().execute("New-MsolUser -UserPrincipalName $_.UserPrincipalName -DisplayName $_.DisplayName -FirstName $_.FirstName -LastName $_.LastName  -Title $_.Title -Department $_.Department -Office $_.Office -alternate_email_address $_.alternate_email_address");

        Path pathToFile = Paths.get(fileName);
        List<User> list = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
            String line = br.readLine();

            boolean i;
            i = false;

            while (line != null) {
                String[] attributes = line.split(",");


                if (i == false) {
                    i = true;
                } else {
                    String user_principal_name = attributes[0];
                    String display_name = attributes[1];
                    String name = attributes[2];
                    String last_name = attributes[3];

                    Student stud = new Student(name,last_name,user_principal_name,display_name);
                    /**stud.setUserPrincipalName(attributes[0]);
                    stud.setDisplayName(attributes[1]);
                    stud.setFirstName(attributes[2]);
                    stud.setLastName(attributes[3]);*/
                    //stud.setTitle(attributes[4]);
                    stud.setDepartment(attributes[4]);
                    stud.setOffice(attributes[5]);
                    stud.setAlternateEmailAddress(attributes[6]);
                    list.add(stud);
                    line = br.readLine();
                }
            }
        }
        return list;
    }

    public static void AddTeacherFromCSV(String fileName) throws IOException {

        //String response = getConsole().execute("New-MsolUser -UserPrincipalName $_.UserPrincipalName -DisplayName $_.DisplayName -FirstName $_.FirstName -LastName $_.LastName  -Title $_.Title -Department $_.Department -Office $_.Office -alternate_email_address $_.alternate_email_address");

        Path pathToFile = Paths.get(fileName);


        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
            String line = br.readLine();

            boolean i;
            i = false;

            while (line != null) {
                String[] attributes = line.split(",");


                if (i == false) {
                    i = true;
                } else {

                    String user_principal_name = attributes[0];
                    String display_name = attributes[1];
                    String name = attributes[2];
                    String last_name = attributes[3];

                    Teacher teach = new Teacher(name,last_name,user_principal_name,display_name);

                    /*teach.setUserPrincipalName(attributes[0]);
                    teach.setDisplayName(attributes[1]);
                    teach.setFirstName(attributes[2]);
                    teach.setLastName(attributes[3]);*/
                    //teach.setTitle(attributes[4]);
                    teach.setDepartment(attributes[4]);
                    teach.setAlternateEmailAddress(attributes[5]);

                    //System.out.println(teach);

                    line = br.readLine();
                }
            }
        }

    }

    public static void addLincenses(User u) {
        if (u.getLicense() != null) {
            String response = PS.getConsole().execute("Set-MsolUserLicense -UserPrincipalName " + u.getUserPrincipalName() + " -AddLicenses " + u.getLicense());
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        PSUsers ps = new PSUsers();
        //AddTeacherFromCSV("C:\\Users\\Utente\\Desktop\\accounts2.csv");
        AddStudentFromCSV("C:\\Users\\Utente\\Desktop\\accounts.csv");


    }


    public static void removeLicenses(User user) {
        if (user.getLicense() != null) {
            String response = PS.getConsole().execute("Set-MsolUserLicense -UserPrincipalName " + user.getUserPrincipalName() + " -RemoveLicenses " + user.getLicense());

        }
    }
}

