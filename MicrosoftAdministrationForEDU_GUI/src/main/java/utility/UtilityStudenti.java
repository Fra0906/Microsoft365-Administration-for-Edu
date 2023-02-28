package utility;

import powershell.PS;
import powershell.PSGroup;
import powershell.PSUsers;
import school.Student;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class UtilityStudenti {

    public static List<Student> searchStudent(List<Student> all_student, String name, String lastname, String classroom, String scuola){
        return searchStudent(all_student,name,lastname,classroom,scuola,false, false,  false,  false);
    }

    public static List<Student> searchStudent(List<Student> all_student, String name, String lastname, String classroom, String scuola, boolean regex_nome, boolean regex_cognome, boolean regex_scuola, boolean regex_classe){
        List<Student> result_list = new ArrayList<>();

        for(Student student : all_student){
            if(name.length()>0 && student.getFirstName()!=null) {
                try{
                    if(regex_nome) {
                        Matcher matcher = Pattern.compile(name.toLowerCase()).matcher(student.getFirstName().toLowerCase());
                        if (!matcher.matches()) {
                            continue;
                        }
                    }
                    else {
                        if (!(student.getFirstName().toLowerCase().contains(name.toLowerCase())))
                            continue;
                    }

                } catch (PatternSyntaxException e){
                    if (!(student.getFirstName().toLowerCase().contains(name.toLowerCase())))
                        continue;
                }
            }

            if(lastname.length()>0 && student.getLastName()!=null) {
                try{
                    if(regex_cognome) {
                        Matcher matcher = Pattern.compile(lastname.toLowerCase()).matcher(student.getLastName().toLowerCase());
                        if (!matcher.matches()) {
                            continue;
                        }
                    }
                    else {
                        if (!(student.getLastName().toLowerCase().contains(lastname.toLowerCase())))
                            continue;
                    }

                } catch (PatternSyntaxException e){
                    if (!(student.getLastName().toLowerCase().contains(lastname.toLowerCase())))
                        continue;
                }
            }
            /*if(lastname.length()>0 && student.getLastName()!=null)
                if(!(student.getDisplayName().toLowerCase().contains(lastname.toLowerCase())
                        | student.getLastName().toLowerCase().contains(lastname.toLowerCase())))
                    continue;*/

            if(classroom.length()>0 && student.getClassroom()!=null) {
                try{
                    if(regex_classe) {
                        Matcher matcher = Pattern.compile(classroom.toLowerCase()).matcher(student.getClassroom().toLowerCase());
                        if (!matcher.matches()) {
                            continue;
                        }
                    }
                    else {
                        if (!(student.getClassroom().toLowerCase().contains(classroom.toLowerCase())))
                            continue;
                    }

                } catch (PatternSyntaxException e){
                    if (!(student.getClassroom().toLowerCase().contains(classroom.toLowerCase())))
                        continue;
                }
            }
            /*if(classroom.length()>0 && student.getClassroom()!=null)
                if(!student.getClassroom().toLowerCase().contains(classroom.toLowerCase()))
                    continue;*/

            if(scuola.length()>0 && student.getSchool()!=null) {
                try{
                    if(regex_scuola) {
                        Matcher matcher = Pattern.compile(scuola.toLowerCase()).matcher(student.getSchool().toLowerCase());
                        if (!matcher.matches()) {
                            continue;
                        }
                    }
                    else {
                        if (!(student.getSchool().toLowerCase().contains(scuola.toLowerCase())))
                            continue;
                    }

                } catch (PatternSyntaxException e){
                    if (!(student.getSchool().toLowerCase().contains(scuola.toLowerCase())))
                        continue;
                }
            }

            /*if(student.getSchool()!=null && scuola.length()>0)
                if(!student.getSchool().toLowerCase().contains(scuola.toLowerCase()))
                    continue;*/

            result_list.add(student);
        }


        return result_list;
    }

    public static List<Student> searchStudentOLD(List<Student> all_student, String name, String lastname, String classroom, String scuola) {
        List<Student> result_list = new ArrayList<>();

        for (Student student : all_student) {
            if (name.length() > 0 && student.getFirstName() != null)
                if (!(student.getFirstName().toLowerCase().contains(name.toLowerCase())
                        | student.getDisplayName().toLowerCase().contains(name.toLowerCase())))
                    continue;
                if (lastname.length() > 0 && student.getLastName() != null)
                    if (!(student.getDisplayName().toLowerCase().contains(lastname.toLowerCase())
                            | student.getLastName().toLowerCase().contains(lastname.toLowerCase())))
                        continue;
                if (classroom.length() > 0 && student.getClassroom() != null)
                    if (!student.getClassroom().toLowerCase().contains(classroom.toLowerCase()))
                        continue;
                if (student.getSchool() != null && scuola.length() > 0)
                    if (!student.getSchool().toLowerCase().contains(scuola.toLowerCase()))
                        continue;

                result_list.add(student);
            }


            return result_list;
        }



    public static Student findStudent(List<Student> list, String user_principal_name){
        for(Student s:list){
            if(s.getUserPrincipalName().equals(user_principal_name)){
                return s;
            }
        }
        return null;
    }

    public static boolean studentExists(List<Student> list,String user_principal_name){
        for(Student s:list){
            if(s.getUserPrincipalName().equals(user_principal_name)){
                return true;
            }
        }
        return false;
    }


    public static void exportCSV(List<Student> list, String path) throws IOException {
        String SEP = ",";

        if(list.isEmpty())
            return;

        Set<String> heads = list.get(0).getFields().keySet();
        String s = "";
        for(String head : heads){
            s += head+SEP;
        }
        s+="\n";

        for(Student student : list){
            for(String head : heads){
                s += student.getFields().get(head).toString().replace(SEP, "-")+SEP;
            }
            s+="\n";
        }

        Files.writeString(Path.of(path), s, StandardCharsets.UTF_8);
    }

    public static void promoteStudents(List<Student> list){
        for(Student s: list){
            promoteStudent(s);
        }
    }

    public static void promoteStudents(List<Student> list, String newclass){
        for(Student s: list){
            promoteStudent(s, newclass);
        }
    }

    public static void promoteStudent(Student student, String new_class){
        //System.out.println(student.getClassroom());

        student.setClassroom(new_class);
        //System.out.println(student.getClassroom());
        //PSUsers.ModifyUser(student.getObjectId(), student);
    }

    public static void promoteStudent(Student student){
        String oldclass = student.getClassroom();
        String newclass = (Integer.parseInt(oldclass.substring(0,1))+1) + oldclass.substring(1);
        //student.setClassroom(newclass);
        //System.out.println(oldclass + "  ->  " + newclass);
        promoteStudent(student, newclass);
    }


    public static void main(String[] args) throws IOException {

        List<Student> lista = PSUsers.getStudentList();

        for(Student s: lista){
            System.out.println(s);
        }

        String regex = "[rob]";
        List<Student> newlist = searchStudent(lista,regex,"","","",true, false,false,false);
        for(Student s : newlist){
            //System.out.println(s);
        }

        /*newlist.get(0).setClassroom("1A");
        newlist.get(0).setDepartment("Primaria");
        System.out.println(newlist.get(0));*/


    }
}
