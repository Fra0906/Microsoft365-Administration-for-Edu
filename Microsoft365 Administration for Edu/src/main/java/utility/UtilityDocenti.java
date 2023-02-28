package utility;

import powershell.PSGroup;
import powershell.PSUsers;
import school.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class UtilityDocenti {

    public static List<Teacher> searchTeacher(List<Teacher> all_teacher, String name, String lastname, String subject){
        List<Teacher> result_list = new ArrayList<>();

        for(Teacher teacher : all_teacher){
            if(name.length()>0)
                if(!(teacher.getFirstName().toLowerCase().contains(name.toLowerCase())
                        || teacher.getDisplayName().toLowerCase().contains(name.toLowerCase())))
                    break;
            if(lastname.length()>0)
                if(!teacher.getDisplayName().toLowerCase().contains(lastname.toLowerCase())
                        || teacher.getLastName().toLowerCase().contains(lastname.toLowerCase()))
                    break;
            if(subject.length()>0) {
                boolean found = false;
                for(Subject sub_in_list: teacher.getSubjectTaught()){
                    if(sub_in_list.getSubjectName().toLowerCase().contains(subject.toLowerCase()))
                        found=true;
                }
                if(found==false)
                    break;
            }
            result_list.add(teacher);
        }

        return result_list;
    }

    public static void exportCSV(List<Teacher> list, String path) throws IOException {
        String SEP = ",";

        if(list.isEmpty())
            return;

        Set<String> heads = list.get(0).getFields().keySet();
        String s = "";
        for(String head : heads){
            s += head+SEP;
        }
        s+="\n";

        for(Teacher teacher : list){
            for(String head : heads){
                s += teacher.getFields().get(head).toString().replace(SEP, "-")+SEP;
            }
            s+="\n";
        }

        Files.writeString(Path.of(path), s, StandardCharsets.UTF_8);
    }

    public static List<Group> createTeamsTeacherClassSubject(Map<Teacher, Map<Group, List<Subject>>> abbinamenti){
        List<Group> created_teams = new ArrayList<>();

        for(Teacher teacher: abbinamenti.keySet()){
            System.out.println("Creazione team docente: " + teacher.getDisplayName());
            Map<Group,List<Subject>> teacher_abbs = abbinamenti.get(teacher);
            for(Group classe : teacher_abbs.keySet()){
                System.out.println("Creazione team per classe "+classe.getGroup_name());
                List<Subject> subjects = teacher_abbs.get(classe);
                for(Subject subject: subjects){
                    System.out.println("Materia " + subject);
                    Group team= new Group();
                    String team_name = classe.getGroup_name() + " - " + subject.getSubjectName() + " - prof." + teacher.getDisplayName();
                    String team_id = PSGroup.CreateTeam(classe.getUsers(),team_name,"Team classe", teacher);
                    System.out.println(team_id);
                    team.setGroup_name(team_name);
                    for(User u:classe.getUsers()){
                        team.getUsers().add(u);
                    }
                    team.getUsers().add(teacher);
                    team.setObjectId(team_id);
                    created_teams.add(team);
                }
            }
        }

        return created_teams;
    }

    public static void main(String[] args) throws IOException {
        PSUsers ps = new PSUsers();

        List<Teacher> list = ps.getTeacherList();
        for(Teacher s : list){
            System.out.println(s);
        }

        List<Group> lg = PSGroup.getGroupList();
        Scanner sc = new Scanner(System.in);

        /*while (true){
            String name;
            String lastname;
            String materia;

            System.out.print("name : ");
            name = sc.nextLine();
            System.out.print("lastname : ");
            lastname = sc.nextLine();
            System.out.print("materia : ");
            materia = sc.nextLine();

            List<Teacher> list_ric = searchTeacher(list, name,lastname,materia);
            for(Teacher s : list_ric){
                System.out.println(s);
            }

            exportCSV(list_ric, "export.csv");
        }*/

        List<Student> students = PSUsers.getStudentList();

        System.out.println("Numero studenti: " + students.size());
        System.out.println("Numero gruppi: " + lg.size());

        Group g = lg.get(0);
        g.setUsers(new ArrayList<>());
        for(Student s: students){

            System.out.println(g.getGroup_name());
            g.getUsers().add(s);
        }

        Map<Teacher, Map<Group, List<Subject>>> abbinamenti = new HashMap<>();
        for(Teacher t : list){
            Map<Group,List<Subject>> map_teach = new HashMap<>();
            map_teach.put(lg.get(0), t.getSubjectTaught());
            abbinamenti.put(t,map_teach);
        }

        createTeamsTeacherClassSubject(abbinamenti);




    }


}
