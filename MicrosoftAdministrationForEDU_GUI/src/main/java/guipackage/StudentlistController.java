package guipackage;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.Style;
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
import utility.UtilityStudenti;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class StudentlistController {

    @FXML
    private TableView tableview;

    @FXML
    private TextField src_nome;

    @FXML
    private TextField src_cognome;

    @FXML
    private TextField src_class;
    @FXML
    private TextField src_school;

    @FXML Button block_student;

    @FXML
    private Button exportcsv_btn;

    @FXML private CheckBox regex_nome;
    @FXML private CheckBox regex_cognome;
    @FXML private CheckBox regex_scuola;
    @FXML private CheckBox regex_classe;

    private static ObservableList<ObservableStudent> students_observable_list = FXCollections.observableArrayList();

    public static ObservableList<ObservableStudent> getStudentsObservableList() {
        return students_observable_list;
    }

    @FXML
    public void initialize() {

        ObservableUtility.buildObservableStudentList(App.getStudentList());

        students_observable_list.addListener(new ListChangeListener<ObservableStudent>() {
            @Override
            public void onChanged(Change<? extends ObservableStudent> change) {

                //System.out.println("ho riaggiornato la lista");
            }
        });

        regex_nome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchstudent();
            }
        });

        regex_cognome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchstudent();
            }
        });

        regex_scuola.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchstudent();
            }
        });

        regex_classe.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                searchstudent();
            }
        });


        tableview.setRowFactory(tv -> new TableRow<ObservableStudent>() {
            @Override
            public void updateItem(ObservableStudent item, boolean empty) {
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

        TableColumn<ObservableStudent, String> col_name = new TableColumn<ObservableStudent,String>("Nome");
        col_name.setCellValueFactory(new PropertyValueFactory<ObservableStudent, String>("name"));
        tableview.getColumns().add(col_name);

        TableColumn<ObservableStudent, String> col_surname = new TableColumn<ObservableStudent,String>("Cognome");
        col_surname.setCellValueFactory(new PropertyValueFactory<ObservableStudent, String>("surname"));
        tableview.getColumns().add(col_surname);

        TableColumn<ObservableStudent, String> col_email = new TableColumn<ObservableStudent,String>("Email istituzionale");
        col_email.setCellValueFactory(new PropertyValueFactory<ObservableStudent, String>("email"));
        tableview.getColumns().add(col_email);

        TableColumn<ObservableStudent, String> col_school = new TableColumn<ObservableStudent,String>("Scuola");
        col_school.setCellValueFactory(new PropertyValueFactory<ObservableStudent, String>("school"));
        tableview.getColumns().add(col_school);

        TableColumn<ObservableStudent, String> col_class = new TableColumn<ObservableStudent,String>("Classe");
        col_class.setCellValueFactory(new PropertyValueFactory<ObservableStudent, String>("classroom"));
        tableview.getColumns().add(col_class);

        TableColumn<ObservableStudent, Void> elimina_column = new TableColumn("Elimina");
        Callback<TableColumn<ObservableStudent, Void>, TableCell<ObservableStudent, Void>> elimina_cell_factory =
                new Callback<TableColumn<ObservableStudent, Void>, TableCell<ObservableStudent, Void>>() {
                    @Override
                    public TableCell<ObservableStudent,Void> call(TableColumn<ObservableStudent,Void> tableColumn) {

                        final TableCell<ObservableStudent, Void> cell = new TableCell<>(){
                            private final Button btn = new Button("Elimina");

                            {
                                btn.setOnAction((ActionEvent event) ->{
                                    ObservableStudent os = getTableView().getItems().get(getIndex());
                                    students_observable_list.remove(os);
                                    Student student = UtilityStudenti.findStudent(App.getStudentList(),os.getEmail());
                                    try {
                                        PSUsers.DeleteUser(student.getObjectId());
                                        App.getStudentList().remove(student);
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


        tableview.setItems(students_observable_list);



    }


    public void addStudent() throws IOException {

        Stage stage = new Stage();
        Scene scene =new Scene(App.loadFXML("new_student"));
        scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tableview.getScene().getWindow());
        //stage.setAlwaysOnTop(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
        stage.showAndWait();
        searchstudent();
    }

    public void editStudent() throws IOException {

        ObservableList selectedItems = tableview.getSelectionModel().getSelectedItems();
        if(selectedItems.isEmpty()){
            return;
        }

        ObservableStudent selected_student;
        Student student;
        try {
            selected_student = (ObservableStudent) selectedItems.get(0);
            student = UtilityStudenti.findStudent(App.getStudentList(), selected_student.getEmail());


        Stage stage = new Stage();
        FXMLLoader fxmlLoader = App.createFXMLLoader("new_student");
        Parent root = App.loadFXML(fxmlLoader);
        NewStudent controller = fxmlLoader.<NewStudent>getController();

        controller.setEditMode(true);

        controller.setStudent(selected_student);


        Scene scene =new Scene(root);
        scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
        stage.setScene(scene);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tableview.getScene().getWindow());
        //stage.setAlwaysOnTop(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
        stage.showAndWait();
        searchstudent();
        } catch (NullPointerException e){
            GUIUtility.showAlert("Errore", "C'è un problema di allineamento. Ricarica i dati e riprova.", Alert.AlertType.ERROR,ButtonType.OK);
            tableview.getScene().getWindow().hide();
            return;
        }

    }

    public void deleteStudent(){
        ObservableList selectedItems = tableview.getSelectionModel().getSelectedItems();
        if(selectedItems.isEmpty()){
            return;
        }
        ObservableStudent selected_student;
        Student student;
        try {
            selected_student = (ObservableStudent) selectedItems.get(0);
            student = UtilityStudenti.findStudent(App.getStudentList(), selected_student.getEmail());
        } catch (NullPointerException e){
            GUIUtility.showAlert("Errore", "C'è un problema di allineamento. Ricarica i dati e riprova.", Alert.AlertType.ERROR,ButtonType.OK);
            tableview.getScene().getWindow().hide();
            return;
        }
        Alert alert = GUIUtility.showAlert("Conferma rimozione","Vuoi davvero rimuovere " + student + " ?" , Alert.AlertType.WARNING, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);


        if (alert.getResult() == ButtonType.YES) {
            try {
                PSUsers.DeleteUser(student.getObjectId());
                App.getStudentList().remove(student);
                getStudentsObservableList().remove(selected_student);
                searchstudent();

            } catch (IOException e) {
                GUIUtility.showAlert("Errore","Errore: " + e.getMessage(), Alert.AlertType.ERROR, ButtonType.OK );
            }
        }
    }

    public void blockStudent()
    {
        ObservableList selectedItems = tableview.getSelectionModel().getSelectedItems();
        if(selectedItems.isEmpty()){
            return;
        }

        ObservableStudent selected_student;
        Student student;
        try {
            selected_student = (ObservableStudent) selectedItems.get(0);
            student = UtilityStudenti.findStudent(App.getStudentList(), selected_student.getEmail());
        } catch (NullPointerException e){
            GUIUtility.showAlert("Errore", "C'è un problema di allineamento. Ricarica i dati e riprova.", Alert.AlertType.ERROR,ButtonType.OK);
            tableview.getScene().getWindow().hide();
            return;
        }

        boolean block=student.isBlockCredential();
        Alert alert;
        if(block)
        {
            alert = GUIUtility.showAlert("Conferma sblocco","Sbloccare le credenziali di " + student + " ?" , Alert.AlertType.WARNING, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        }
        else
        {
            alert = GUIUtility.showAlert("Conferma blocco","Bloccare le credenziali di " + student + " ?" , Alert.AlertType.WARNING, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        }

        if(alert.getResult()==ButtonType.YES) {
            student.setBlockCredential(!block);
            ObservableUtility.refreshStudents();
            searchstudent();
        }

    }

    public  void searchstudent()
    {

        String name=src_nome.getText();
        String surname=src_cognome.getText();
        String classe=src_class.getText();
        String school=src_school.getText();
        List<Student> listStud=UtilityStudenti.searchStudent(
                App.getStudentList(),
                name,
                surname,
                classe,
                school,
                regex_nome.isSelected(),
                regex_cognome.isSelected(),
                regex_scuola.isSelected(),
                regex_classe.isSelected()
                );
        ObservableUtility.buildObservableStudentList(listStud);
    }

    public void exportCSV(){
        List<Student> list = ObservableUtility.getStudentListFromObservable(App.getStudentList(), getStudentsObservableList());
        FileChooser file_choser = new FileChooser();
        file_choser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File CSV", "*.csv"));
        file_choser.setInitialFileName("export_studenti.csv");

        File file = file_choser.showSaveDialog(tableview.getScene().getWindow());
        if(file!=null){
            try {
                UtilityStudenti.exportCSV(list, file.getAbsolutePath());
                Alert alert = GUIUtility.showAlert("Conferma", "file salvato correttamente" , Alert.AlertType.CONFIRMATION, ButtonType.OK);
            } catch (IOException e){
                Alert alert = GUIUtility.showAlert("Errore",e.getMessage(), Alert.AlertType.ERROR, ButtonType.OK);
            }
        }
    }


    private void change_buttom_text(String msg)
    {
        block_student.setText(msg);
    }

    public void change()
    {
        ObservableList selectedItems = tableview.getSelectionModel().getSelectedItems();
        if(selectedItems.isEmpty()){
            return;
        }
        ObservableStudent selected_student;
        Student student;
        try {
            selected_student = (ObservableStudent) selectedItems.get(0);
            student = UtilityStudenti.findStudent(App.getStudentList(), selected_student.getEmail());
        } catch (NullPointerException e){
            GUIUtility.showAlert("Errore", "C'è un problema di allineamento. Ricarica i dati e riprova.", Alert.AlertType.ERROR,ButtonType.OK);
            tableview.getScene().getWindow().hide();
            return;
        }

        boolean block=student.isBlockCredential();
        if(block)
        {
            change_buttom_text("Sblocca studente");
        }
        else
        {
            change_buttom_text("Blocca studente");
        }
    }
}
