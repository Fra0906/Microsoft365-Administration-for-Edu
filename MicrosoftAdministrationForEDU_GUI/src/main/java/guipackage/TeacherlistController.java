package guipackage;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import powershell.PSUsers;
import school.Student;
import school.Teacher;
import utility.UtilityDocenti;
import utility.UtilityStudenti;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TeacherlistController {
    @FXML
    private TableView tableview;

    @FXML
    private TextField src_nome;

    @FXML
    private Button block_btn;
    @FXML
    private TextField src_cognome;

    @FXML
    private TextField src_subject;

    @FXML private CheckBox regex_nome;
    @FXML private CheckBox regex_cognome;
    @FXML private CheckBox regex_materie;

    private static ObservableList<ObservableTeacher> teacher_observable_list = FXCollections.observableArrayList();

    public static ObservableList<ObservableTeacher> getTeachersObservableList() {
        return teacher_observable_list;
    }



    @FXML
    public void initialize() {

        regex_nome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchTeacher();
            }
        });

        regex_cognome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchTeacher();
            }
        });

        regex_materie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchTeacher();
            }
        });



        ObservableUtility.buildObservableTeacherList(App.getTeacherList());

        teacher_observable_list.addListener(new ListChangeListener<ObservableTeacher>() {
            @Override
            public void onChanged(Change<? extends ObservableTeacher> change) {

                //System.out.println("ho riaggiornato la lista");
            }
        });

        tableview.setRowFactory(tv -> new TableRow<ObservableTeacher>() {
            @Override
            public void updateItem(ObservableTeacher item, boolean empty) {
                super.updateItem(item, empty) ;
                if (item == null) {
                    this.getStyleClass().remove("red-row");
                    //setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
                } else if (item.isBlockCredential()){
                    //setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
                    this.getStyleClass().add("red-row");

                } else{
                    this.getStyleClass().remove("red-row");
                    //setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
                }
            }
        });

        TableColumn<ObservableTeacher, String> col_name = new TableColumn<ObservableTeacher,String>("Nome");
        col_name.setCellValueFactory(new PropertyValueFactory<ObservableTeacher, String>("name"));
        tableview.getColumns().add(col_name);

        TableColumn<ObservableTeacher, String> col_surname = new TableColumn<ObservableTeacher,String>("Cognome");
        col_surname.setCellValueFactory(new PropertyValueFactory<ObservableTeacher, String>("surname"));
        tableview.getColumns().add(col_surname);

        TableColumn<ObservableTeacher, String> col_email = new TableColumn<ObservableTeacher,String>("Email istituzionale");
        col_email.setCellValueFactory(new PropertyValueFactory<ObservableTeacher, String>("email"));
        tableview.getColumns().add(col_email);

        TableColumn<ObservableTeacher, String> col_subject = new TableColumn<ObservableTeacher,String>("Materie");
        col_subject.setCellValueFactory(new PropertyValueFactory<ObservableTeacher, String>("subjects"));
        tableview.getColumns().add(col_subject);

        TableColumn<ObservableTeacher, Void> elimina_column = new TableColumn("Elimina");
        Callback<TableColumn<ObservableTeacher, Void>, TableCell<ObservableTeacher, Void>> elimina_cell_factory =
                new Callback<TableColumn<ObservableTeacher, Void>, TableCell<ObservableTeacher, Void>>() {
                    @Override
                    public TableCell<ObservableTeacher,Void> call(TableColumn<ObservableTeacher,Void> tableColumn) {

                        final TableCell<ObservableTeacher, Void> cell = new TableCell<>(){
                            private final Button btn = new Button("Elimina");

                            {
                                btn.setOnAction((ActionEvent event) ->{
                                    ObservableTeacher os = getTableView().getItems().get(getIndex());
                                    teacher_observable_list.remove(os);
                                    Teacher teacher = UtilityDocenti.findTeacher(App.getTeacherList(),os.getEmail());
                                    try {
                                        PSUsers.DeleteUser(teacher.getObjectId());
                                        App.getTeacherList().remove(teacher);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });

                            }

                            @Override
                            protected void updateItem(Void unused, boolean empty) {
                                super.updateItem(unused, empty);{
                                    if(empty){
                                        setGraphic(null);
                                    } else {
                                        setGraphic(btn);
                                    }
                                }
                            }
                        };
                        return cell;

                    }
                };

        elimina_column.setCellFactory(elimina_cell_factory);
        tableview.getColumns().add(elimina_column);


        tableview.setItems(teacher_observable_list);

    }



    public void addTeacher() throws IOException {

        Stage stage = new Stage();
        Scene scene = new Scene(App.loadFXML("new_teacher"));
        scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tableview.getScene().getWindow());
        //stage.setAlwaysOnTop(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
        stage.showAndWait();
        searchTeacher();
    }


    public void editTeacher() throws IOException {

        ObservableList selectedItems = tableview.getSelectionModel().getSelectedItems();
        if(selectedItems.isEmpty()){
            return;
        }

        ObservableTeacher selected_teacher;
        Teacher teacher;
        try {
            selected_teacher = (ObservableTeacher) selectedItems.get(0);
            teacher = UtilityDocenti.findTeacher(App.getTeacherList(), selected_teacher.getEmail());


            Stage stage = new Stage();
            FXMLLoader fxmlLoader = App.createFXMLLoader("new_teacher");
            Parent root = App.loadFXML(fxmlLoader);
            //here
            NewTeacher controller = fxmlLoader.<NewTeacher>getController();

            controller.setEditMode(true);

            controller.setTeacher(selected_teacher);

            Scene scene =new Scene(root);
            scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
            stage.setScene(scene);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tableview.getScene().getWindow());
            //stage.setAlwaysOnTop(true);
            stage.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
            stage.showAndWait();
            searchTeacher();
        } catch (NullPointerException e){
            GUIUtility.showAlert("Errore", "C'è un problema di allineamento. Ricarica i dati e riprova.", Alert.AlertType.ERROR,ButtonType.OK);
            tableview.getScene().getWindow().hide();
            return;
        }
    }

    public void deleteTeacher(){
        ObservableList selectedItems = tableview.getSelectionModel().getSelectedItems();
        if(selectedItems.isEmpty()){
            return;
        }

        ObservableTeacher selected_teacher;
        Teacher teacher;
        try {
            selected_teacher = (ObservableTeacher) selectedItems.get(0);
            teacher = UtilityDocenti.findTeacher(App.getTeacherList(), selected_teacher.getEmail());


            Alert alert = GUIUtility.showAlert("Conferma rimozione","Vuoi davvero cancellare " + teacher + " ?" , Alert.AlertType.WARNING, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

            if (alert.getResult() == ButtonType.YES) {
                try {
                    PSUsers.DeleteUser(teacher.getObjectId());
                    App.getTeacherList().remove(teacher);
                    getTeachersObservableList().remove(selected_teacher);
                    searchTeacher();

                } catch (IOException e) {
                    GUIUtility.showAlert("Errore", "Errore: " + e.getMessage(), Alert.AlertType.ERROR, ButtonType.OK);
                }
            }
        } catch (NullPointerException e){
            GUIUtility.showAlert("Errore", "C'è un problema di allineamento. Ricarica i dati e riprova.", Alert.AlertType.ERROR,ButtonType.OK);
            tableview.getScene().getWindow().hide();
            return;
        }
    }

    public void blockTeacher()
    {
        ObservableList selectedItems = tableview.getSelectionModel().getSelectedItems();
        if(selectedItems.isEmpty()){
            return;
        }

        ObservableTeacher selected_teacher;
        Teacher teacher;
        try {
            selected_teacher = (ObservableTeacher) selectedItems.get(0);
            teacher = UtilityDocenti.findTeacher(App.getTeacherList(), selected_teacher.getEmail());
        } catch (NullPointerException e){
            GUIUtility.showAlert("Errore", "C'è un problema di allineamento. Ricarica i dati e riprova.", Alert.AlertType.ERROR,ButtonType.OK);
            tableview.getScene().getWindow().hide();
            return;
        }

        boolean block=teacher.isBlockCredential();
        Alert alert;
        if(block)
        {
            alert = GUIUtility.showAlert("Conferma sblocco","Sbloccare le credenziali di " + teacher + " ?" , Alert.AlertType.WARNING, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        }
        else
        {
            alert = GUIUtility.showAlert("Conferma blocco","Bloccare le credenziali di " + teacher + " ?" , Alert.AlertType.WARNING, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

        }

        if(alert.getResult()==ButtonType.YES) {
            teacher.setBlockCredential(!block);
            ObservableUtility.refreshTeachers();
            searchTeacher();
        }

    }

    public  void searchTeacher()
    {

        String name=src_nome.getText();
        String surname=src_cognome.getText();
        String subject=src_subject.getText();
        List<Teacher> listTeacher=UtilityDocenti.searchTeacher(
                App.getTeacherList(),
                name,
                surname,
                subject,
                regex_nome.isSelected(),
                regex_cognome.isSelected(),
                regex_materie.isSelected()
        );
        ObservableUtility.buildObservableTeacherList(listTeacher);
    }

    public void exportCSV(){
        List<Teacher> list = ObservableUtility.getTeacherListFromObservable(App.getTeacherList(), getTeachersObservableList());
        FileChooser file_choser = new FileChooser();
        file_choser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File CSV", "*.csv"));
        file_choser.setInitialFileName("export_teachers.csv");

        File file = file_choser.showSaveDialog(tableview.getScene().getWindow());
        if(file!=null){
            try {
                UtilityDocenti.exportCSV(list, file.getAbsolutePath());
                Alert alert = GUIUtility.showAlert("Conferma", "file salvato correttamente" , Alert.AlertType.CONFIRMATION, ButtonType.OK);
            } catch (IOException e){
                Alert alert = GUIUtility.showAlert("Errore", e.getMessage(), Alert.AlertType.ERROR, ButtonType.OK);
            }
        }
    }

    private void change_buttom_text(String msg)
    {
        block_btn.setText(msg);
    }
    public void change_btn()
    {
        ObservableList selectedItems = tableview.getSelectionModel().getSelectedItems();
        if(selectedItems.isEmpty()){
            return;
        }

        ObservableTeacher selected_teacher = (ObservableTeacher) selectedItems.get(0);
        Teacher t = UtilityDocenti.findTeacher(App.getTeacherList() ,selected_teacher.getEmail());
        boolean block=t.isBlockCredential();
        if(block)
        {
            change_buttom_text("Sblocca docente");
        }
        else
        {
            change_buttom_text("Blocca docente");
        }
    }

}
