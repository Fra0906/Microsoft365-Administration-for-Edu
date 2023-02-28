package guipackage;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import powershell.PS;
import school.*;

import java.io.IOException;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primary_stage;
    private static List<Student> all_student;

    private static List<Teacher> all_teacher;
    private static List<Group> all_group;
    private static String admin_email;
    private static List<String> domain_list;
    private static List<License> licences_list;
    private static List<Subject> materie_list;

    public final static String css = "style/style.css";
    public final static String favicon = "img/officeedu.png";

    public static List<Subject> getMaterieList() {
        return materie_list;
    }

    public static void setMaterieList(List<Subject> materie_list) {
        App.materie_list = materie_list;
    }

    @Override
    public void start(Stage stage) throws IOException {
        primary_stage=stage;
        scene = new Scene(loadFXML("connection"));

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    PS.getConsole().close();
                    stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
        primary_stage.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
        scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);


        stage.show();
        stage.setIconified(false);

    }

    public static String getAdminEmail() {
        return admin_email;
    }

    public static void setAdminEmail(String admin_email) {
        App.admin_email = admin_email;
    }

    public static void setLicenceList(List<License> licences) {
        licences_list = licences;
    }

    public static List<License> getLicencesList(){
        return licences_list;
    }

    public static List<Student> getStudentList(){
        return all_student;
    }

    public static void setAllStudent(List<Student> list){
        all_student = list;
    }

    public static List<Teacher> getTeacherList(){
        return all_teacher;
    }

    public static void setAllTeachers(List<Teacher> list){
        all_teacher = list;
    }

    public static List<Group> getGroupList(){
        return all_group;
    }

    public static void setAllGroups(List<Group> list){
        all_group = list;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Scene getScene() {
        return scene;
    }

    public static Stage getPrimaryStage() {
        return primary_stage;
    }

    public static List<String> getDomainList() {
        return domain_list;
    }

    public static void setDomainList(List<String> domain_list) {
        App.domain_list = domain_list;
    }


    public static FXMLLoader createFXMLLoader(String fxml){
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }

    public static Parent loadFXML(String fxml) throws IOException {
        return createFXMLLoader(fxml).load();
    }

    public static Parent loadFXML(FXMLLoader fxmlLoader) throws IOException {
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}