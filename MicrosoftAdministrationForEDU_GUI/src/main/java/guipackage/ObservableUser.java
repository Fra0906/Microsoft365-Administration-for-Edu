package guipackage;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import school.User;

public abstract class ObservableUser {

    private final SimpleStringProperty objectid;
    public final SimpleStringProperty name;
    private final SimpleStringProperty surname;
    private final SimpleStringProperty email;

    private final SimpleStringProperty title;
    private final SimpleStringProperty displayname;
    private final SimpleBooleanProperty blockcredential;

    private final SimpleBooleanProperty cheked = new SimpleBooleanProperty(false);

    private User user = null;

    public boolean isCheked() {
        return cheked.get();
    }

    public SimpleBooleanProperty chekedProperty() {
        return cheked;
    }

    public void setCheked(boolean cheked) {
        this.cheked.set(cheked);
    }

    ObservableUser(User user){
        this.user = user;
        this.name = new SimpleStringProperty(user.getFirstName());
        this.objectid=new SimpleStringProperty(user.getObjectId());
        this.surname = new SimpleStringProperty(user.getLastName());
        this.email = new SimpleStringProperty(user.getUserPrincipalName());
        this.title = new SimpleStringProperty(user.getTitle());
        this.displayname=new SimpleStringProperty(user.getDisplayName());

        this.blockcredential=new SimpleBooleanProperty(user.isBlockCredential());

    }

    public ObservableUser(String name, String surname,  boolean blockcredential, String displayname, String email, String title) {
        this.objectid = new SimpleStringProperty();
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.email = new SimpleStringProperty(email);
        this.blockcredential = new SimpleBooleanProperty(blockcredential);
        this.displayname = new SimpleStringProperty(displayname);
        this.title=new SimpleStringProperty(title);
    }

    public String getObjectId()
    {
        return objectid.get();
    }

    public boolean isBlockCredential(){return blockcredential.get();}

    public String getName(){
        return name.get();
    }

    public String getSurname(){
        return surname.get();
    }

    public String getEmail(){
        return email.get();
    }

    public String getTitle(){
        return title.get();
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDisplayname() {
        return displayname.get();
    }

    @Override
    public String toString() {
        return "Nome - " + name.get();
    }
}
