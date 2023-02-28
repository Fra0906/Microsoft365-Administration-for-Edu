package guipackage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import school.Group;
import school.Student;
import school.Teacher;
import utility.UtilityDocenti;
import utility.UtilityGroup;
import utility.UtilityStudenti;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewGroup {

    private boolean editmode = false;
    @FXML
    private Button save_btn;
    @FXML
    private Button save_close_btn;
    @FXML
    private Button cancel_btn;

    @FXML
    private TextField group_name;
    @FXML
    private TextField group_description;
    @FXML
    private TextField group_email;

    @FXML
    private ComboBox domain_combobox;

    @FXML
    public void initialize() {

        group_name.textProperty().addListener(((observableValue, s, t1) -> {
            onChangeNameDescription();
        }));

        group_description.textProperty().addListener(((observableValue, s, t1) -> {
            onChangeNameDescription();
        }));

        ObservableList<String> obs_domains = FXCollections.observableArrayList();
        for(String dom : App.getDomainList()){
            obs_domains.add("@"+dom);
        }
        domain_combobox.setItems(obs_domains);
    }

    private void onChangeNameDescription()
    {
        if(group_name.getText()!=null && group_name.getText().length()>0){
            group_email.setText(group_name.getText().replace(" ", "").replace("'","").toLowerCase()+".group");
        }
    }

    private static boolean checkEmailSyntax(String email) {
        for (String domain : App.getDomainList()) {
            Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@" + domain, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(email);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    public boolean addGroup() {
        if (group_email.getText() == null || !checkEmailSyntax(group_email.getText() + domain_combobox.getSelectionModel().getSelectedItem().toString())) {
            Alert alert = GUIUtility.showAlert("Errore","Email non valida (controllare anche il dominio)", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }
        if (UtilityGroup.GroupExists(App.getGroupList(), group_email.getText() + domain_combobox.getSelectionModel().getSelectedItem().toString())) { //TODO aggiungere controllo su lista docenti
            Alert alert = GUIUtility.showAlert("Errore","Gruppo già esistente", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }

        if (group_name.getText() == null || group_name.getText().length() < 3) {
            Alert alert = GUIUtility.showAlert("Errore","Inserire un nome valido", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }

        if (group_description.getText() == null || group_description.getText().length() < 3) {
            Alert alert = GUIUtility.showAlert("Errore","Inserire una descrizione valida", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }

        Group g = new Group(
                group_name.getText(),
                group_email.getText(),
                group_description.getText());


        App.getGroupList().add(g);
        //ObservableUtility.refreshStudents();
        GrouplistController.getGroupsObservableList().clear();
        ObservableUtility.buildObservableGroupList(App.getGroupList()); //messo io
        return true;

    }


    public void save(ActionEvent event){
        //((Node)(event.getSource())).getScene().getWindow().hide();
        /*if(editmode){
            if(saveExistent()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Utente " + group_email.getText()+domain_combobox.getSelectionModel().getSelectedItem().toString() + " modificato correttamente", ButtonType.OK);
                alert.showAndWait();

            }
        } else{*/
            if(addGroup()){
                Alert alert = GUIUtility.showAlert("Conferma", "Gruppo inserito correttamente" , Alert.AlertType.CONFIRMATION, ButtonType.OK);
                group_name.setText("");
                group_description.setText("");
                group_email.setText("");
                domain_combobox.getSelectionModel().clearSelection();
            }
        //}

    }

    public void save_and_close(){
        if(addGroup()){
            Alert alert = GUIUtility.showAlert("Conferma", "Gruppo inserito correttamente" , Alert.AlertType.CONFIRMATION, ButtonType.OK);
            Stage stage = (Stage) save_close_btn.getScene().getWindow();
            stage.close();

        }
    }

    public void close_window()
    {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }


}