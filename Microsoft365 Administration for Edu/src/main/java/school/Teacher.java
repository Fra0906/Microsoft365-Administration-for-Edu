package school;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Teacher extends User {

    public Teacher(){
        super("Teacher");
    }

    @Override
    public String toString() {
        return getDisplayName() + " - " + getTitle() + " - " + getDepartment();
    }

    public List<Subject> getSubjectTaught(){//////TODO: deve diventare lista di subject
        String subject_string = getDepartment();
        List<Subject> subjects = new ArrayList<>();
        if(subject_string.length()>0){
            List<String> list = Arrays.asList(subject_string.toUpperCase().split(","));
            for(String sub_name : list){
                Subject s = new Subject();
                s.setSubjectName(sub_name);
                subjects.add(s);
            }
            return subjects;
        } else{
            return null;
        }
    }
}
