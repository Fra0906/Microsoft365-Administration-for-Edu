package guipackage;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import school.*;
import utility.UtilityGroup;
import utility.UtilityStudenti;


import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClassGroupController<observableGroupClass> implements Initializable {

    @FXML
    private TableView<ObservableGroupClass> table;
    @FXML
    private TableColumn<ObservableGroupClass, String> classecol;
    @FXML
    private TableColumn<ObservableGroupClass, String> scuolacol;
    @FXML
    private TextField txt_classe;
    @FXML
    private TextField txt_scuola;
    @FXML
    private Button savee;

    private ObservableList<ObservableGroupClass> observableList = FXCollections.observableArrayList();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //carica gruppi esistenti
        for(Group group: App.getGroupList()){
            if(ClassGroup.isClassGroup(group)){
                observableList.add(new ObservableGroupClass(ClassGroup.fromGroup(group)));
            }
        }

        classecol.setCellValueFactory(new PropertyValueFactory<ObservableGroupClass, String>("classe"));
        scuolacol.setCellValueFactory(new PropertyValueFactory<ObservableGroupClass, String>("scuola"));

        table.setItems(observableList);
    }


    public boolean AddRecord() {

        if( txt_classe.getText()==null || txt_classe.getText().equals("")) {
            GUIUtility.showAlert("Errore caricamento", "I campi non possono essere vuoti.", Alert.AlertType.ERROR, ButtonType.OK);
            //Alert alert = new Alert(Alert.AlertType.ERROR, "I campi non possono essere vuoti.", ButtonType.OK);
            //alert.showAndWait();
            return false;
        }
        if( txt_scuola.getText()==null || txt_scuola.getText().equals("")) {
            GUIUtility.showAlert("Errore caricamento", "I campi non possono essere vuoti.", Alert.AlertType.ERROR, ButtonType.OK);
            //alert.showAndWait();
            return false;
        }

        Matcher matcher = Pattern.compile("[1-9][a-zA-Z]+").matcher(txt_classe.getText());

        if(!matcher.matches()){
            GUIUtility.showAlert("Errore caricamento", "La classe deve essere composta da un numero e da una sequenza di lettere.", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }

        for(ObservableGroupClass oc: observableList){
            if(oc.getClasse().toLowerCase().equals(txt_classe.getText().toLowerCase()) && oc.getScuola().toLowerCase().equals(txt_scuola.getText().toLowerCase())){
                GUIUtility.showAlert("Errore caricamento", "Gruppo classe già esistente", Alert.AlertType.ERROR, ButtonType.OK);
                return false;
            }
        }

        ClassGroup gruppoClasse = new ClassGroup(txt_classe.getText().toUpperCase(), txt_scuola.getText());

        ObservableGroupClass observableGroupClass = new ObservableGroupClass(gruppoClasse);
        observableList.add(observableGroupClass);
        App.getGroupList().add(gruppoClasse);

        txt_classe.clear();
        txt_scuola.clear();

        return true;
    }



    public void save() {

        if (AddRecord()) {
            GUIUtility.showAlert("Conferma", "Gruppo classe inserito correttamente", Alert.AlertType.CONFIRMATION, ButtonType.OK);
            //Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Gruppo classe inserito correttamente", ButtonType.OK);
            //alert.showAndWait();
            txt_classe.setText("");
        }
    }
}

