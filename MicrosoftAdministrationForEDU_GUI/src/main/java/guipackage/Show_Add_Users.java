package guipackage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import powershell.PSGroup;
import school.Group;
import school.Student;
import school.Teacher;
import school.User;
import utility.UtilityDocenti;
import utility.UtilityGroup;
import utility.UtilityStudenti;
import utility.UtilityUser;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static utility.UtilityUser.UserExists;
import static utility.UtilityUser.UserExistsString;

public class Show_Add_Users {
    private String mode;
    private ObservableGroup groupob=new ObservableGroup();

    private static ObservableList<ObservableUser> users_observable_list = FXCollections.observableArrayList();

    public static ObservableList<ObservableUser> getUsersObservableList() {
        return users_observable_list;
    }

    Set<String> lista_objid_selezionati = new HashSet<>();
    Set<String> lista_objid_selezionati_temp = new HashSet<>();
    private boolean testo_cambiato = false;


    @FXML
    private Label label_Add;

    @FXML
    private TextField field_name;

    @FXML
    private  TextField field_surname;

    @FXML
    private TableView tableview;

    @FXML
    private Button add_member_owner;

    public void setMode(String mode)
    {
        this.mode=mode;

        //System.out.println(mode);
        if(mode.equals("Member"))
        {
            label_Add.setText("Aggiungi membri al gruppo "+ groupob.getGroup_name());
        }
        if(mode.equals("Owner"))
        {
            label_Add.setText("Aggiungi proprietari al gruppo "+ groupob.getGroup_name());
        }

    }

    public void setGroup(ObservableGroup groupob) {
        this.groupob=groupob;
    }


