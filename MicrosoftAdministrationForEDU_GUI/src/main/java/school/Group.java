package school;

import guipackage.ObservableGroup;
import powershell.PSGroup;
import powershell.PSUsers;

import java.util.ArrayList;
import java.util.List;

import static guipackage.ObservableUtility.getUserListFromObservable;

public class Group extends MicrosoftObject{


    private String group_name=new String(); //DisplayName
    private String description=new String(); //Description
    private String email_address=new String(); //EmailAddress
    private String group_type=new String(); //GroupType

    private List<User> members = new ArrayList<>();
    private List<User> owners = new ArrayList<>();



    public Group(String group_name, String nickname, String description){
        this.group_name = group_name;
        this.email_address = nickname; //
        this.description = description;
        String object_id = PSGroup.CreateGroup(this);
        this.setObjectId(object_id);
    }

    public Group(){}


    public static Group fromExisting(){
        Group g = new Group();
        g.stopSyncronizing();
        return g;
    }

    public String getGroupName() {
        return group_name;
    }

    public void setGroupName(String group_name) {
        this.group_name = group_name;
        if(isSync()){
            PSGroup.changename(this, group_name);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        if(isSync()){
            PSGroup.changedescription(this, description);
        }

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

    public List<User> getMembers() {
        return members;
    }

    public List<User> getOwners(){
        return owners;
    }

    public void setMembers(List<User> users) {
        this.members = users;
    }

    public void setOwners(List<User> users) {
        this.owners = users;
    }

    public void addMember(User user){
        if(!members.contains(user)){
            members.add(user);
            if(isSync()){
                PSGroup.addMember(this,user);
            }
        }
    }

    public void addOwner(User user){
        if(!owners.contains(user)){
            owners.add(user);
            if(isSync()){
                PSGroup.addOwner(this,user);
            }
        }
    }

    public void removeMember(User user)
    {
        if(members.contains(user)){
            members.remove(user);
            if(isSync()){
                PSGroup.remove_member(this.getObjectId(),user);
            }
        }
    }



    public void removeOwner(User user)
    {
        if(owners.contains(user)){
            owners.remove(user);
            if(isSync()){
                PSGroup.remove_owner(this.getObjectId(),user);
            }
        }
    }

    public String toString()
    {
        return this.getGroupName()+" - "+this.getEmail_address()+" - "+this.group_type;
    }

    public void assigned(ObservableGroup objectId)
    {
        this.group_type=objectId.getGroup_type();
        this.group_name=objectId.getGroup_name();
        this.setObjectId(objectId.getObjectId());
        this.email_address=objectId.getEmail_address();
        this.description=objectId.getDescription();
        this.members=getUserListFromObservable(this.getMembers(), objectId.getMembers());
        this.owners=getUserListFromObservable(this.getOwners(), objectId.getOwners());
        System.out.print((members.size()));
    }
}
