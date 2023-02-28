package guipackage;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import school.*;
import utility.UtilityUser;

import java.net.URL;
import java.util.*;

public class CreazioneTeamDidattici implements Initializable {

    @FXML private TableView tableview_teams;
    @FXML private ListView lista_docenti;
    @FXML private ListView lista_gruppi_classe;
    @FXML private ListView lista_materie;

    @FXML private Button aggiungi_materia_btn;
    @FXML private Button aggiungi_abbinamento_btn;
    @FXML private Button crea_btn;
    @FXML private Button pulisci_btn;

    @FXML private TextField nuova_materia_txt;
    @FXML private AnchorPane pane_scegliabbinamento;

    private ObservableList<ObservableTeacher> obs_teacher_list;
    private ObservableList<ObservableAbbinamento> obs_abbinamenti_list;
    private ObservableList<ObservableMateria> obs_materie_list;
    private ObservableList<ObservableClassGroup> obs_class_group_list;

    private List<String> abbinamenti_esistenti;

    private TableColumn delete_column;


    public class ObservableTeacher{

        public Teacher teacher;
        public SimpleStringProperty nome;

        public ObservableTeacher(Teacher teacher) {
            this.teacher = teacher;
            nome = new SimpleStringProperty(teacher.getDisplayName());
        }

        public Teacher getTeacher() {
            return teacher;
        }

        public void setTeacher(Teacher teacher) {
            this.teacher = teacher;
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

        @Override
        public String toString() {
            return nome.get();
        }
    }

    public class ObservableClassGroup{
        public ClassGroup group;
        public SimpleStringProperty nome;

        public ClassGroup getGroup() {
            return group;
        }

        public void setGroup(ClassGroup group) {
            this.group = group;
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



        public ObservableClassGroup(ClassGroup group) {
            this.group = group;
            nome = new SimpleStringProperty(group.getClasse() + " (" + group.getScuola() + ") ["+Integer.toString(group.getMembers().size()) + "]");
        }

        @Override
        public String toString() {
            return nome.get();
        }
    }

    public class ObservableMateria{
        public SimpleStringProperty nome;

        public String getNome() {
            return nome.get();
        }

        public SimpleStringProperty nomeProperty() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome.set(nome);
        }

        public ObservableMateria(String nome) {
            this.nome = new SimpleStringProperty(nome);
        }

        @Override
        public String toString() {
            return nome.get();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ObservableMateria that = (ObservableMateria) o;

            return that.getNome().equals(getNome());
        }

    }

    public class ObservableAbbinamento{

        public ObservableTeacher teacher;
        public ObservableClassGroup classGroup;
        public ObservableMateria materia;
        public Button elimina;
        public SimpleStringProperty status = new SimpleStringProperty();

        private String nome;
        private String descrizione;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getDescrizione() {
            return descrizione;
        }

        public void setDescrizione(String descrizione) {
            this.descrizione = descrizione;
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



        public Button getElimina() {
            return elimina;
        }

        public void setElimina(Button elimina) {
            this.elimina = elimina;
        }

        public ObservableTeacher getTeacher() {
            return teacher;
        }

        public void setTeacher(ObservableTeacher teacher) {
            this.teacher = teacher;
        }

        public ObservableClassGroup getClassGroup() {
            return classGroup;
        }

        public void setClassGroup(ObservableClassGroup classGroup) {
            this.classGroup = classGroup;
        }

        public ObservableMateria getMateria() {
            return materia;
        }

        public void setMateria(ObservableMateria materia) {
            this.materia = materia;
        }

        public ObservableAbbinamento(ObservableTeacher teacher, ObservableClassGroup classGroup, ObservableMateria materia) {
            this.teacher = teacher;
            this.classGroup = classGroup;
            this.materia = materia;

            nome = materia.getNome() + " - " + classGroup.getGroup().getClasse() + " [" + classGroup.getGroup().getScuola() + "] - prof." + teacher.getTeacher().getLastName();
            descrizione = "DIDATEAM#"+materia.getNome()+"#"+classGroup.getGroup().getClasse()+"#"+classGroup.getGroup().getScuola()+"#"+teacher.getTeacher().getLastName();

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ObservableAbbinamento that = (ObservableAbbinamento) o;


            return that.getDescrizione().equals(this.getDescrizione()) || that.getClassGroup()==this.getClassGroup() && that.getTeacher()==this.getTeacher() && that.getMateria()==this.getMateria();
        }


        public void submit(){

            Team team = new Team(nome, descrizione, teacher.getTeacher(), "");
            for(User user : getClassGroup().getGroup().getMembers()){
                team.addMember(user);
            }

            App.getGroupList().add(team);

        }
    }

