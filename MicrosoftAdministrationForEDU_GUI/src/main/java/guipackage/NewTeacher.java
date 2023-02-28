package guipackage;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import powershell.PS;
import school.ClassGroup;
import school.License;
import school.Teacher;
import school.User;
import utility.RandomString;
import utility.UtilityDocenti;
import utility.UtilityUser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewTeacher {

    @FXML
    private Button save_btn;
    @FXML private Button save_new_btn;
    @FXML private Button cancel_btn;


    @FXML private TextField name;
    @FXML private TextField surname;
    @FXML private TextField email;
    @FXML private TextField subject;


    private boolean editmode = false;

    @FXML public ComboBox domain_combobox;
    @FXML public ComboBox licenza_combobox;
    @FXML public Button reset_pwd_btn;

    private Teacher new_teacher = null;

    private ObservableTeacher teacher;
    private ObservableLicence old_licence;
    private ObservableList<Object> licences = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
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

        licences.add(new String("Nessuna licenza"));

        for (License lic : App.getLicencesList()){
            licences.add(new ObservableLicence(lic));
        }

        licenza_combobox.setItems(licences);
    }

    public void resetPassword(){
        Alert alert = GUIUtility.showAlert("Reset password","Vuoi davvero resettare la password per l'utente "+teacher.getEmail()+"?" , Alert.AlertType.INFORMATION, ButtonType.YES, ButtonType.NO);
        if(alert.getResult()==ButtonType.YES){
            String new_pwd = RandomString.getAlphaNumericString("Mmm00000");
            UtilityUser.changeUserPassword(teacher.getEmail(),new_pwd);
            GUIUtility.showAlert("Password resettata","La nuova password per " + teacher.getEmail() + " è " + new_pwd , Alert.AlertType.CONFIRMATION, ButtonType.OK);
        }
    }

    public void onChangeNameSurname(){
        if(!editmode) {
            if (name.getText() != null && surname.getText() != null && name.getText().length() > 0 && surname.getText().length() > 0) {
                email.setText((name.getText() + "." + surname.getText()).toLowerCase().replace(" ", "").replace("'", ""));
            }
        }
    }



    private static boolean checkSubjectsSyntax(String subject) //TODO Farlo funzionare
    {
        Pattern p=Pattern.compile("^([A-Z]+[,]?){1}", Pattern.CASE_INSENSITIVE);
        Matcher m=p.matcher(subject);
        if(m.find())
        {
            //System.out.println("sono entrato");
            return true;
        }
        return false;
    }
    public boolean addTeacher(){
        if(email.getText()==null || domain_combobox.getSelectionModel().getSelectedItem()==null || !UtilityUser.checkEmailSyntax(email.getText()+domain_combobox.getSelectionModel().getSelectedItem().toString())) {
            Alert alert = GUIUtility.showAlert("Errore","Email non valida (controllare anche il dominio)", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }
        if(UtilityDocenti.teacherExists(App.getTeacherList(),email.getText()+domain_combobox.getSelectionModel().getSelectedItem().toString())) { //TODO aggiungere controllo su lista docenti
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

        new_teacher = new Teacher(
                name.getText(),
                surname.getText(),
                email.getText()+domain_combobox.getSelectionModel().getSelectedItem().toString(),
                name.getText() + " " + surname.getText());

        if(subject.getText()!=null && checkSubjectsSyntax(subject.getText())){
            new_teacher.setDepartment(subject.getText());
        }
        /*else
        {
            Alert alert = GUIUtility.showAlert("Errore","Inserire le materie separate da virgole", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }*/

        if(licenza_combobox.getSelectionModel().getSelectedItem()!=null){

            if(licenza_combobox.getSelectionModel().getSelectedItem() instanceof ObservableLicence){

                new_teacher.setLicense(((ObservableLicence) licenza_combobox.getSelectionModel().getSelectedItem()).getNome());

            }
        }



        new_teacher.setBlockCredential(false);
        App.getTeacherList().add(new_teacher);
        //ObservableUtility.refreshStudents();
        TeacherlistController.getTeachersObservableList().clear();
        ObservableUtility.buildObservableTeacherList(App.getTeacherList()); //messo io
        return true;

    }


    public boolean saveExistent(){
        Teacher teacher = UtilityDocenti.findTeacher(App.getTeacherList(),email.getText()+domain_combobox.getSelectionModel().getSelectedItem().toString());

        //System.out.println(teacher);
        teacher.startSyncronizing();

        if(name.getText().length()<3){
            Alert alert = GUIUtility.showAlert("Errore","Inserire un nome valido", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }

        if(surname.getText().length()<3){
            Alert alert = GUIUtility.showAlert("Errore","Inserire un cognome valido", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }

        if(!teacher.getFirstName().equals(name.getText())){
            teacher.setFirstName(name.getText());
            teacher.setDisplayName(name.getText() + " " + teacher.getLastName());
        }
        if(!teacher.getLastName().equals(surname.getText())){
            teacher.setLastName(surname.getText());
            teacher.setDisplayName(teacher.getLastName() + " " + surname.getText());
        }
        teacher.setDepartment((subject.getText()==null || subject.getText().isEmpty())?"":subject.getText().toUpperCase());

        if(licenza_combobox.getSelectionModel().getSelectedItem()!=old_licence){
            if(old_licence!=null){
                teacher.removeLicense();
                old_licence = null;
            }
            if(licenza_combobox.getSelectionModel().getSelectedItem()!=null){

                if(licenza_combobox.getSelectionModel().getSelectedItem() instanceof ObservableLicence){

                    teacher.setLicense(((ObservableLicence) licenza_combobox.getSelectionModel().getSelectedItem()).getNome());
                    old_licence = (ObservableLicence) licenza_combobox.getSelectionModel().getSelectedItem();
                }
            }
        }


        ObservableUtility.refreshTeachers();
        return true;
    }

    public boolean save(ActionEvent event){
        //((Node)(event.getSource())).getScene().getWindow().hide();
        if(editmode){
            if(saveExistent()){
                Alert alert = GUIUtility.showAlert("Conferma", "Utente " + email.getText()+domain_combobox.getSelectionModel().getSelectedItem().toString() + " modificato correttamente" , Alert.AlertType.CONFIRMATION, ButtonType.OK);
                return true;

            }
            else {
                return false;
            }
        } else{
            if(addTeacher()){
                String pwd =(new_teacher.getJsonResponse()==null)?"":(String) new_teacher.getJsonResponse().get("Password");
                Alert alert;
                if(pwd.length()>0){
                    alert = GUIUtility.showAlert("Conferma", "Utente inserito correttamente\nLa password e': " + pwd , Alert.AlertType.CONFIRMATION, ButtonType.OK);
                } else {
                    alert = GUIUtility.showAlert("Conferma", "Utente inserito correttamente" , Alert.AlertType.CONFIRMATION, ButtonType.OK);
                }

                name.setText("");
                surname.setText("");
                email.setText("");
                subject.setText("");
                domain_combobox.getSelectionModel().clearSelection();
                licenza_combobox.getSelectionModel().clearSelection();
                return true;
            }
            else {
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

    public void setSubject(String subject) {this.subject.setText(subject);}

    public static void main(String[] args) throws IOException {
        PS.connect("SaraCa@01hc5.onmicrosoft.com","bitter123_");
        App.setDomainList(PS.getDomains());
        System.out.println(App.getDomainList());
        String m = "ciao@saraca.it";
        System.out.println(UtilityUser.checkEmailSyntax(m));
    }

    public void setEditMode(boolean b) {
        email.setDisable(b);
        editmode=b;
        reset_pwd_btn.setVisible(b);
        domain_combobox.setDisable(b);
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

    public void setTeacher(ObservableTeacher teacher)
    {
        this.teacher = teacher;
        setName(teacher.getName());
        setSurname(teacher.getSurname());
        setEmail(teacher.getEmail());
        setSubject(teacher.getSubjects());


        if(teacher.getUser()!=null){

            User user = teacher.getUser();
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


        ObservableLicence(License lic){
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




}
