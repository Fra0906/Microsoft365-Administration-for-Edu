package guipackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Menu {

    @FXML
    private Button gestione_studenti_btn;

    @FXML
    private Button gestione_docenti_btn;

    @FXML
    private Button gestione_gruppi_btn;

    @FXML
    private Button multiaddstudenti_btn;

    @FXML private Button refresh_btn;
    @FXML private Button promozione_studenti_btn;
    @FXML private Button creazione_didateam_btn;

    public void creazioneTeamDidattici(){
        try {
            Stage s = new Stage();
            Scene scene = new Scene(App.loadFXML("creazione_team_didattici"));
            s.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
            scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
            s.setScene(scene);
            s.show();
            //App.getPrimaryStage().setScene(new Scene(App.loadFXML("studentlist")));
        } catch (IOException e) {
            GUIUtility.showAlert("Errore caricamento", "Errore caricamento", Alert.AlertType.ERROR, ButtonType.OK);
            //Alert alert = new Alert(Alert.AlertType.ERROR, "Errore", ButtonType.OK);
            //alert.showAndWait();
        }
    }

    public void refreshData(){
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
            GUIUtility.showAlert("Errore caricamento", "Errore caricamento: "+ e.getMessage(), Alert.AlertType.ERROR, ButtonType.OK);
            //Alert alert = new Alert(Alert.AlertType.ERROR, "Errore " + e.getMessage(), ButtonType.OK);
            //alert.showAndWait();
        }
    }

    public void loadStudentListView(){
        try {
            Stage s = new Stage();
            s.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
            Scene scene = new Scene(App.loadFXML("studentlist"));
            scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
            s.setScene(scene);
            s.show();
            //App.getPrimaryStage().setScene(new Scene(App.loadFXML("studentlist")));
        } catch (IOException e) {
            GUIUtility.showAlert("Errore caricamento", "Errore nell'apertura della lista studenti", Alert.AlertType.ERROR, ButtonType.OK);
            //Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nell'apertura della lista studenti", ButtonType.OK);
            //alert.showAndWait();
        }
    }

    public void loadTeacherListView(){
        try {
            Stage s = new Stage();
            s.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
            Scene scene =new Scene(App.loadFXML("teacherlist"));
            scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
            s.setScene(scene);
            s.show();
            //App.getPrimaryStage().setScene(new Scene(App.loadFXML("teacherlist")));
        } catch (IOException e) {
            GUIUtility.showAlert("Errore caricamento", "Errore nell'apertura della lista docenti", Alert.AlertType.ERROR, ButtonType.OK);
            //Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nell'apertura della lista docenti", ButtonType.OK);
            //alert.showAndWait();
        }
    }

    public void loadGroupListView(){
        try {
            Stage s = new Stage();
            s.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
            Scene scene =new Scene(App.loadFXML("grouplist"));
            scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
            s.setScene(scene);
            s.show();
            //App.getPrimaryStage().setScene(new Scene(App.loadFXML("grouplist")));
        } catch (IOException e) {
            System.out.println(e);
            GUIUtility.showAlert("Errore caricamento", "Errore nell'apertura della lista gruppi", Alert.AlertType.ERROR, ButtonType.OK);
            //Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nell'apertura della lista gruppi", ButtonType.OK);
            //alert.showAndWait();
        }
    }

    public void loadMultiAddStudent(){
        try {
            Stage s = new Stage();
            s.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
            Scene scene =new Scene(App.loadFXML("import_student_check_csv"));
            scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
            s.setScene(scene);
            s.show();
            //App.getPrimaryStage().setScene(new Scene(App.loadFXML("import_student_check_csv")));
        } catch (IOException e) {
            System.out.println(e);
            GUIUtility.showAlert("Errore caricamento", "Errore nell'apertura", Alert.AlertType.ERROR, ButtonType.OK);
            //Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nell'apertura", ButtonType.OK);
            //alert.showAndWait();
        }
    }

    public void loadPromozioneStudenti(){
        try {
            Stage s = new Stage();
            s.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
            Scene scene =new Scene(App.loadFXML("promozione_studenti"));
            scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
            s.setScene(scene);
            s.show();
            //App.getPrimaryStage().setScene(new Scene(App.loadFXML("import_student_check_csv")));
        } catch (IOException e) {
            System.out.println(e);
            GUIUtility.showAlert("Errore caricamento", "Errore nell'apertura", Alert.AlertType.ERROR, ButtonType.OK);
            //Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nell'apertura", ButtonType.OK);
            //alert.showAndWait();
        }
    }

    public void loadMultiAddTeacher()
    {
        try {
            Stage s = new Stage();
            s.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
            Scene scene =new Scene(App.loadFXML("import_teacher_check_csv"));
            scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
            s.setScene(scene);
            s.show();
            //App.getPrimaryStage().setScene(new Scene(App.loadFXML("import_student_check_csv")));
        } catch (IOException e) {
            System.out.println(e);
            GUIUtility.showAlert("Errore caricamento", "Errore nell'apertura", Alert.AlertType.ERROR, ButtonType.OK);
            //Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nell'apertura", ButtonType.OK);
            //alert.showAndWait();
        }
    }
    public void loadGroupClassView(){
        try {
            Stage s = new Stage();
            s.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
            Scene scene =new Scene(App.loadFXML("classgroups"));
            scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
            s.setScene(scene);
            s.show();
            //App.getPrimaryStage().setScene(new Scene(App.loadFXML("studentlist")));
        } catch (IOException e) {
            GUIUtility.showAlert("Errore caricamento", "Errore nell'apertura della lista dei gruppi classe", Alert.AlertType.ERROR, ButtonType.OK);
            //Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nell'apertura della lista dei gruppi classe", ButtonType.OK);
            //alert.showAndWait();
        }
    }

}
