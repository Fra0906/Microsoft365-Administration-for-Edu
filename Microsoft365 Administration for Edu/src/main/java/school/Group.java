package school;

import java.util.ArrayList;
import java.util.List;

public class Group extends MicrosoftObject{


    private String group_name; //DisplayName
    private String description; //Description
    private String email_address; //EmailAddress
    private String group_type; //GroupType

    private List<User> members = new ArrayList<>();
    private List<Teacher> owners = new ArrayList<>();

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getGroup_type() {
        return group_type;
    }

    public void setGroup_type(String group_type) {
        this.group_type = group_type;
    }

    public List<User> getUsers() {
        return members;
    }

    public void setUsers(List<User> users) {
        this.members = users;
    }

    public List<Teacher> getOwners() {
        return owners;
    }

    public void setOwners(List<Teacher> owners) {
        this.owners = owners;
    }
}
