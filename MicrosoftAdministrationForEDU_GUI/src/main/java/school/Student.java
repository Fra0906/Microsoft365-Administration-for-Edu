package school;

public class Student extends User {

    public Student(String name, String lastname, String user_principal_name, String display_name){
        super("Studente", name, lastname, user_principal_name, display_name);
    }

    private Student(){super("Studente");}

    public static Student fromExisting(){
        Student s = new Student();
        s.stopSyncronizing();
        return s;
    }

    public String getSchool(){
        return getDepartment();
    }

    public void setSchool(String school){
        setDepartment(school);
    }

    public String getClassroom(){
        return getOffice();
    }

    public void setClassroom(String classe){
        setOffice(classe);
    }

    @Override
    public String toString() {
        return getDisplayName() + " - " + getTitle() + " - " + getDepartment() + " - " + getOffice();
    }
    }