    public void aggiungiMateria(){
        if(nuova_materia_txt.getText()!=null && nuova_materia_txt.getText().length()>0){
            ObservableMateria om = new ObservableMateria(nuova_materia_txt.getText().toUpperCase());
            if(!obs_materie_list.contains(om)){
                obs_materie_list.add(om);
                nuova_materia_txt.clear();
            }
        }
    }

    public void aggiungiAbbinamento(){

        ObservableMateria materia;
        ObservableClassGroup classGroup;
        ObservableTeacher teacher;

        if(lista_materie.getSelectionModel().getSelectedItem()==null){
            Alert alert = GUIUtility.showAlert("Errore","Non hai selezionato la materia", Alert.AlertType.ERROR, ButtonType.OK);
            return;
        }

        if(lista_docenti.getSelectionModel().getSelectedItem()==null){
            Alert alert = GUIUtility.showAlert("Errore","Non hai selezionato il docente", Alert.AlertType.ERROR, ButtonType.OK);
            return ;
        }

        if(lista_gruppi_classe.getSelectionModel().getSelectedItem()==null){
            Alert alert = GUIUtility.showAlert("Errore","Non hai selezionato il gruppo classe", Alert.AlertType.ERROR, ButtonType.OK);
            return ;
        }

        for (Object cg : lista_gruppi_classe.getSelectionModel().getSelectedItems()){
            for(Object m : lista_materie.getSelectionModel().getSelectedItems()){
                materia = (ObservableMateria) m;
                teacher = (ObservableTeacher) lista_docenti.getSelectionModel().getSelectedItem();
                classGroup = (ObservableClassGroup) cg;
                ObservableAbbinamento oa = new ObservableAbbinamento(teacher,classGroup,materia);

                if(!abbinamenti_esistenti.contains(oa.getDescrizione())){
                    abbinamenti_esistenti.add(oa.getDescrizione());
                } else {
                    Alert alert = GUIUtility.showAlert("Errore","Abbinamento " + oa.getDescrizione() + " già presente", Alert.AlertType.ERROR, ButtonType.OK);
                    continue;
                }

                if(!obs_abbinamenti_list.contains(oa)){
                    obs_abbinamenti_list.add(oa);
                } else {
                    Alert alert = GUIUtility.showAlert("Errore","Abbinamento " + oa.getDescrizione() + " già presente in lista", Alert.AlertType.ERROR, ButtonType.OK);
                    continue;
                }
            }
        }

        return ;

    }

    public void creaTeams(){

        crea_btn.setDisable(true);
        tableview_teams.getColumns().remove(delete_column);
        pane_scegliabbinamento.setDisable(true);
        pulisci_btn.setDisable(true);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<ObservableAbbinamento,String> stati = new HashMap<>();

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        for (ObservableAbbinamento os : stati.keySet()){
                            if(os!=null)
                                os.setStatus(stati.get(os));
                        }

                    }
                };

                for (ObservableAbbinamento obs : obs_abbinamenti_list) {
                    if(obs!=null) {

                        try {

                            String nome = obs.getMateria().getNome() + " - " + obs.getClassGroup().getGroup().getClasse() + " [" + obs.getClassGroup().getGroup().getScuola() + "] - prof." + obs.getTeacher().getTeacher().getLastName();
                            String descrizione = obs.getDescrizione(); //obs.getMateria().getNome()+"#"+obs.getClassGroup().getGroup().getClasse()+"#"+obs.getClassGroup().getGroup().getScuola()+"#"+obs.getTeacher().getTeacher().getLastName();

                            obs.setStatus("Creazione...");
                            Platform.runLater(runnable);

                            Teacher teacher = obs.getTeacher().getTeacher();
                            if(teacher.getLicense()==null || teacher.getLicense().equals("null") || teacher.getLicense().length()==0){
                                throw new Exception("Il docente non ha una licenza, non può essere importato come proprietario");
                            }
                            Team team = new Team(nome, descrizione, teacher, "");
                            for(User user : obs.getClassGroup().getGroup().getMembers()){
                                obs.setStatus("Inserimento " + user);
                                Platform.runLater(runnable);
                                team.addMember(user);
                            }

                            Subject subject = new Subject(obs.getMateria().getNome());
                            obs.getTeacher().getTeacher().addSubject(subject);

                            if(!App.getMaterieList().contains(subject)){
                                App.getMaterieList().add(subject);
                            }

                            App.getGroupList().add(team);
                            stati.put(obs, "OK");
                        } catch (Exception e){
                            stati.put(obs, "FALLITO " + e.getMessage());
                        }

                        Platform.runLater(runnable);
                    }
                }

