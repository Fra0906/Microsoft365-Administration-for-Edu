package school;

public class Student extends school.User {

    public Student(){
        super("Studente");
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

    public void setTitle(String title) { }

    @Override
    public String toString() {
        return getDisplayName() + " - " + getTitle() + " - " + getDepartment() + " - " + getOffice();
    }
    }



