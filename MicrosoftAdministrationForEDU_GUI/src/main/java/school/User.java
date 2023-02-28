package school;

import org.json.JSONObject;
import powershell.PSUsers;

import java.util.HashMap;

public abstract class User extends MicrosoftObject {

    private HashMap<String,Object> fields  = new HashMap<>();

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

    protected User(String title){
        fields.put("Title",title);
    }

    public User(String title, String name, String lastname, String user_principal_name, String display_name){

        if(display_name.length()==0){
            display_name = name + " " + lastname;
        }
        this.fields.put("Title", title);
        this.fields.put("DisplayName", display_name);
        this.fields.put("FirstName", name);
        this.fields.put("LastName", lastname);
        this.fields.put("UserPrincipalName",user_principal_name);

        JSONObject json_reponse = PSUsers.AddUser(this);
        this.fields.put("JsonResponse", json_reponse);
        //this.setObjectId(object_id);
    }

    public JSONObject getJsonResponse(){
        if(fields.containsKey("JsonResponse")){
            return (JSONObject) fields.get("JsonResponse");
        }
        return null;
    }

    public String getUserPrincipalName() {
        return (String) fields.get("UserPrincipalName");

    }

    /**
     * Attenzione: non aggiorna online
     */
   public void setUserPrincipalName(String user_principal_name) {
        if(!(user_principal_name.length() ==0)){
            fields.put("UserPrincipalName", user_principal_name);
        }
        //this.user_principal_name = user_principal_name;
    }

    public String getAlternateEmailAddress() {
        return (String) fields.get("AlternateEmailAddress");
        //return alternate_email_address;
    }

    public void setAlternateEmailAddress(String alternate_email_address) {
        if(!(alternate_email_address.length()==0)){
            fields.put("AlternateEmailAddress", alternate_email_address);
            if(isSync()){
                //TODO PSUSER SETALTERNATEADDRESS
            }
        }

        //this.alternate_email_address = alternate_email_address;
    }

    public boolean isBlockCredential() {
       if(fields.containsKey("IsBlockCredential")){
           return (boolean) fields.get("IsBlockCredential");
       } else{
           return false;
       }

        //return block_credential;
    }

    public void setBlockCredential(boolean block_credential) {
        fields.put("IsBlockCredential", block_credential);
        if(isSync()){
            PSUsers.BlockAccount(getObjectId(), block_credential);
        }
        //this.block_credential = block_credential;
    }

    public String getDepartment() {
        return (String) fields.get("Department");
        //return department;
    }

    public void setDepartment(String department) {
        if(!(department.length()==0)){
            fields.put("Department", department);
            if(isSync()){
                PSUsers.setDepartment(this);
            }
        }

        //this.department = department;
    }

    public String getDisplayName() {
        return (String) fields.get("DisplayName");
        //return display_name;
    }

    public void setDisplayName(String display_name) {
        if(!(display_name.length()==0)){
            fields.put("DisplayName", display_name);
            if(isSync()){
                PSUsers.setDisplayName(this);
            }
        }

        //this.display_name = display_name;
    }

    public String getFirstName() {
        return (String) fields.get("FirstName");
        //return first_name;
    }

    public void setFirstName(String first_name) {
        fields.put("FirstName", first_name);
        if(isSync()){
            PSUsers.setFirstName(this);
        }
        //this.first_name = first_name;
    }

    public String getLastName() {
        return (String) fields.get("LastName");
        //return last_name;
    }

    public void setLastName(String last_name) {
        fields.put("LastName", last_name);
        if(isSync()){
            PSUsers.setLastName(this);
        }
        //this.last_name = last_name;
    }



    public String getOffice() {
        return (String) fields.get("Office");
        //return office;
    }

    public void setOffice(String office) {
        if(!(office.length()==0)){
            fields.put("Office", office);
            if(isSync()){
                PSUsers.setOffice(this);
            }
        }

        //this.office = office;
    }

    public String getLicense() {
        return (String) fields.get("License");
        //return office;
    }

    public void setLicense(String license) {
        if(license!=null && !(license.length()==0)){
            fields.put("License", license);
            if(isSync()){
                PSUsers.addLincenses(this);
            }
        }

    }

    public void removeLicense(){
       if(fields.get("License")!=null){
           String lic = (String) fields.get("License");
           if(lic.length()>0){
               if(isSync()){
                   PSUsers.removeLicenses(this);
               }

               fields.remove("License");
           }
       }
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
