package school;

import java.util.HashMap;

public abstract class User extends MicrosoftObject {

    private HashMap<String,Object> fields;

    /*private String alternate_email_address;
    private boolean block_credential = false;
    private String department; //Department
    private String display_name;
    private String first_name;
    private String last_name;
    private String user_principal_name;
    //private List<License> license_list;
    private String office; //Office
    private String title; //Title*/

    public User(String title){
        fields = new HashMap<>();
        this.fields.put("Title", title);
    }

    public String getUserPrincipalName() {
        return (String) fields.get("UserPrincipalName");

    }

    public void setUserPrincipalName(String user_principal_name) {
        fields.put("UserPrincipalName", user_principal_name);
        //this.user_principal_name = user_principal_name;
    }

    public String getAlternateEmailAddress() {
        return (String) fields.get("AlternateEmailAddress");
        //return alternate_email_address;
    }

    public void setAlternateEmailAddress(String alternate_email_address) {
        fields.put("AlternateEmailAddress", alternate_email_address);
        //this.alternate_email_address = alternate_email_address;
    }

    public boolean isBlockCredential() {
        return (boolean) fields.get("IsBlockCredential");
        //return block_credential;
    }

    public void setBlockCredential(boolean block_credential) {
        fields.put("IsBlockCredential", block_credential);
        //this.block_credential = block_credential;
    }

    public String getDepartment() {
        return (String) fields.get("Department");
        //return department;
    }

    public void setDepartment(String department) {
        fields.put("Department", department);
        //this.department = department;
    }

    public String getDisplayName() {
        return (String) fields.get("DisplayName");
        //return display_name;
    }

    public void setDisplayName(String display_name) {
        fields.put("DisplayName", display_name);
        //this.display_name = display_name;
    }

    public String getFirstName() {
        return (String) fields.get("FirstName");
        //return first_name;
    }

    public void setFirstName(String first_name) {
        fields.put("FirstName", first_name);
        //this.first_name = first_name;
    }

    public String getLastName() {
        return (String) fields.get("LastName");
        //return last_name;
    }

    public void setLastName(String last_name) {
        fields.put("LastName", last_name);
        //this.last_name = last_name;
    }



    public String getOffice() {
        return (String) fields.get("Office");
        //return office;
    }

    public void setOffice(String office) {
        fields.put("Office", office);
        //this.office = office;
    }

    public String getTitle() {
        return (String) fields.get("Title");
        //return title;
    }

    //PasswordResetNotRequiredDuringActivate

    public HashMap<String, Object> getFields(){
        return fields;
    }

    @Override
    public String toString() {
        String s = "";
        for (String key : fields.keySet()){
            s+=fields.get(key) + ",";
        }
        return s;
    }
}
