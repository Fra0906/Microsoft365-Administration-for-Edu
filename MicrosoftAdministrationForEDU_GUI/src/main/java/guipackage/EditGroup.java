package guipackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import school.Group;
import utility.UtilityGroup;

public class EditGroup {

    @FXML
    private Button save_btn;

    private String objid;
    @FXML private TextField name;
    @FXML private TextField description;

    @FXML
    public void initialize() {
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setDescription(String description)
    {
        this.description.setText(description);
    }

    public boolean save(ActionEvent event) {

        Group g = UtilityGroup.findGroup(App.getGroupList(), this.objid);
        g.startSyncronizing();

        if(name.getText().length()<3){
            Alert alert = GUIUtility.showAlert("Errore","Inserire un nome valido", Alert.AlertType.ERROR, ButtonType.OK);
            return false;
        }

        if(!g.getGroupName().equals(name.getText())){
            g.setGroupName(name.getText());
        }
        if(!g.getDescription().equals(description.getText())){
            g.setGroupName(description.getText());
        }

        Alert alert = GUIUtility.showAlert("Conferma",  "Gruppo " + name.getText() + " modificato correttamente", Alert.AlertType.CONFIRMATION, ButtonType.OK);
        Stage stage = (Stage) save_btn.getScene().getWindow();
        stage.close();
        ObservableUtility.refreshGroups();

        return true;
    }

    public void setObjectId(String objectId)
    {
        this.objid=objectId;
    }
}
