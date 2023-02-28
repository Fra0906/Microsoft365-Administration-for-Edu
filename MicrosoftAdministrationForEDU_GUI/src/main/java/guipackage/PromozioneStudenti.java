package guipackage;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import school.ClassGroup;
import school.Group;
import school.Student;
import school.User;
import utility.UtilityGroup;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PromozioneStudenti implements Initializable {

    @FXML private TableView tableview;

    private ObservableList<ObservableItem> lista_item = FXCollections.observableArrayList();

    public static ObservableList<String> list_comandi = FXCollections.observableArrayList(
            "Esegui trasferimento",
            "Non eseguire",
            "Elimina gruppo e blocca studenti"
    );

    public class ObservableItem{
        public StringProperty vecchia_classe;
        public StringProperty nuova_classe;
        public StringProperty scuola;
        public StringProperty nuova_scuola;
        public TextField campo_nuova_scuola;
        public Integer numero_studenti;
        public ComboBox<String> comando;
        public TextField field_nuova_classe;
        public StringProperty status;

        public String getStatus() {
            return status.get();
        }

        public StringProperty statusProperty() {
            return status;
        }

        public void setStatus(String status) {
            this.status.set(status);
        }

        public String getNuova_scuola() {
            return nuova_scuola.get();
        }

        public StringProperty nuova_scuolaProperty() {
            return nuova_scuola;
        }

        public void setNuova_scuola(String nuova_scuola) {
            this.nuova_scuola.set(nuova_scuola);
        }

        public void setCampo_nuova_scuola(TextField campo_nuova_scuola) {
            this.campo_nuova_scuola = campo_nuova_scuola;
        }

        public String getCampo_nuova_scuola() {
            return campo_nuova_scuola.getText();
        }

        public StringProperty scuolaProperty() {
            return scuola;
        }

        public void setScuola(String scuola) {
            this.scuola.set(scuola);
        }

        public Integer getNumero_studenti() {
            return numero_studenti;
        }

        public void setNumero_studenti(Integer numero_studenti) {
            this.numero_studenti = numero_studenti;
        }

        public ComboBox<String> getComando() {
            return comando;
        }

        public void setComando(ComboBox<String> comando) {
            this.comando = comando;
        }

        public TextField getField_nuova_classe() {
            return field_nuova_classe;
        }

        public void setField_nuova_classe(TextField field_nuova_classe) {
            this.field_nuova_classe = field_nuova_classe;
        }

        public TextField getField_nuova_scuola(){return campo_nuova_scuola;}


        public String getVecchia_classe() {
            return vecchia_classe.get();
        }

        public StringProperty vecchia_classeProperty() {
            return vecchia_classe;
        }

        public void setVecchia_classe(String vecchia_classe) {
            this.vecchia_classe.set(vecchia_classe);
        }

        public String getNuova_classe() {
            return nuova_classe.get();
        }

        public StringProperty nuova_classeProperty() {
            return nuova_classe;
        }

        public void setNuova_classe(String nuova_classe) {
            this.nuova_classe.set(nuova_classe);
        }

        public ClassGroup getGroup() {
            return group;
        }

        public void setGroup(ClassGroup group) {
            this.group = group;
        }

        public void createText()
        {
            this.campo_nuova_scuola = new TextField(scuola.get());
        }

        ClassGroup group;

        ObservableItem(ClassGroup gruppo){
            this.group=gruppo;
            vecchia_classe= new SimpleStringProperty(gruppo.getClasse());

            String classe = gruppo.getClasse().substring(0,1);
            String sezione =gruppo.getClasse().substring(1);

            String classenuova = Integer.toString (Integer.parseInt(classe)+1);
            nuova_classe = new SimpleStringProperty(classenuova+sezione);
            nuova_scuola = new SimpleStringProperty(gruppo.getScuola());

            this.scuola = new SimpleStringProperty(gruppo.getScuola());
            this.campo_nuova_scuola = new TextField(scuola.get());
            this.field_nuova_classe = new TextField(nuova_classe.get());
            field_nuova_classe.setText(nuova_classe.get());
            campo_nuova_scuola.setText(scuola.get());

            numero_studenti = gruppo.getMembers().size();

            comando = new ComboBox<>(list_comandi);
            comando.getSelectionModel().selectFirst();
            status = new SimpleStringProperty();

            field_nuova_classe.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                nuova_classe.set(field_nuova_classe.getText());
            } ));

            campo_nuova_scuola.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                nuova_scuola.set(campo_nuova_scuola.getText());
            } ));

        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for (Group group : App.getGroupList()){
            //System.out.print(group.getGroupName());
            if(ClassGroup.isClassGroup(group)){
                //System.out.print(" --> aggiunto");
                lista_item.add(new ObservableItem(ClassGroup.fromGroup(group)));
            }
            //System.out.println();
        }

        //ObservableList<PromozioneStudenti.ObservableItem> list = FXCollections.observableArrayList();
        //list.add(new ObservableItem(new ClassGroup("1B","Secondaria")));

        TableColumn<PromozioneStudenti.ObservableItem,String> old_class_column = new TableColumn<PromozioneStudenti.ObservableItem,String>("Vecchia classe");
        old_class_column.setCellValueFactory( i -> {
            return new SimpleStringProperty(i.getValue().vecchia_classe.get() + " (" + i.getValue().scuola.get() + ")");
        });
        tableview.getColumns().add(old_class_column);

        TableColumn<PromozioneStudenti.ObservableItem, TextField> new_class_column = new TableColumn<>("Nuova classe");
        new_class_column.setCellValueFactory(new PropertyValueFactory<PromozioneStudenti.ObservableItem,TextField>("field_nuova_classe"));
        tableview.getColumns().add(new_class_column);

        TableColumn<PromozioneStudenti.ObservableItem,TextField> new_school_column = new TableColumn<>("Nuova scuola");
        new_school_column.setCellValueFactory(new PropertyValueFactory<PromozioneStudenti.ObservableItem,TextField>("field_nuova_scuola"));
        tableview.getColumns().add(new_school_column);

        TableColumn<ObservableItem,Integer> num_stud_column = new TableColumn<>("Numero studenti");
        num_stud_column.setCellValueFactory(new PropertyValueFactory<ObservableItem,Integer>("numero_studenti"));
        tableview.getColumns().add(num_stud_column);

        TableColumn<ObservableItem,ComboBox<String>> comando_column = new TableColumn<>("Comando");
        comando_column.setCellValueFactory(new PropertyValueFactory<ObservableItem,ComboBox<String>>("comando"));
        tableview.getColumns().add(comando_column);

        TableColumn<ObservableItem,String> status_column = new TableColumn<>("Stato");
        status_column.setCellValueFactory(new PropertyValueFactory<ObservableItem,String>("status"));
        tableview.getColumns().add(status_column);

        tableview.setItems(lista_item);

    }


    public void processa(){

        Alert alert = GUIUtility.showAlert("Procedere?","Vuoi procedere con le azioni indicate?" , Alert.AlertType.INFORMATION, ButtonType.YES, ButtonType.NO);
        if(alert.getResult()==ButtonType.YES) {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    HashMap<ObservableItem, String> stati = new HashMap<>();

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            for(ObservableItem gruppo : stati.keySet()){
                                gruppo.setStatus("OK");
                            }
                            //obs_group.setStatus("OK");
                        }


                    };
                    for (ObservableItem obs_group : lista_item) {
                        obs_group.setStatus("Elaborazione...");
                        Group realgroup = UtilityGroup.findGroup (App.getGroupList(),obs_group.getGroup().getObjectId());

                        switch (obs_group.getComando().getSelectionModel().getSelectedIndex()) {
                            case 0: //ESEGUI TRASFERIMENTO
                            {

                                obs_group.getGroup().setScuola(obs_group.getNuova_scuola());
                                obs_group.getGroup().setClasse(obs_group.getNuova_classe());
                                realgroup.setGroupName(obs_group.getGroup().getGroupName());
                                realgroup.setDescription(obs_group.getGroup().getDescription());
                                realgroup.setEmail_address(obs_group.getGroup().getEmail_address());

                                for (User st : obs_group.getGroup().getMembers()) {
                                    Student student = (Student) st;
                                    student.setClassroom(obs_group.getNuova_classe());
                                    student.setSchool(obs_group.getNuova_scuola());
                                }

                                break;
                            }
                            case 2: // ELIMINA GRUPPO E BLOCCA STUDENTI
                            {
                                for (User st : obs_group.getGroup().getMembers()) {
                                    st.setBlockCredential(true);
                                }

                                UtilityGroup.deleteGroup(App.getGroupList(), realgroup);

                                break;

                            }
                        }
                        stati.put(obs_group,"OK");
                        Platform.runLater(runnable);
                    }

                }
            });

            thread.setDaemon(true);
            thread.start();

        }

    }


}
