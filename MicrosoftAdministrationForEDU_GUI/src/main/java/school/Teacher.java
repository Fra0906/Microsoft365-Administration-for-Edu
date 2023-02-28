package school;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Teacher extends User {

    public Teacher(String name, String lastname, String user_principal_name, String display_name){
        super("Teacher", name, lastname, user_principal_name, display_name);
    }

    private Teacher(){super("Teacher");}

    public static Teacher fromExisting(){
        Teacher s = new Teacher();
        s.stopSyncronizing();
        return s;
    }

    @Override
    public String toString() {
        return getDisplayName() + " - " + getTitle() + " - " + getDepartment();
    }

    public List<Subject> getSubjectTaught(){
        String subject_string = getDepartment();
        List<Subject> subjects = new ArrayList<>();
        if(subject_string==null){
            return subjects;
        }
        if(subject_string.length()>0){
            List<String> list = Arrays.asList(subject_string.toUpperCase().split(","));
            for(String sub_name : list){
                Subject s = new Subject(sub_name);
                //s.setSubjectName(sub_name);
                subjects.add(s);
            }

        }
        return subjects;
    }

    public void addSubject(Subject newSubject){
        List<Subject> list = getSubjectTaught();
        if(!list.contains(newSubject)){
            list.add(newSubject);
            setDepartment(getListSubjectAsString(list));
        }
    }

    private String  getListSubjectAsString(List<Subject> list){
        String s = "";
        for(Subject subject : list){
            s+=subject.getSubjectName()+",";
        }
        s = s.substring(0,s.length()-1);
        return s;
    }
}
