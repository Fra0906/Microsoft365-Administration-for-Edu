package school;

public class Subject {


    private String subject_name;

    public String getSubjectName() {
        return subject_name;
    }

    public void setSubjectName(String subject_name) {
        this.subject_name = subject_name;
    }

    public Subject(String s){
        subject_name=s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        return subject_name != null ? subject_name.equals(subject.subject_name) : subject.subject_name == null;
    }

    @Override
    public int hashCode() {
        return subject_name != null ? subject_name.hashCode() : 0;
    }
}
