package guipackage;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import school.ClassGroup;
import school.Group;
import school.License;
import school.Student;

public class ObservableStudent extends  ObservableUser{


    //private final String object_id;
    //private final SimpleStringProperty name;
    //private final SimpleStringProperty surname;
    //private final SimpleStringProperty email;
    private final SimpleStringProperty school;
    private final SimpleStringProperty classroom;
    //private final SimpleStringProperty displayname;
    //private final SimpleStringProperty title = new SimpleStringProperty("Studente");

    //private final SimpleBooleanProperty blockcredential;
    public ClassGroup classGroup = null;
    public License license = null;

    public ComboBox<Object> combobox_group_class = new ComboBox<>();


    public ComboBox<Object> combobox_licenses = new ComboBox<>();

    private ObservableList<Object> obs_class_group = FXCollections.observableArrayList();
    private ObservableList<Object> obs_licences = FXCollections.observableArrayList();


    public ComboBox<Object> getCombobox_licenses() {
        return combobox_licenses;
    }

    public void setCombobox_licenses(ComboBox<Object> combobox_licenses) {
        this.combobox_licenses = combobox_licenses;
    }

    public ComboBox<Object> getCombobox_group_class() {
        return combobox_group_class;
    }

    public void setCombobox_group_class(ComboBox<Object> combobox_group_class) {
        this.combobox_group_class = combobox_group_class;
    }

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
        classroom.set(classGroup.getClasse());
        school.set(classGroup.getScuola());
    }


    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
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

    public SimpleStringProperty status = new SimpleStringProperty("");

    public void prepareComboboxClassGroup(){
        combobox_group_class = new ComboBox<>();

        combobox_group_class.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(combobox_group_class.getSelectionModel().getSelectedItem()!=null){
                    Object obj = combobox_group_class.getSelectionModel().getSelectedItem();
                    if(obj instanceof ObservableClassGroup){
                        setClassGroup(((ObservableClassGroup) obj).getGroup());
                    } else{
                        setClassGroup(null);
                    }
                } else {
                    setClassGroup(null);
                }
            }
        });

        obs_class_group.add(new String("Nessun gruppo"));

        for (Group group : App.getGroupList()){
            if(ClassGroup.isClassGroup(group)){
                obs_class_group.add(new ObservableClassGroup(ClassGroup.fromGroup(group)));
            }
        }

        combobox_group_class.setItems(obs_class_group);

        for (Object obj : obs_class_group){
            if(obj instanceof ObservableClassGroup){
                ClassGroup group = ((ObservableClassGroup) obj).getGroup();
                if(group.getScuola().toLowerCase().equals(school.get().toLowerCase())&&group.getClasse().toLowerCase().equals(classroom.get().toLowerCase())){
                    combobox_group_class.getSelectionModel().select(obj);
                    setClassGroup(group);
                    break;
                }
            }
        }
    }


    public void prepareComboboxLicense(){
        combobox_licenses = new ComboBox<>();

        combobox_licenses.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(combobox_licenses.getSelectionModel().getSelectedItem()!=null){
                    Object obj = combobox_licenses.getSelectionModel().getSelectedItem();
                    if(obj instanceof NewStudent.ObservableLicence){
                        setLicense(((NewStudent.ObservableLicence) obj).getLicense());
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
                obs_licences.add(new NewStudent.ObservableLicence(l));
        }

        combobox_licenses.setItems(obs_licences);
        combobox_licenses.getSelectionModel().select(obs_licences.get(1));
        License new_l=((NewStudent.ObservableLicence) obs_licences.get(1)).getLicense();
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





    public ObservableStudent(String name, String surname, String email, String school, String classroom, boolean blockcredential, String displayname) {
        super(name, surname, blockcredential, displayname, email, "Studente");
        this.school = new SimpleStringProperty(school);
        this.classroom = new SimpleStringProperty(classroom);

    }

    ObservableStudent(Student student){
        super(student);
        this.school = new SimpleStringProperty(student.getSchool());
        this.classroom = new SimpleStringProperty(student.getClassroom());
    }




    public String getSchool(){
        return school.get();
    }

    public String getClassroom(){
        return classroom.get();
    }

    public class ObservableClassGroup{

        public ClassGroup group;

        public SimpleStringProperty nome;

        public ObservableClassGroup(ClassGroup group) {
            this.group = group;
            this.nome = new SimpleStringProperty(group.getClasse() + " (" + group.getScuola() + ")");
        }

        public ClassGroup getGroup() {
            return group;
        }

        public void setGroup(ClassGroup group) {
            this.group = group;
        }

        public String getNome() {
            return nome.get();
        }

        public SimpleStringProperty nomeProperty() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome.set(nome);
        }

        @Override
        public String toString() {
            return nome.get();
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObservableStudent that = (ObservableStudent) o;
        return getEmail().equals(that.getEmail());


    }

}