                Platform.runLater(() -> {
                    Alert alert = GUIUtility.showAlert("Conferma","Import riuscito"  , Alert.AlertType.CONFIRMATION, ButtonType.OK);
                    pulisci_btn.setDisable(false);
                });



            }
        });

        thread.setDaemon(true);
        thread.start();



    }

    public void pulisciLista(){

        obs_abbinamenti_list.clear();
        abbinamenti_esistenti.clear();
        for(Group group : App.getGroupList()){
            if(group.getDescription().startsWith("DIDATEAM")){
                abbinamenti_esistenti.add(group.getDescription());
            }
        }
        crea_btn.setDisable(false);
        pane_scegliabbinamento.setDisable(false);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        obs_teacher_list = FXCollections.observableArrayList();
        for(Teacher teacher:App.getTeacherList()){
            obs_teacher_list.add(new ObservableTeacher(teacher));
        }
        lista_docenti.setItems(obs_teacher_list);

        obs_materie_list = FXCollections.observableArrayList();
        lista_materie.setItems(obs_materie_list);

        for(Subject subject: App.getMaterieList()){
            obs_materie_list.add(new ObservableMateria(subject.getSubjectName().toUpperCase()));
        }

        obs_class_group_list = FXCollections.observableArrayList();
        for (Group group : App.getGroupList()){
            if(ClassGroup.isClassGroup(group)){
                obs_class_group_list.add(new ObservableClassGroup(ClassGroup.fromGroup(group)));
            }
        }

        lista_gruppi_classe.setItems(obs_class_group_list);

        obs_abbinamenti_list = FXCollections.observableArrayList();
        tableview_teams.setItems(obs_abbinamenti_list);


        abbinamenti_esistenti = new LinkedList<>();
        for(Group group : App.getGroupList()){
            if(group.getDescription().startsWith("DIDATEAM")){
                abbinamenti_esistenti.add(group.getDescription());
            }
        }

        lista_gruppi_classe.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lista_materie.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);



        TableColumn<ObservableAbbinamento, ObservableMateria> col_materia = new TableColumn<>("Materia");
        col_materia.setCellValueFactory(new PropertyValueFactory<ObservableAbbinamento,ObservableMateria>("materia"));
        col_materia.setPrefWidth(140);
        tableview_teams.getColumns().add(col_materia);

        TableColumn<ObservableAbbinamento, ObservableTeacher> col_teacher = new TableColumn<>("Docente");
        col_teacher.setCellValueFactory(new PropertyValueFactory<ObservableAbbinamento, ObservableTeacher>("teacher"));
        col_teacher.setPrefWidth(140);
        tableview_teams.getColumns().add(col_teacher);

        TableColumn<ObservableAbbinamento, ObservableClassGroup> col_group_class = new TableColumn<>("Gruppo classe");
        col_group_class.setCellValueFactory(new PropertyValueFactory<ObservableAbbinamento, ObservableClassGroup>("classGroup"));
        col_group_class.setPrefWidth(125);
        tableview_teams.getColumns().add(col_group_class);

        /*TableColumn<ObservableAbbinamento, Button> col_elimina = new TableColumn<>("Elimina");
        col_elimina.setCellValueFactory(new PropertyValueFactory<ObservableAbbinamento, Button>("elimina"));
        tableview_teams.getColumns().add(col_elimina);*/

        //COLONNA ELIMINA
        TableColumn<ObservableAbbinamento, Void> elimina_column = new TableColumn("Elimina");
        elimina_column.setPrefWidth(100);
        Callback<TableColumn<ObservableAbbinamento, Void>, TableCell<ObservableAbbinamento, Void>> elimina_cell_factory =
                new Callback<TableColumn<ObservableAbbinamento, Void>, TableCell<ObservableAbbinamento, Void>>() {
                    @Override
                    public TableCell<ObservableAbbinamento,Void> call(TableColumn<ObservableAbbinamento,Void> tableColumn) {

                        final TableCell<ObservableAbbinamento, Void> cell = new TableCell<>(){
                            private final Button btn = new Button("Elimina");

                            {
                                btn.setOnAction((ActionEvent event) ->{
                                    ObservableAbbinamento os = getTableView().getItems().get(getIndex());
                                    obs_abbinamenti_list.remove(os);
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
        tableview_teams.getColumns().add(elimina_column);

        TableColumn<ObservableAbbinamento, String> col_status = new TableColumn<>("Stato");
        col_status.setCellValueFactory(new PropertyValueFactory<ObservableAbbinamento, String>("status"));
        col_status.setPrefWidth(250);
        tableview_teams.getColumns().add(col_status);


    }
}
