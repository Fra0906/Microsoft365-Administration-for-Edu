package utility;

import powershell.PSGroup;
import powershell.PSUsers;
import school.Group;
import school.Student;
import school.Teacher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class UtilityStudenti {

    public static List<Student> searchStudent(List<Student> all_student, String name, String lastname, String classroom, String scuola){
        List<Student> result_list = new ArrayList<>();

        for(Student student : all_student){
            if(name.length()>0)
                if(!(student.getFirstName().toLowerCase().contains(name.toLowerCase())
                        || student.getDisplayName().toLowerCase().contains(name.toLowerCase())))
                    break;
            if(lastname.length()>0)
                if(!student.getDisplayName().toLowerCase().contains(lastname.toLowerCase())
                        || student.getLastName().toLowerCase().contains(lastname.toLowerCase()))
                    break;
            if(classroom.length()>0)
                if(!student.getClassroom().toLowerCase().contains(classroom.toLowerCase()))
                    break;
            if(scuola.length()>0)
                if(!student.getSchool().toLowerCase().contains(scuola.toLowerCase()))
                    break;

            result_list.add(student);
        }

        return result_list;
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
        System.out.println(student.getClassroom());

        student.setClassroom(new_class);
        System.out.println(student.getClassroom());
        PSUsers.ModifyUser(student.getObjectId(), student);
    }

    public static void promoteStudent(Student student){
        String oldclass = student.getClassroom();
        String newclass = (Integer.parseInt(oldclass.substring(0,1))+1) + oldclass.substring(1);
        //student.setClassroom(newclass);
        System.out.println(oldclass + "  ->  " + newclass);
        promoteStudent(student, newclass);
    }

    public static Group createClassGroup(List<Student> students, String group_name){
        Group group = new Group();
        if(students.isEmpty()){
            return null;
        }
        group.setGroup_name(group_name);
        group.setEmail_address(group_name);

        for(Student s:students){
            group.getUsers().add(s);
        }


        String object_id = PSGroup.CreateGroup(group);
        group.setObjectId(object_id);

        return group;
    }


    public static void main(String[] args) throws IOException {
        PSUsers ps = new PSUsers();
        List<Student> list = ps.getStudentList();
        for(Student s : list){
            System.out.println(s);
        }

        Scanner sc = new Scanner(System.in);

        while (true){
            String name;
            String lastname;
            String classe;
            String scuola;

            System.out.print("name: ");
            name = sc.nextLine();
            System.out.print("lastname: ");
            lastname = sc.nextLine();
            System.out.print("classe: ");
            classe = sc.nextLine();
            System.out.print("scuola: ");
            scuola = sc.nextLine();

            List<Student> list_ric = searchStudent(list, name,lastname,classe,scuola);
            for(Student s : list_ric){
                System.out.println(s);
            }


            Group new_group = createClassGroup(list_ric, "gruppo_prova");
            System.out.println(new_group.getUsers());
        }


    }
}
