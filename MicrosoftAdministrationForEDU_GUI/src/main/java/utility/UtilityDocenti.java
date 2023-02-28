package utility;

import powershell.PS;
import powershell.PSGroup;
import powershell.PSUsers;
import school.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class UtilityDocenti {

    public static List<Teacher> searchTeacher(List<Teacher> all_teacher, String name, String lastname, String subject, boolean regex_nome, boolean regex_cognome, boolean regex_materie){
        List<Teacher> result_list = new ArrayList<>();


        for(Teacher teacher : all_teacher){
            if(name.length()>0 && teacher.getFirstName()!=null) {
                try{
                    if(regex_nome) {
                        Matcher matcher = Pattern.compile(name.toLowerCase()).matcher(teacher.getFirstName().toLowerCase());
                        if (!matcher.matches()) {
                            continue;
                        }
                    }
                    else {
                        if (!(teacher.getFirstName().toLowerCase().contains(name.toLowerCase())))
                            continue;
                    }

                } catch (PatternSyntaxException e){
                    if (!(teacher.getFirstName().toLowerCase().contains(name.toLowerCase())))
                        continue;
                }
            }

            if(lastname.length()>0 && teacher.getLastName()!=null) {
                try{
                    if(regex_cognome) {
                        Matcher matcher = Pattern.compile(lastname.toLowerCase()).matcher(teacher.getLastName().toLowerCase());
                        if (!matcher.matches()) {
                            continue;
                        }
                    }
                    else {
                        if (!(teacher.getLastName().toLowerCase().contains(lastname.toLowerCase())))
                            continue;
                    }

                } catch (PatternSyntaxException e){
                    if (!(teacher.getLastName().toLowerCase().contains(lastname.toLowerCase())))
                        continue;
                }
            }

            if(subject.length()>0 && teacher.getDepartment()!=null) {
                try{
                    if(regex_materie) {
                        Matcher matcher = Pattern.compile(subject.toLowerCase()).matcher(teacher.getDepartment().toLowerCase());
                        if (!matcher.matches()) {
                            continue;
                        }
                    }
                    else {
                        if (!(teacher.getDepartment().toLowerCase().contains(subject.toLowerCase())))
                            continue;
                    }

                } catch (PatternSyntaxException e){
                    if (!(teacher.getDepartment().toLowerCase().contains(subject.toLowerCase())))
                        continue;
                }
            }

            result_list.add(teacher);
        }
        return result_list;

    }

    public static boolean teacherExists(List<Teacher> list,String user_principal_name){
        for(Teacher s:list){
            if(s.getUserPrincipalName().equals(user_principal_name)){
                return true;
            }
        }
        return false;
    }

    public static Teacher findTeacher(List<Teacher> list, String user_principal_name){
        for(Teacher t:list){
            if(t.getUserPrincipalName().equals(user_principal_name)){
                return t;
            }
        }
        return null;
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
                s += "\"" + teacher.getFields().get(head).toString().replace(SEP, "-")+"\""+SEP;
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
                System.out.println("Creazione team per classe "+classe.getGroupName());
                List<Subject> subjects = teacher_abbs.get(classe);
                for(Subject subject: subjects){
                    System.out.println("Materia " + subject);
                    String team_name = classe.getGroupName() + " - " + subject.getSubjectName() + " - prof." + teacher.getDisplayName();

                    Team team = new Team(team_name,"Team classe", teacher, "EDU_Class");
                    for(User user : classe.getMembers()){
                        team.addMember(user);
                    }


                    /**Group team= Group.fromExisting();

                    String team_id = PSGroup.CreateTeam(classe.getMembers(),team_name,"Team classe", teacher);
                    System.out.println(team_id);
                    team.setGroupName(team_name);
                    for(User u:classe.getMembers()){
                        team.getMembers().add(u);
                    }
                    team.getMembers().add(teacher);
                    team.setObjectId(team_id);*/
                    created_teams.add(team);
                }
            }
        }

        return created_teams;
    }

    public static void main(String[] args) throws IOException {
        PS.connect();

        List<Teacher> list = PSUsers.getTeacherList();
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
        g.setMembers(new ArrayList<>());
        for(Student s: students){

            //System.out.println(g.getGroupName());
            g.getMembers().add(s);
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
