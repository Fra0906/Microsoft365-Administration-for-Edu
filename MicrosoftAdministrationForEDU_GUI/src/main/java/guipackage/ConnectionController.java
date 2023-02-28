package guipackage;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import powershell.PS;
import powershell.PSGroup;
import powershell.PSUsers;
import school.Student;

import java.io.IOException;




public class ConnectionController {

    @FXML
    private TextField email_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private Button connect_button;

    @FXML private Button connect_mfa_btn;

    public void connectMFA(){
        //progress_lbl.setText("Sto connettendo...");

        System.out.println("connection");
        try {

            if(PS.connectMFA()) {

                App.setAdminEmail(email_field.getText());
                //Loader loader = new Loader();
                //loader.start();


                //App.getPrimaryStage().setScene(new Scene(App.loadFXML("menu")));

                try {
                    Stage s = new Stage();
                    s.setResizable(false);
                    s.setAlwaysOnTop(true);
                    s.initStyle(StageStyle.UNDECORATED);
                    Scene scene = new Scene(App.loadFXML("loader"));
                    scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
                    s.setScene(scene);
                    s.show();
                    //App.getPrimaryStage().setScene(new Scene(App.loadFXML("studentlist")));
                } catch (IOException e) {
                    GUIUtility.showAlert("Errore caricamento", "Errore caricamento", Alert.AlertType.ERROR, ButtonType.OK);
                    //Alert alert = new Alert(Alert.AlertType.ERROR, "Errore " + e.getMessage(), ButtonType.OK);
                    //alert.showAndWait();
                }

            } else{
                GUIUtility.showAlert("Errore", "Connessione non riuscita", Alert.AlertType.ERROR, ButtonType.OK);
                //Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Connesso", ButtonType.OK);
                //alert.showAndWait();
            }
        } catch (IOException  e) {
            GUIUtility.showAlert("Errore", "Connessione non riuscita", Alert.AlertType.ERROR, ButtonType.OK);
            //Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            //alert.showAndWait();
        }
    }


    public void connect(){
        //progress_lbl.setText("Sto connettendo...");
        if(email_field.getText()==null || email_field.getText().length()==0){
            GUIUtility.showAlert("Errore","Inserisci un indirizzo email valido", Alert.AlertType.ERROR,ButtonType.OK);
            return;
        }
        if(password_field.getText()==null || password_field.getText().length()==0){
            GUIUtility.showAlert("Errore","Inserisci una password", Alert.AlertType.ERROR,ButtonType.OK);
            return;
        }

        System.out.println("connection");
        try {

            if(PS.connect(email_field.getText(), password_field.getText())) {

                App.setAdminEmail(email_field.getText());
                //Loader loader = new Loader();
                //loader.start();


                //App.getPrimaryStage().setScene(new Scene(App.loadFXML("menu")));

                try {
                    Stage s = new Stage();
                    s.setResizable(false);
                    s.setAlwaysOnTop(true);
                    s.initStyle(StageStyle.UNDECORATED);
                    Scene scene = new Scene(App.loadFXML("loader"));
                    scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
                    s.setScene(scene);
                    s.setScene(scene);
                    s.show();
                    //App.getPrimaryStage().setScene(new Scene(App.loadFXML("studentlist")));
                } catch (IOException e) {
                    GUIUtility.showAlert("Errore caricamento", e.getMessage(), Alert.AlertType.ERROR, ButtonType.OK);
                    //Alert alert = new Alert(Alert.AlertType.ERROR, "Errore " + e.getMessage(), ButtonType.OK);
                    //alert.showAndWait();
                }

            } else{
                GUIUtility.showAlert("Errore connessione", "Credenziali non valide", Alert.AlertType.ERROR, ButtonType.OK);
                //Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Connesso", ButtonType.OK);
                //alert.showAndWait();
            }
        } catch (IOException  e) {
            GUIUtility.showAlert("Errore connessione", e.getMessage(), Alert.AlertType.ERROR, ButtonType.OK);
            //Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            //alert.showAndWait();
        }
    }
}
