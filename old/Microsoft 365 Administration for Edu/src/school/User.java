package school;

public abstract class User extends MicrosoftObject {

    private String alternate_email_address;
    private boolean block_credential = false;
    private boolean school; //Department
    private String display_name;
    private String first_name;
    private String last_name;

    //private List<License> license_list;
    private String classroom; //Office
    private String ruolo; //Title

    public User(String ruolo){
        this.ruolo=ruolo;
    }

    public String getAlternate_email_address() {
        return alternate_email_address;
    }

    public void setAlternate_email_address(String alternate_email_address) {
        this.alternate_email_address = alternate_email_address;
    }

    public boolean isBlock_credential() {
        return block_credential;
    }

    public void setBlock_credential(boolean block_credential) {
        this.block_credential = block_credential;
    }

    public boolean isSchool() {
        return school;
    }

    public void setSchool(boolean school) {
        this.school = school;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }



    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getRuolo() {
        return ruolo;
    }

    //PasswordResetNotRequiredDuringActivate

}
