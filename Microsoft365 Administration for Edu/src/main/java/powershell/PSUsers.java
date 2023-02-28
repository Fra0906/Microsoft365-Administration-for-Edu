package powershell;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import school.Student;
import school.Teacher;
import utility.UtilityUser;
import school.User;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;


public class PSUsers extends PS {

    public static List<Student> getStudentList() throws IOException {
        String response = getConsole().execute("Get-MsolUser -Title \"Student\"| ConvertTo-Json");// | Format-List ");
        if (response.length() < 3) {
            response = "[]";
        }
        if (response.startsWith("{")) {
            response = "[" + response + "]";
        }
        response = "{\"items\":" + response + "}";

        //System.out.println(response);
        //Files.writeString(Path.of("response.txt"), response, StandardCharsets.UTF_8);

        JSONArray json_array = new JSONObject(response).getJSONArray("items");
        ArrayList<Student> list = new ArrayList<>();
        System.out.println(json_array.length());
        for (int i = 0; i < json_array.length(); i++) {

            JSONObject json_student = json_array.getJSONObject(i);

            Student student = new Student();
            student.setFirstName(json_student.isNull("FirstName") ? "" : (String) json_student.get("FirstName"));
            student.setLastName(json_student.isNull("LastName") ? "" : (String) json_student.get("LastName"));
            student.setSchool(json_student.isNull("Department") ? "" : (String) json_student.get("Department"));
            student.setClassroom(json_student.isNull("Office") ? "" : (String) json_student.get("Office"));
            student.setAlternateEmailAddress(json_student.isNull("AlternateEmailAddress") ? "" : (String) json_student.get("AlternateEmailAddress"));
            student.setBlockCredential(json_student.isNull("BlockCredential") ? false : (boolean) json_student.get("BlockCredential"));
            student.setDisplayName(json_student.isNull("DisplayName") ? "" : (String) json_student.get("DisplayName"));
            student.setUserPrincipalName(json_student.isNull("UserPrincipalName") ? "" : (String) json_student.get("DisplayName"));
            student.setObjectId(json_student.isNull("ObjectId") ? "" : (String) json_student.get("ObjectId"));

            list.add(student);
        }

        return list;
    }

    public static List<Teacher> getTeacherList() throws IOException {
        String response = getConsole().execute("Get-MsolUser -Title \"Teacher\"| ConvertTo-Json");// | Format-List ");
        System.out.println("Lunghezza " + response.length());
        if (response.length() < 3) {
            response = "[]";
        }
        if (response.startsWith("{")) {
            response = "[" + response + "]";
        }
        response = "{\"items\":" + response + "}";
        //Files.writeString(Path.of("response.txt"), response, StandardCharsets.UTF_8);
        //System.out.println(response);


        JSONArray json_array = new JSONObject(response).getJSONArray("items");
        ArrayList<Teacher> list = new ArrayList<>();
        System.out.println(json_array.length());
        for (int i = 0; i < json_array.length(); i++) {

            JSONObject json_teacher = json_array.getJSONObject(i);

            Teacher student = new Teacher();
            student.setFirstName(json_teacher.isNull("FirstName") ? "" : (String) json_teacher.get("FirstName"));
            student.setLastName(json_teacher.isNull("LastName") ? "" : (String) json_teacher.get("LastName"));
            student.setDepartment(json_teacher.isNull("Department") ? "" : (String) json_teacher.get("Department"));
            student.setOffice(json_teacher.isNull("Office") ? "" : (String) json_teacher.get("Office"));
            student.setAlternateEmailAddress(json_teacher.isNull("AlternateEmailAddress") ? "" : (String) json_teacher.get("AlternateEmailAddress"));
            student.setBlockCredential(json_teacher.isNull("BlockCredential") ? false : (boolean) json_teacher.get("BlockCredential"));
            student.setDisplayName(json_teacher.isNull("DisplayName") ? "" : (String) json_teacher.get("DisplayName"));
            student.setUserPrincipalName(json_teacher.isNull("UserPrincipalName") ? "" : (String) json_teacher.get("DisplayName"));
            student.setObjectId(json_teacher.isNull("ObjectId") ? "" : (String) json_teacher.get("ObjectId"));

            list.add(student);
        }

        return list;
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
        String response = getConsole().execute("Remove-MsolUser -ObjectId " + ObjectId + "| ConvertTo-Json");// | Format-List ");
    }

