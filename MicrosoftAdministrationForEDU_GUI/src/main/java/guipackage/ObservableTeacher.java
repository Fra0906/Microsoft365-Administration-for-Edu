package guipackage;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import school.License;
import school.Teacher;

public class ObservableTeacher  extends ObservableUser{
    // private final SimpleStringProperty name;
    // private final SimpleStringProperty surname;
    // private final SimpleStringProperty email;
    private final SimpleStringProperty subjects;
    public SimpleStringProperty status = new SimpleStringProperty("");

    public License license = null;

    public ComboBox<Object> combobox_group_class = new ComboBox<>();


    public ComboBox<Object> combobox_licenses = new ComboBox<>();
    private ObservableList<Object> obs_licences = FXCollections.observableArrayList();
    // private final SimpleBooleanProperty blockcredential;

    public ComboBox<Object> getCombobox_licenses() {
        return combobox_licenses;
    }

    public void setCombobox_licenses(ComboBox<Object> combobox_licenses) {
        this.combobox_licenses = combobox_licenses;
    }

    ObservableTeacher(Teacher teacher){
        super(teacher);
        this.subjects = new SimpleStringProperty(teacher.getDepartment());
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }
    public void setStatus(String status) {
        this.status.set(status);
    }
    ObservableTeacher(String name, String surname,  boolean blockcredential, String displayname, String email, String subjects) {
        super(name, surname, blockcredential, displayname, email, "Docente");
        this.subjects=new SimpleStringProperty(subjects);
    }
    /* public boolean isBlockCredential(){return blockcredential.get();}*/

   /* public String getName(){
        return name.get();
    }*/

   /* public String getSurname(){
        return surname.get();
    }*/

   /* public String getEmail(){
        return email.get();
    }*/

    public String getSubjects(){return subjects.get();}

    public void prepareComboboxLicense(){
        combobox_licenses = new ComboBox<>();

        combobox_licenses.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(combobox_licenses.getSelectionModel().getSelectedItem()!=null){
                    Object obj = combobox_licenses.getSelectionModel().getSelectedItem();
                    if(obj instanceof NewTeacher.ObservableLicence){
                        setLicense(((NewTeacher.ObservableLicence) obj).getLicense());
                    } else{
                        setLicense(null);
                    }
                } else {
                    setLicense(null);
                }
            }
        });

        obs_licences.add(new String("Nessuna Licenza"));

        for (License l: App.getLicencesList()){
            obs_licences.add(new NewTeacher.ObservableLicence(l));
        }

        combobox_licenses.setItems(obs_licences);
        combobox_licenses.getSelectionModel().select(obs_licences.get(1));
        License new_l=((NewTeacher.ObservableLicence) obs_licences.get(1)).getLicense();
        setLicense(new_l);

        /*for (Object obj : obs_licences){
            if(obj instanceof ObservableClassGroup){
                ClassGroup group = ((ObservableClassGroup) obj).getGroup();
                if(group.getScuola().toLowerCase().equals(school.get().toLowerCase())&&group.getClasse().toLowerCase().equals(classroom.get().toLowerCase())){
                    combobox_group_class.getSelectionModel().select(obj);
                    setClassGroup(group);
                    break;
                }
            }
        }*/
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if(!(o instanceof ObservableUser)) return false;

        ObservableUser that = (ObservableUser) o;

        boolean cond = true;
        if(that.getObjectId()!=null && this.getObjectId()!=null){
            cond = that.getObjectId().equals(this.getObjectId()!=null);
        }
        cond = that.getEmail().equals(this.getEmail()) && cond;
        //System.out.println(that.getEmail() + " equals? " + this.getEmail()+ " = "+cond );

        return cond;
    }

    @Override
    public int hashCode() {
        int result = subjects != null ? subjects.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (license != null ? license.hashCode() : 0);
        result = 31 * result + (combobox_group_class != null ? combobox_group_class.hashCode() : 0);
        result = 31 * result + (combobox_licenses != null ? combobox_licenses.hashCode() : 0);
        result = 31 * result + (obs_licences != null ? obs_licences.hashCode() : 0);
        return result;
    }
}