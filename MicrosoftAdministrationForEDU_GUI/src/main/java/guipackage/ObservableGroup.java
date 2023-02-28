package guipackage;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import powershell.PSGroup;
import school.Group;


public class ObservableGroup {

    private final SimpleStringProperty group_name;

    private final SimpleStringProperty group_id;
    private final SimpleStringProperty description;
    private final SimpleStringProperty email_address;
    private final SimpleStringProperty group_type;

    private final SimpleListProperty<ObservableUser> members;
    private final SimpleListProperty<ObservableUser> owners;


    ObservableGroup(Group g){
        this.group_id=new SimpleStringProperty(g.getObjectId());
        this.group_name = new SimpleStringProperty(g.getGroupName());
        this.description = new SimpleStringProperty(g.getDescription());
        this.email_address = new SimpleStringProperty(g.getEmail_address());
        this.group_type = new SimpleStringProperty(g.getGroup_type());
        this.members=new SimpleListProperty<ObservableUser>(ObservableUtility.buildUserObservableList(g.getMembers()));
        this.owners=new SimpleListProperty<ObservableUser>(ObservableUtility.buildUserObservableList(g.getOwners()));
    }

    ObservableGroup()
    {
        this.group_id=new SimpleStringProperty("");
        this.group_name = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.email_address = new SimpleStringProperty("");
        this.group_type = new SimpleStringProperty("");
        this.members=new SimpleListProperty<ObservableUser>();
        this.owners=new SimpleListProperty<ObservableUser>();
    }


    public void remove_member(ObservableUser user)
    {
        if(this.getMembers().contains(user)){
            this.getMembers().remove(user);
        }
    }

    public void remove_owner(ObservableUser user)
    {
        if(this.getOwners().contains(user)){
            this.getOwners().remove(user);
        }
    }
    public String getGroupType(){return group_type.get();}
    public String getObjectId(){return group_id.get();}
    public String getGroup_name(){
        return group_name.get();
    }

    public String getDescription(){
        return description.get();
    }

    public String getEmail_address(){
        return email_address.get();
    }

    public String getGroup_type(){
        return group_type.get();
    }

    public void setGroup_name(String group_name) {
        this.group_name.set(group_name);
    }

    public void setGroup_id(String group_id) {
        this.group_id.set(group_id);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setEmail_address(String email_address) {
        this.email_address.set(email_address);
    }

    public void setGroup_type(String group_type) {
        this.group_type.set(group_type);
    }

    public void setMembers(ObservableList<ObservableUser> members) {
        this.members.set(members);
    }

    public void setOwners(ObservableList<ObservableUser> owners) {
        this.owners.set(owners);
    }

    public ObservableList<ObservableUser> getMembers(){
        return members.get();
    }

    public ObservableList<ObservableUser> getOwners(){
        return owners.get();
    }

    @Override
    public String toString() {
        return "OBID:"+this.getObjectId()+" - Nome:"+this.getGroup_name();
    }
}