    public void initialize()
    {


        users_observable_list=ObservableUtility.buildUserObservableList(App.getStudentList());

        users_observable_list.addListener(new ListChangeListener<ObservableUser>() {
            @Override
            public void onChanged(Change<? extends ObservableUser> change) {
                for (Object u: tableview.getItems()) {
                    if(u instanceof ObservableUser) {
                        ObservableUser ou = (ObservableUser) u;
                        if (lista_objid_selezionati.contains(ou.getObjectId())) {
                            tableview.getSelectionModel().select(ou);
                            //tableview.getSelectionModel().select(u);
                        }
                    }
                }
            }
        });

        ObservableList<ObservableUser> list_doc=ObservableUtility.buildUserObservableList(App.getTeacherList());
        for(ObservableUser u: list_doc)
        {
            //System.out.println("ok");
            users_observable_list.add(u);
        }


        ChangeListener<String> changeListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                lista_objid_selezionati_temp.clear();
                for(String obj : lista_objid_selezionati){
                    lista_objid_selezionati_temp.add(obj);
                }
                testo_cambiato=true;
            }
        };

        field_name.textProperty().addListener(changeListener);
        field_surname.textProperty().addListener(changeListener);


        tableview.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<ObservableUser>(){

            @Override
            public void onChanged(Change<? extends ObservableUser> change) {

                while (change.next()) {
                    if (change.wasRemoved()) {
                        change.getRemoved().forEach(observableUser -> {
                            lista_objid_selezionati.remove(observableUser.getObjectId());
                        });
                    } else {
                        if (change.wasAdded()) {
                            change.getAddedSubList().forEach(observableUser -> {
                                lista_objid_selezionati.add(observableUser.getObjectId());
                            });
                        }
                    }
                }

                if(testo_cambiato){
                    for(String s:lista_objid_selezionati_temp){
                        lista_objid_selezionati.add(s);
                    }
                }
            }
        });


        TableColumn<ObservableUser, String> col_name = new TableColumn<ObservableUser,String>("Nome");
        col_name.setCellValueFactory(new PropertyValueFactory<ObservableUser, String>("name"));
        tableview.getColumns().add(col_name);


        TableColumn<ObservableUser, String> col_surname = new TableColumn<ObservableUser,String>("Cognome");
        col_surname.setCellValueFactory(new PropertyValueFactory<ObservableUser, String>("surname"));
        tableview.getColumns().add(col_surname);

        TableColumn<ObservableUser, String> col_email = new TableColumn<ObservableUser,String>("Email istituzionale");
        col_email.setCellValueFactory(new PropertyValueFactory<ObservableUser, String>("email"));
        tableview.getColumns().add(col_email);

        TableColumn<ObservableUser, String> col_tipo = new TableColumn<ObservableUser,String>("Tipo");
        col_tipo.setCellValueFactory(new PropertyValueFactory<ObservableUser, String>("title"));
        tableview.getColumns().add(col_tipo);



        tableview.setItems(users_observable_list);
        tableview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


    }

    public  void searchUser()
    {
        /*
        for(Object o: tableview.getSelectionModel().getSelectedItems()){
            if(o instanceof ObservableUser) {
                ObservableUser ou = (ObservableUser) o;
                lista_objid_selezionati.add(ou.getObjectId());
            }

        }
        /*
         */

        //ObservableList<ObservableUser> selected_users = FXCollections.observableArrayList(tableview.getSelectionModel().getSelectedItems());


        users_observable_list.clear();
        String name=field_name.getText();
        String surname=field_surname.getText();
        String classe="";
        String school="";
        List<Student> listStud= UtilityStudenti.searchStudent(
                App.getStudentList(),
                name,
                surname,
                classe,
                school,
                false,
                false,
                false,
                false
        );
        List<Teacher> teacher= UtilityDocenti.searchTeacher(
                App.getTeacherList(),
                name,
                surname,
                "",
                false,
                false,
                false
        );
        for(Student s : listStud)
        {
            //System.out.println(s);
        }


        ObservableList<ObservableUser> list_stud=ObservableUtility.buildUserObservableList(listStud);
        ObservableList<ObservableUser> list_doc=ObservableUtility.buildUserObservableList(teacher);
        for(ObservableUser u: list_stud)
        {
            users_observable_list.add(u);
        }
        for(ObservableUser u: list_doc)
        {
            users_observable_list.add(u);
        }

    }


    public void Add(MouseEvent mouseEvent) throws IOException {
        ObservableList selectedItems=tableview.getSelectionModel().getSelectedItems();
        if(selectedItems.isEmpty()){
            return;
        }

        Alert alert = GUIUtility.showAlert("Attenzione","Vuoi inserire " + lista_objid_selezionati.size() + " utenti a questo gruppo?" , Alert.AlertType.WARNING, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        if(alert.getResult()==ButtonType.YES)
        {
            for(Iterator<String> it = lista_objid_selezionati.iterator(); it.hasNext(); )
            {
                String obj_id = it.next();
                User user= UtilityUser.findUser(obj_id, App.getStudentList(), App.getTeacherList());
                Group group = UtilityGroup.findGroup(App.getGroupList(),groupob.getObjectId());
                if(mode.equals("Member"))
                {
                    //boolean result=UserExistsString(PSGroup.getGroupMembers(groupob.getObjectId()), user.getObjectId());
                    boolean result = group.getMembers().contains(user);
                    if(!result)
                    {
                        if(user instanceof  Student)
                        {
                            groupob.getMembers().add(new ObservableStudent((Student) user));
                        }
                        if(user instanceof Teacher)
                        {
                            groupob.getMembers().add(new ObservableTeacher((Teacher) user));
                        }
                        group.addMember(user);
                        //PSGroup.addMember(groupob, user);
                    }
                    else
                    {
                        alert = GUIUtility.showAlert("Attenzione","1 o più membri già presente" , Alert.AlertType.WARNING, ButtonType.OK);
                    }

                }
                if(mode.equals("Owner"))
                {
                    //boolean result=UserExistsString(PSGroup.getGroupOwners(groupob.getObjectId()), user.getObjectId());
                    boolean result = group.getOwners().contains(user);
                    //System.out.println(result);
                    if(!result)
                    {
                        if(user instanceof  Student)
                        {
                            groupob.getOwners().add(new ObservableStudent((Student) user));
                        }
                        if(user instanceof  Teacher)
                        {
                            groupob.getOwners().add(new ObservableTeacher((Teacher) user));
                        }
                        group.addOwner(user);
                    }
                    else
                    {
                        alert = GUIUtility.showAlert("Attenzione","1 o più proprietari già presente" , Alert.AlertType.WARNING, ButtonType.OK);
                    }
                }



            }
        }
        Stage stage = (Stage) add_member_owner.getScene().getWindow();
        stage.close();


    }


}
