package guipackage;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import powershell.PS;
import powershell.PSGroup;
import powershell.PSUsers;
import school.*;
import utility.UtilityUser;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;

public class Loader implements Initializable {

    private Double status;
    private String status_msg;


    public Double getStatus(){
        return status;
    }

    public String getStatusMsg(){
        return status_msg;
    }

    @FXML public TableView tableview;
    @FXML public Button load_btn;

    private ObservableLoader load_domini;
    private ObservableLoader load_licenze;
    private ObservableLoader load_studenti;
    private ObservableLoader load_docenti;
    private ObservableLoader load_gruppi;
    private ObservableLoader load_materie;

    private Runnable runnable_view;

    private ObservableList<ObservableLoader> task_list = FXCollections.observableArrayList();

    public void carica() {

        load_btn.setDisable(true);
        HashMap<ObservableLoader, String> stati = new HashMap<>();

        for(ObservableLoader ol : task_list){
            stati.put(ol,"");
        }
        Platform.runLater(runnable_view);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                runnable_view = new Runnable() {
                    @Override
                    public void run() {
                        for (ObservableLoader ol : stati.keySet()){
                            ol.setStatus(stati.get(ol));
                        }
                    }
                };

                {
                    int max_attempts=3;
                    int i=0;
                    boolean successo=false;

                    while(!successo && i<max_attempts) {
                        i++;
                        try {
                            stati.put(load_domini, "Caricamento...");
                            Platform.runLater(runnable_view);
                            App.setDomainList(PS.getDomains());
                            stati.put(load_domini, "OK");
                            Platform.runLater(runnable_view);
                            successo=true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            stati.put(load_domini, "FALLITO");
                            load_btn.setDisable(false);
                            Platform.runLater(runnable_view);
                            //return;
                        }
                    }

                    if(!successo){return;}else{i=0;successo=false;}

                    while(!successo && i<max_attempts) {
                        i++;
                        try {
                            stati.put(load_licenze, "Caricamento...");
                            Platform.runLater(runnable_view);
                            App.setLicenceList(PS.getLicences());
                            stati.put(load_licenze, "OK");
                            Platform.runLater(runnable_view);
                            successo=true;
                        } catch (IOException e){
                            e.printStackTrace();
                            stati.put(load_licenze, "FALLITO");
                            load_btn.setDisable(false);
                            Platform.runLater(runnable_view);
                            //return;
                        }
                    }

                    if(!successo){return;}else{i=0;successo=false;}

                    while(!successo && i<max_attempts) {
                        i++;
                        try {
                            stati.put(load_studenti, "Caricamento...");
                            Platform.runLater(runnable_view);
                            App.setAllStudent(PSUsers.getStudentList());
                            ObservableUtility.refreshStudents();
                            stati.put(load_studenti, "OK");
                            Platform.runLater(runnable_view);
                            successo=true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            stati.put(load_studenti, "FALLITO");
                            load_btn.setDisable(false);
                            Platform.runLater(runnable_view);
                            //return;
                        }
                    }

                    if(!successo){return;}else{i=0;successo=false;}


                    while(!successo && i<max_attempts) {
                        i++;
                        try {
                            stati.put(load_docenti, "Caricamento...");
                            Platform.runLater(runnable_view);
                            App.setAllTeachers(PSUsers.getTeacherList());
                            ObservableUtility.refreshTeachers();
                            stati.put(load_docenti, "OK");
                            Platform.runLater(runnable_view);
                            successo=true;
                        } catch (IOException e) {
                            stati.put(load_docenti, "FALLITO");
                            load_btn.setDisable(false);
                            Platform.runLater(runnable_view);
                            //return;
                        }
                    }

                    if(!successo){return;}else{i=0;successo=false;}

                    while(!successo && i<max_attempts) {
                        i++;
                        try {
                            stati.put(load_materie, "Caricamento...");
                            Platform.runLater(runnable_view);
                            List<Teacher> list_teacher = App.getTeacherList();
                            List<Subject> list_materie = new LinkedList<>();
                            for(Teacher teacher : list_teacher){
                                for (Subject s : teacher.getSubjectTaught()){
                                    if(!list_materie.contains(s)){
                                        list_materie.add(s);
                                    }
                                }
                            }
                            App.setMaterieList(list_materie);
                            stati.put(load_materie, "OK");
                            successo=true;
                            Platform.runLater(runnable_view);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                            stati.put(load_materie, "FALLITO");
                            load_btn.setDisable(false);
                            Platform.runLater(runnable_view);
                            //return;
                        }
                    }

                    if(!successo){return;}else{i=0;successo=false;}


                    while(!successo && i<max_attempts) {
                        i++;
                        try {
                            stati.put(load_gruppi, "Caricamento...");
                            Platform.runLater(runnable_view);
                            App.setAllGroups(PSGroup.getGroupList());
                            for (Group group : App.getGroupList())
                            {
                                group.stopSyncronizing();
                                List<String> list=PSGroup.getGroupMembers(group.getObjectId());
                                for(String objectid : list)
                                {
                                    User u= UtilityUser.findUser(objectid, App.getStudentList(), App.getTeacherList());
                                    if(u!=null)
                                    {
                                        group.addMember(u);
                                    }
                                }
                                List<String> listowner=PSGroup.getGroupOwners(group.getObjectId());
                                for(String objectid : listowner)
                                {
                                    User u=UtilityUser.findUser(objectid, App.getStudentList(), App.getTeacherList());
                                    if(u!=null)
                                    {
                                        group.addOwner(u);
                                    }
                                }
                                group.startSyncronizing();
                            }
                            ObservableUtility.refreshGroups();
                            stati.put(load_gruppi, "OK");
                            successo=true;
                            Platform.runLater(runnable_view);
                        } catch (IOException e ){
                            stati.put(load_gruppi, "FALLITO");
                            load_btn.setDisable(false);
                            Platform.runLater(runnable_view);
                            //return;
                        }
                    }


                    Platform.runLater(() -> {
                        try {
                            Scene scene = new Scene(App.loadFXML("menu"));
                            scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
                            App.getPrimaryStage().setScene(scene);
                            Stage s = (Stage) tableview.getScene().getWindow();
                            s.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

        thread.setDaemon(true);
        thread.start();




    }




    public void run() {

        //while (true) {
        try {
            status_msg = "Recupero i domini...";
            App.setDomainList(PS.getDomains());
            status = 0.2;

            status_msg = "Recupero gli studenti...";
            App.setAllStudent(PSUsers.getStudentList());
            ObservableUtility.refreshStudents();
            status = 0.5;

            status_msg = "Recupero i docenti...";
            App.setAllTeachers(PSUsers.getTeacherList());
            ObservableUtility.refreshTeachers();
            status = 0.75;

            status_msg = "Recupero i gruppi...";
            App.setAllGroups(PSGroup.getGroupList());
            for (Group group : App.getGroupList())
            {
                group.stopSyncronizing();
                List<String> list=PSGroup.getGroupMembers(group.getObjectId());
                for(String objectid : list)
                {
                    User u= UtilityUser.findUser(objectid, App.getStudentList(), App.getTeacherList());
                    if(u!=null)
                    {
                        group.addMember(u);
                    }
                }
                List<String> listowner=PSGroup.getGroupOwners(group.getObjectId());
                for(String objectid : listowner)
                {
                    User u=UtilityUser.findUser(objectid, App.getStudentList(), App.getTeacherList());
                    if(u!=null)
                    {
                        group.addOwner(u);
                    }
                }
                group.startSyncronizing();
            }
            ObservableUtility.refreshGroups();
            status = 1d;
        } catch (Exception e) {
            status_msg = "Fallito. " + e.getMessage();
            status = (double) -1;
        }
            /*try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }

    public class ObservableLoader{

        public Runnable runnable;
        public SimpleStringProperty nome;
        public SimpleStringProperty status;

        public Runnable getRunnable() {
            return runnable;
        }

        public void setRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        public ObservableLoader(String nome) {

            this.nome = new SimpleStringProperty(nome);
            this.status = new SimpleStringProperty();
        }


        public String getNome() {
            return nome.get();
        }

        public SimpleStringProperty nomeProperty() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome.set(nome);
        }

        public String getStatus() {
            return status.get();
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }

        public void setStatus(String status) {
            this.status.set(status);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<ObservableLoader> task_list = FXCollections.observableArrayList();

        load_domini = new ObservableLoader("Caricamento domini");
        load_studenti = new ObservableLoader("Caricamento studenti");
        load_docenti = new ObservableLoader("Caricamento docenti");
        load_gruppi = new ObservableLoader("Caricamento gruppi");
        load_licenze = new ObservableLoader("Caricamento licenze");
        load_materie = new ObservableLoader("Caricamento materie");


        task_list.add(load_domini);
        task_list.add(load_licenze);
        task_list.add(load_studenti);
        task_list.add(load_docenti);
        task_list.add(load_materie);
        task_list.add(load_gruppi);


        TableColumn<ObservableLoader, String> nome_col = new TableColumn<ObservableLoader,String>("Nome");
        nome_col.setCellValueFactory(new PropertyValueFactory<ObservableLoader,String>("nome"));
        nome_col.setMinWidth(175);
        tableview.getColumns().add(nome_col);

        TableColumn<ObservableLoader, String> status_col = new TableColumn<ObservableLoader,String>("Stato");
        status_col.setCellValueFactory(new PropertyValueFactory<ObservableLoader,String>("status"));
        status_col.setMinWidth(125);
        tableview.getColumns().add(status_col);

        tableview.setItems(task_list);

        load_btn.fire();

    }
}
