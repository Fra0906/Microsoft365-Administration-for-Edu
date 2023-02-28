package guipackage;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public class GUIUtility {

    public static Alert showAlert(String title, String msg, Alert.AlertType type, ButtonType ... btn){
        Alert alert = new Alert(type ,msg, btn);
        alert.setHeaderText(title);
        alert.getDialogPane().setMinHeight(250);
        alert.getDialogPane().setMinWidth(400);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().getStylesheets().add(GUIUtility.class.getResource(App.css).toExternalForm());
        alert.getDialogPane().getStyleClass().add("alert");
        alert.getDialogPane().getStyleClass().add("alert-"+type);

        alert.showAndWait();
        return alert;
    }

}
