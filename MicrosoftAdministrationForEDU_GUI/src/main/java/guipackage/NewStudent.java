package guipackage;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import school.*;
import utility.RandomString;
import utility.UtilityStudenti;
import utility.UtilityUser;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewStudent implements Initializable {

    @FXML private Button save_btn;
    @FXML private Button save_new_btn;
    @FXML private Button cancel_btn;


    @FXML private TextField name;
    @FXML private TextField surname;
    @FXML private TextField email;
    @FXML private TextField school;
    @FXML private TextField classroom;

    @FXML public ComboBox domain_combobox;
    @FXML public ComboBox gruppoclasse_combobox;
    @FXML public ComboBox licenza_combobox;

    @FXML public Button reset_pwd_btn;

    private Student new_student = null;

    private boolean editmode = false;

    private ObservableStudent student;
    private ObservableClassGroup old_class_group;
    private ObservableLicence old_licence;

    private ObservableList<Object> classGroups = FXCollections.observableArrayList();
    private ObservableList<Object> licences = FXCollections.observableArrayList();

    public void resetPassword(){
        Alert alert = GUIUtility.showAlert("Reset password","Vuoi davvero resettare la password per l'utente "+student.getEmail()+"?" , Alert.AlertType.INFORMATION, ButtonType.YES, ButtonType.NO);
        if(alert.getResult()==ButtonType.YES){
            String new_pwd = RandomString.getAlphaNumericString("Mmm00000");
            UtilityUser.changeUserPassword(student.getEmail(),new_pwd);
            GUIUtility.showAlert("Password resettata","La nuova password per " + student.getEmail() + " è " + new_pwd , Alert.AlertType.CONFIRMATION, ButtonType.OK);

        }
    }


    public boolean addStudent(){
        if(email.getText()==null || domain_combobox.getSelectionModel().getSelectedItem()==null || !UtilityUser.checkEmailSyntax(email.getText()+domain_combobox.getSelectionModel().getSelectedItem().toString())) {
            Alert alert = GUIUtility.showAlert("Errore","Email non valida (controllare anche il dominio)", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }
        if(UtilityStudenti.studentExists(App.getStudentList(),email.getText()+domain_combobox.getSelectionModel().getSelectedItem().toString())) { //TODO aggiungere controllo su lista docenti
            Alert alert = GUIUtility.showAlert("Errore","Utente già esistente", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }

        if(name.getText()==null || name.getText().length()<3){
            Alert alert = GUIUtility.showAlert("Errore","Inserire un nome valido", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }

        if(surname.getText()==null || surname.getText().length()<3){
            Alert alert = GUIUtility.showAlert("Errore","Inserire un cognome valido", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }

        new_student = new Student(
                name.getText(),
                surname.getText(),
                email.getText()+domain_combobox.getSelectionModel().getSelectedItem().toString(),
                name.getText() + " " + surname.getText());

        if(classroom.getText()!=null)
            new_student.setClassroom(classroom.getText());

        if(school.getText()!=null)
            new_student.setSchool(school.getText());



        if(licenza_combobox.getSelectionModel().getSelectedItem()!=null){
            if(licenza_combobox.getSelectionModel().getSelectedItem() instanceof ObservableLicence){

                new_student.setLicense(((ObservableLicence) licenza_combobox.getSelectionModel().getSelectedItem()).getNome());

            }
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(gruppoclasse_combobox.getSelectionModel().getSelectedItem()!=null){
            if(gruppoclasse_combobox.getSelectionModel().getSelectedItem() instanceof ObservableClassGroup){
                ClassGroup g = ((ObservableClassGroup)  gruppoclasse_combobox.getSelectionModel().getSelectedItem()).getGroup();
                g.startSyncronizing();
                g.addMember(new_student);
            }
        }

        new_student.setBlockCredential(false);
        App.getStudentList().add(new_student);
        //ObservableUtility.refreshStudents();
        StudentlistController.getStudentsObservableList().clear();
        ObservableUtility.buildObservableStudentList(App.getStudentList()); //messo io
        return true;

    }

    public boolean saveExistent(){
        Student student = UtilityStudenti.findStudent(App.getStudentList(),email.getText()+domain_combobox.getSelectionModel().getSelectedItem().toString());

        //System.out.println(student);
        student.startSyncronizing();

        if(name.getText().length()<3){
            Alert alert = GUIUtility.showAlert("Errore","Inserire un nome valido", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }

        if(surname.getText().length()<3){
            Alert alert = GUIUtility.showAlert("Errore","Inserire un cognome valido", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }

        if(!student.getFirstName().equals(name.getText())){
            student.setFirstName(name.getText());
            student.setDisplayName(name.getText() + " " + student.getLastName());
        }
        if(!student.getLastName().equals(surname.getText())){
            student.setLastName(surname.getText());
            student.setDisplayName(student.getLastName() + " " + surname.getText());
        }


        student.setClassroom((classroom.getText()==null||classroom.getText().isEmpty())?" ":classroom.getText());
        student.setSchool((school.getText()==null||school.getText().isEmpty())?" ":school.getText());



        if(licenza_combobox.getSelectionModel().getSelectedItem()!=old_licence){
            if(old_licence!=null){
                student.startSyncronizing();
                student.removeLicense();
                old_licence = null;
            }
            if(licenza_combobox.getSelectionModel().getSelectedItem()!=null){
                if(licenza_combobox.getSelectionModel().getSelectedItem() instanceof ObservableLicence){

                    student.setLicense(((ObservableLicence) licenza_combobox.getSelectionModel().getSelectedItem()).getNome());
                    old_licence = (ObservableLicence) licenza_combobox.getSelectionModel().getSelectedItem();
                }
            }
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(gruppoclasse_combobox.getSelectionModel().getSelectedItem()!=old_class_group){
            if(old_class_group!=null) {
                old_class_group.getGroup().removeMember(student);
                old_class_group = null;
            }
            if(gruppoclasse_combobox.getSelectionModel().getSelectedItem()!=null){
                if(gruppoclasse_combobox.getSelectionModel().getSelectedItem() instanceof ObservableClassGroup){
                    ClassGroup group = ((ObservableClassGroup) gruppoclasse_combobox.getSelectionModel().getSelectedItem()).getGroup();
                    group.startSyncronizing();
                    group.addMember(student);
                    old_class_group = (ObservableClassGroup) gruppoclasse_combobox.getSelectionModel().getSelectedItem();
                }
            }
        }

        /*if(student.getClassroom()==null || !student.getClassroom().equals(classroom.getText())){

        }

        if(student.getSchool()==null || !student.getSchool().equals(school.getText())) {

        }*/

        ObservableUtility.refreshStudents();
        return true;
    }

    public void onChangeClassGroup(){
        Object selectedItem = gruppoclasse_combobox.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            if(selectedItem instanceof ObservableClassGroup){
                ClassGroup gr = ((ObservableClassGroup) selectedItem).getGroup();
                setClassroom(gr.getClasse());
                setSchool(gr.getScuola());
            } else {
                setClassroom("");
                setSchool("");
            }
        }
    }

    public void onChangeLicence(){
        Object selectedItem = licenza_combobox.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            if(selectedItem instanceof ObservableLicence){
                License license = ((ObservableLicence) selectedItem).getLicense();

            } else {

            }
        }
    }

    public void onChangeNameSurname(){
        if(!email.isDisabled()) {
            if (name.getText() != null && surname.getText() != null && name.getText().length() > 0 && surname.getText().length() > 0) {
                email.setText((name.getText() + "." + surname.getText()).toLowerCase().replace(" ","").replace("'",""));
            }
        }
    }

    public boolean save(ActionEvent event){
        //((Node)(event.getSource())).getScene().getWindow().hide();
        if(editmode){
            if(saveExistent()){
                Alert alert = GUIUtility.showAlert("Conferma", "Utente " + email.getText()+domain_combobox.getSelectionModel().getSelectedItem().toString() + " modificato correttamente" , Alert.AlertType.CONFIRMATION, ButtonType.OK);
                return true;
            } else {
                return false;
            }
        } else{
            if(addStudent()){
                String pwd =(new_student.getJsonResponse()==null)?"":(String) new_student.getJsonResponse().get("Password");
                Alert alert;
                if(pwd.length()>0){
                    alert = GUIUtility.showAlert("Conferma", "Utente inserito correttamente\nLa password e': " + pwd , Alert.AlertType.CONFIRMATION, ButtonType.OK);
                } else {
                    alert = GUIUtility.showAlert("Conferma", "Utente inserito correttamente" , Alert.AlertType.CONFIRMATION, ButtonType.OK);
                }
                name.setText("");
                surname.setText("");
                email.setText("");
                classroom.setText("");
                school.setText("");
                domain_combobox.getSelectionModel().clearSelection();
                gruppoclasse_combobox.getSelectionModel().clearSelection();
                licenza_combobox.getSelectionModel().clearSelection();
                return true;
            } else {
                return false;
            }
        }

    }

    public void save_and_close(ActionEvent event){
        if(save(event)){
            Stage stage = (Stage) save_new_btn.getScene().getWindow();
            stage.close();
        }
    }

    public void close_window()
    {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
    public void setName(String name) {
        this.name.setText(name);
    }

    public void setSurname(String surname) {
        this.surname.setText(surname);
    }

    public void setEmail(String email) {
        String[] valori = email.split("@");
        this.email.setText(valori[0]);
        for(Object d :  domain_combobox.getItems()){
            if(((String) d).equals("@"+valori[1])){
                domain_combobox.getSelectionModel().select(d);
                break;
            }
        }
    }

    public void setSchool(String school) {
        this.school.setText(school);
    }

    public void setClassroom(String classroom) {
        this.classroom.setText(classroom);
    }

    public void setStudent(ObservableStudent student) {
        //System.out.println("SET");
        this.student = student;
        setName(student.getName());
        setSurname(student.getSurname());
        setEmail(student.getEmail());
        setClassroom(student.getClassroom());
        setSchool(student.getSchool());

        for(Object obsClassGroup : classGroups){
            if(obsClassGroup instanceof ObservableClassGroup){
                ClassGroup group = ((ObservableClassGroup) obsClassGroup).getGroup();
                for(User user : group.getMembers()){
                    if(user.getObjectId().equals(student.getObjectId())){
                        gruppoclasse_combobox.getSelectionModel().select(obsClassGroup);
                        old_class_group = (ObservableClassGroup) obsClassGroup;
                        break;
                    }
                }
            }
        }

        if(student.getUser()!=null){

            User user = student.getUser();
            //System.out.println(user);
            String license = user.getLicense();
            //System.out.println(license);
            for(Object ol : licences){
                if(ol instanceof ObservableLicence){
                    ObservableLicence obslic = (ObservableLicence) ol;
                    if(obslic.equals(license)){
                        licenza_combobox.getSelectionModel().select(obslic);
                        old_licence = obslic;
                        break;
                    }
                }
            }

        }
    }

    public void setEditMode(boolean b) {
        email.setDisable(b);
        editmode=b;
        reset_pwd_btn.setVisible(b);
        domain_combobox.setDisable(b);
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

    public static class ObservableLicence {
        public SimpleStringProperty nome;

        public License getLicense() {
            return lic;
        }

        public void setLicense(License lic) {
            this.lic = lic;
        }

        public License lic;

        public String getNome() {
            return nome.get();
        }

        public SimpleStringProperty nomeProperty() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome.set(nome);
        }


        public ObservableLicence(License lic){
            this.lic=lic;
            this.nome = new SimpleStringProperty(lic.getNome());
        }

        @Override
        public String toString() {
            return nome.get();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if(o instanceof String){
                return this.getNome().equals((String) o);
            }
            if(o instanceof License){
                return lic.getNome().equals(((License)o).getNome());
            }
            if(o instanceof ObservableLicence){
                return this.getNome().equals(((ObservableLicence)o).getNome());
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = nome != null ? nome.hashCode() : 0;
            result = 31 * result + (lic != null ? lic.hashCode() : 0);
            return result;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //System.out.println("INITIALIZE");
        name.textProperty().addListener(((observableValue, s, t1) -> {
            onChangeNameSurname();
        }));

        surname.textProperty().addListener(((observableValue, s, t1) -> {
            onChangeNameSurname();
        }));



        //System.out.println(App.getDomainList());

        ObservableList<String> obs_domains = FXCollections.observableArrayList();
        for(String dom : App.getDomainList()){
            obs_domains.add("@"+dom);
        }
        domain_combobox.setItems(obs_domains);

        classGroups.add(new String("Nessun gruppo"));

        for (Group group : App.getGroupList()){
            if(ClassGroup.isClassGroup(group)){
                classGroups.add(new ObservableClassGroup(ClassGroup.fromGroup(group)));
            }
        }

        gruppoclasse_combobox.setItems(classGroups);

        licences.add(new String("Nessuna licenza"));

        for (License lic : App.getLicencesList()){
            licences.add(new ObservableLicence(lic));
        }

        licenza_combobox.setItems(licences);



    }
}
