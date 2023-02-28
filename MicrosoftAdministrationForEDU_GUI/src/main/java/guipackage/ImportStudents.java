package guipackage;

import com.opencsv.exceptions.CsvException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import com.opencsv.CSVReader;
import javafx.util.Callback;
import school.ClassGroup;
import school.Group;
import school.Student;
import utility.RandomString;
import utility.UtilityStudenti;
import utility.UtilityUser;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ImportStudents implements Initializable {

    @FXML private TableView tableview;
    @FXML private Label pathcsv_lbl;
    @FXML private Button selectcsv_btn;
    @FXML private Button import_btn;
    @FXML private TextField txt_nome;
    @FXML private TextField txt_cognome;
    @FXML private TextField txt_email;
    @FXML private TextField txt_scuola;
    @FXML private TextField txt_classe;
    //@FXML private Button btn_salva;
    @FXML private Button btn_nuovo;
    @FXML private CheckBox same_pwd_chk;
    @FXML private TextField password_txt;

    private TableColumn delete_column = null;
    private TableColumn class_group_column = null;
    private TableColumn class_license_column = null;

    private ObservableList<ObservableStudent> obs_student = FXCollections.observableArrayList();



    private List<Student> added_student = null;



    public void selectCSV(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File CSV", "*.csv"));

        File file = fileChooser.showOpenDialog(tableview.getScene().getWindow());
        if(file!=null){
            tableview.getColumns().clear();
            pathcsv_lbl.setText(file.getAbsolutePath());
            try {
                CSVReader reader = new CSVReader(new FileReader(file));
                List<String[]> r = reader.readAll();

                List<String> column_names_on_file = new ArrayList<>();
                column_names_on_file.add(0,"UserPrincipalName");
                column_names_on_file.add(1,"FirstName");
                column_names_on_file.add(2,"LastName");
                column_names_on_file.add(3,"DisplayName");
                column_names_on_file.add(4,"Title");
                column_names_on_file.add(5,"Department");
                column_names_on_file.add(6,"Office");

                Map<String, String> column_names_map = new HashMap<>();
                column_names_map.put("UserPrincipalName", "Email istituzionale");
                column_names_map.put("FirstName", "Nome");
                column_names_map.put("LastName", "Cognome");
                column_names_map.put("DisplayName", "Nome mostrato");
                column_names_map.put("Title", "Tipo utente");
                column_names_map.put("Department", "Scuola");
                column_names_map.put("Office", "Classe");


                //int[] col_index = Arrays.stream(r.get(0)).mapToInt(x -> column_names.indexOf(x)).toArray();
                int[] col_index = column_names_on_file.stream().mapToInt(x-> Arrays.asList(r.get(0)).indexOf(x)).toArray();

                r.remove(0);

                ObservableList<ObservableStudent> obslist = FXCollections.observableArrayList();

                for(String[] array : r){
                    //System.out.println(Arrays.toString(array));
                    obslist.add(new ObservableStudent(
                            array[col_index[1]],
                            array[col_index[2]],
                            array[col_index[0]],
                            array[col_index[5]],
                            array[col_index[6]],
                            false,
                            array[col_index[3]]
                    ));
                }

                Map<String, String> map_fields_obs = new HashMap<>();
                map_fields_obs.put("UserPrincipalName", "email");
                map_fields_obs.put("FirstName","name");
                map_fields_obs.put("LastName","surname");
                map_fields_obs.put("Department","school");
                map_fields_obs.put("Office","classroom");
                map_fields_obs.put("DisplayName","displayname");
                map_fields_obs.put("Title", "title");

                column_names_on_file.forEach(column_name -> {
                    TableColumn<ObservableStudent, String> col = new TableColumn(column_names_map.get(column_name));
                    col.setCellValueFactory(new PropertyValueFactory<ObservableStudent, String>(map_fields_obs.get(column_name)));
                    tableview.getColumns().add(col);
                });

                for(ObservableStudent os: obslist){
                    os.prepareComboboxClassGroup();
                    os.prepareComboboxLicense();
                }



                tableview.setItems(obslist);
                obs_student = obslist;

                //COLONNA GROUPCLASS
                TableColumn<ObservableStudent, ComboBox<Object>> group_class_column = new TableColumn<>("Gruppo classe");
                group_class_column.setCellValueFactory(new PropertyValueFactory<ObservableStudent,ComboBox<Object>>("combobox_group_class"));
                tableview.getColumns().add(group_class_column);
                class_group_column = group_class_column;

                TableColumn<ObservableStudent, ComboBox<Object>> license_column = new TableColumn<>("Licenza");
                license_column.setCellValueFactory(new PropertyValueFactory<ObservableStudent,ComboBox<Object>>("combobox_licenses"));
                tableview.getColumns().add(license_column);
                class_license_column = license_column;

                //COLONNA ELIMINA
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
                                            obs_student.remove(os);
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
                delete_column = elimina_column;
                elimina_column.setCellFactory(elimina_cell_factory);
                tableview.getColumns().add(elimina_column);


                TableColumn<ObservableStudent, String> status_column = new TableColumn<ObservableStudent, String>("Stato");
                status_column.setCellValueFactory(new PropertyValueFactory<ObservableStudent, String>("status"));
                status_column.setPrefWidth(250);
                tableview.getColumns().add(status_column);

            } catch (FileNotFoundException e) {
                Alert alert = GUIUtility.showAlert("Errore","File non trovato", Alert.AlertType.ERROR, ButtonType.OK);
            } catch (IOException e) {
                Alert alert = GUIUtility.showAlert("Errore","File non leggibile: " + e.getMessage(), Alert.AlertType.ERROR, ButtonType.OK);
            } catch (CsvException e) {
                Alert alert = GUIUtility.showAlert("Errore","CSV non formattato correttamente: " + e.getMessage(), Alert.AlertType.ERROR, ButtonType.OK);
            }
        }

    }

    public void insertStudent(){
        if(
                (txt_classe.getText()!=null
                && txt_scuola.getText()!=null
                && txt_cognome.getText()!=null
                && txt_email.getText()!=null
                && txt_nome.getText()!=null)
                &txt_email.getText()!=""
        ){
            if(!UtilityUser.checkEmailSyntax(txt_email.getText())){
                GUIUtility.showAlert("Errore", "Email non valida", Alert.AlertType.ERROR, ButtonType.OK);
                return;
            }

            ObservableStudent os = new ObservableStudent(
                    txt_nome.getText(),
                    txt_cognome.getText(),
                    txt_email.getText(),
                    txt_scuola.getText(),
                    txt_classe.getText(),
                    false,
                    txt_nome.getText() + " " +txt_cognome.getText()
            );
            if(!obs_student.contains(os)){
                obs_student.add(os);
                txt_nome.setText("");
                txt_cognome.setText("");
                txt_email.setText("");
                txt_classe.setText("");
                txt_scuola.setText("");
                os.prepareComboboxClassGroup();
                os.prepareComboboxLicense();
            } else{
                GUIUtility.showAlert("Errore","Esiste uno studente con la stessa email nella lista", Alert.AlertType.ERROR,ButtonType.OK );
            }

        }
    }

    public void importStudents(){

        added_student = new ArrayList<>();
        if(validateUsers(obs_student)) {
            import_btn.setDisable(true);
            tableview.getColumns().remove(delete_column);
            tableview.getColumns().remove(class_group_column);
            tableview.getColumns().remove(class_license_column);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    HashMap<ObservableStudent,String> stati = new HashMap<>();

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            for (ObservableStudent os : stati.keySet()){
                                if(os!=null)
                                    os.setStatus(stati.get(os));
                            }

                        }
                    };

                    for (ObservableStudent obs : obs_student) {
                        if (obs != null) {
                            obs.setStatus("Inserimento...");
                            Platform.runLater(runnable);
                            Student st = new Student(
                                    obs.getName(),
                                    obs.getSurname(),
                                    obs.getEmail(),
                                    obs.getDisplayname()
                            );
                            obs.setUser(st);
                            obs.setStatus("Inserito. In attesa di configurazione...");
                            Platform.runLater(runnable);

                        }
                    }

                    if(obs_student.size()<10){
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    for (ObservableStudent obs : obs_student){
                        if(obs!=null){
                            Student st = (Student) obs.getUser();
                            obs.setStatus("Configurazione...");
                            Platform.runLater(runnable);
                            st.setSchool(obs.getSchool());
                            st.setClassroom(obs.getClassroom());


                            if(obs.getClassGroup()!=null){
                                obs.getClassGroup().addMember(st);
                            }

                            if(obs.getLicense()!=null)
                            {
                                st.setLicense(obs.getLicense().getNome());
                            }

                            App.getStudentList().add(st);
                            added_student.add(st);
                            if (same_pwd_chk.isSelected()) {   // || st.getJsonResponse()==null){
                                UtilityUser.changeUserPassword(st.getUserPrincipalName(), password_txt.getText());
                            } else {
                                if(st.getJsonResponse()==null){
                                    String pwd= RandomString.getAlphaNumericString("Mmm00000");
                                    UtilityUser.changeUserPassword(st.getUserPrincipalName(),pwd);
                                    st.getFields().put("manual_pwd",pwd);
                                }
                            }
                            stati.put(obs, "OK");
                            Platform.runLater(runnable);
                        }
                    }

                    Platform.runLater(() -> {
                        Alert alert = GUIUtility.showAlert("Conferma", "Import riuscito" , Alert.AlertType.CONFIRMATION, ButtonType.OK);
                        saveCredentials(same_pwd_chk.isSelected(),password_txt.getText());

                        tableview.setEditable(true);
                        //tableview.getColumns().add(tableview.getColumns().size()-2, delete_column);
                    });



                }
            });

            thread.setDaemon(true);
            thread.start();

        } else{
            Alert alert = GUIUtility.showAlert("Errore","Import sospeso: alcuni utenti esistono già", Alert.AlertType.ERROR, ButtonType.OK);
        }


    }

    private boolean validateUsers(ObservableList<ObservableStudent> oslist){
        for (ObservableStudent os : oslist){
            if(UtilityStudenti.studentExists(App.getStudentList(),os.getEmail())){
                return false;
            }
        }
        return true;
    }

    private void saveCredentials(boolean same_pwd, String password){
        Alert alert = GUIUtility.showAlert("Conferma salvataggio", "Vuoi salvare le credenziali dei nuovi utenti?" , Alert.AlertType.INFORMATION, ButtonType.YES, ButtonType.NO);
        if(alert.getResult()==ButtonType.YES){
            FileChooser file_choser = new FileChooser();
            file_choser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File CSV", "*.csv"));
            file_choser.setInitialFileName("export_credenziali_nuovi_studenti.csv");

            File file = file_choser.showSaveDialog(tableview.getScene().getWindow());
            if(file!=null){
                try {
                    String s = "";
                    String SEP = ",";
                    for(Student student : added_student){

                        String pwd =same_pwd?password:(student.getJsonResponse()==null)?(String) student.getFields().get("manual_pwd"):(String) student.getJsonResponse().get("Password");
                        s+=student.getFirstName() + SEP
                                + student.getLastName() + SEP
                                + student.getUserPrincipalName() + SEP
                                + student.getSchool() + SEP
                                + student.getClassroom() + SEP
                                + pwd + "\n";
                    }

                    Files.writeString(Path.of(file.getPath()), s, StandardCharsets.UTF_8);

                    GUIUtility.showAlert("Successo", "File salvato correttamente", Alert.AlertType.CONFIRMATION, ButtonType.OK);

                } catch (IOException e){
                    GUIUtility.showAlert("Errore", e.getMessage(), Alert.AlertType.CONFIRMATION, ButtonType.OK);

                }
            }
        }
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txt_nome.textProperty().addListener(((observableValue, s, t1) -> {
            onChangeNameSurname();
        }));

        txt_cognome.textProperty().addListener(((observableValue, s, t1) -> {
            onChangeNameSurname();
        }));


       tableview.setRowFactory(tv -> new TableRow<ObservableStudent>() {
            @Override
            public void updateItem(ObservableStudent item, boolean empty) {
                super.updateItem(item, empty) ;
                Background def = tableview.getBackground();

                if (item == null) {
                    this.getStyleClass().remove("red-row");
                    //setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
                } else if (UtilityStudenti.studentExists(App.getStudentList(),item.getEmail()) && item.getStatus()!="OK"){
                    //setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
                    this.getStyleClass().add("red-row");

                } else{
                    this.getStyleClass().remove("red-row");
                    //setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
                }

                /*
                if (item == null) {
                    setBackground(def);
                } else if (UtilityStudenti.studentExists(App.getStudentList(),item.getEmail()) && item.getStatus()!="OK"){
                    setBackground(new Background(new BackgroundFill(Color.ORANGERED, null, null)));

                } else{
                    setBackground(def);

                }*/


            }
        });

        //tableview.getItnKeyTyped();

        same_pwd_chk.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                password_txt.setDisable(!t1);
            }
        });


        tableview.setEditable(true);



    }

    private void onChangeNameSurname() {
        if(!txt_email.isDisabled()) {
            if (txt_nome.getText() != null && txt_cognome.getText() != null && txt_nome.getText().length() > 0 && txt_cognome.getText().length() > 0) {
                txt_email.setText((txt_nome.getText() + "." + txt_cognome.getText()).toLowerCase().replace(" ","").replace("'",""));
            }
        }
    }
}