    public static void AddStudent(Student s) {
        //System.out.println("ok");
        if(s.getUserPrincipalName()==null | s.getFirstName()==null | s.getLastName()==null | s.getDisplayName()==null)
        {
            System.out.println("Mancano requisiti fondamentali");
        }
        else
        {
            //System.out.println(s.getUserPrincipalName()+"-"+s.getFirstName()+"-"+s.getLastName()+"-"+s.getTitle());
            String response = getConsole().execute("New-MsolUser -UserPrincipalName '"+ s.getUserPrincipalName() +"' -FirstName '"+ s.getFirstName() +"' -LastName '"+s.getLastName()+"' -Title '"+s.getTitle()+"' -DisplayName '"+s.getDisplayName()+"' | ConvertTo-Json");
            if(s.getSchool()!=null)
            {
                response=getConsole().execute("Set-MsolUser -UserPrincipalName '"+s.getUserPrincipalName()+"' -Department '"+s.getSchool()+"'");
            }
            if(s.getClassroom()!=null)
            {
                System.out.println("sono qui");
                response=getConsole().execute("Set-MsolUser -UserPrincipalName '"+s.getUserPrincipalName()+"' -Office '"+s.getClassroom()+"'");
            }
            if(s.getAlternateEmailAddress()!=null)
            {
                response=getConsole().execute("Set-MsolUser -UserPrincipalName '"+s.getUserPrincipalName()+"' -AlternateEmailAdrress '"+s.getAlternateEmailAddress()+"'");
            }
        }

    }

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
    }


    public static void BlockAccount(String ObjectId) {
        String response = PS.getConsole().execute("Set-MsolUser -ObjectId " + ObjectId + " -BlockCredential $true | ConvertTo-Json");
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


    public static List<Student> AddStudentFromCSV(String fileName) throws IOException {

        //String response = getConsole().execute("New-MsolUser -UserPrincipalName $_.UserPrincipalName -DisplayName $_.DisplayName -FirstName $_.FirstName -LastName $_.LastName  -Title $_.Title -Department $_.Department -Office $_.Office -alternate_email_address $_.alternate_email_address");
        List<Student> list = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
            String line = br.readLine();

            boolean i;
            i = false;


            while (line != null) {
                String[] attributes = line.split(",");

                Student stud = new Student();
                if (i == false) {
                    i = true;
                } else {


                    stud.setUserPrincipalName(attributes[0]);
                    stud.setDisplayName(attributes[1]);
                    stud.setFirstName(attributes[2]);
                    stud.setLastName(attributes[3]);
                    //stud.setTitle(attributes[4]);
                    stud.setDepartment(attributes[4]);
                    stud.setOffice(attributes[5]);
                    stud.setAlternateEmailAddress(attributes[6]);

                    AddStudent(stud);
                    //System.out.println(stud);
                    list.add(stud);

                    line = br.readLine();
                }
            }
        }
        return list;
    }

    public static List<Teacher> AddTeacherFromCSV(String fileName) throws IOException {

        //String response = getConsole().execute("New-MsolUser -UserPrincipalName $_.UserPrincipalName -DisplayName $_.DisplayName -FirstName $_.FirstName -LastName $_.LastName  -Title $_.Title -Department $_.Department -Office $_.Office -alternate_email_address $_.alternate_email_address");
        List<Teacher> list = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);


        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
            String line = br.readLine();

            boolean i;
            i = false;

            while (line != null) {
                String[] attributes = line.split(",");

                Teacher teach = new Teacher();
                if (i == false) {
                    i = true;
                } else {

                    teach.setUserPrincipalName(attributes[0]);
                    teach.setDisplayName(attributes[1]);
                    teach.setFirstName(attributes[2]);
                    teach.setLastName(attributes[3]);
                    //teach.setTitle(attributes[4]);
                    teach.setDepartment(attributes[4]);
                    teach.setAlternateEmailAddress(attributes[5]);

                    AddTeacher(teach);
                    //System.out.println(teach);
                    list.add(teach);

                    line = br.readLine();
                }
            }
        }
    return list;
    }



    public static void main(String[] args) throws IOException, InterruptedException {
        PSUsers ps = new PSUsers();
        List<Student> list = PSUsers.getStudentList();
        //AddTeacherFromCSV("C:\\Users\\Utente\\Desktop\\accounts2.csv");
        AddStudentFromCSV("C:\\Users\\Utente\\Desktop\\accounts.csv");



    }


}

