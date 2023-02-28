package guipackage;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import powershell.PSGroup;
import powershell.PSUsers;
import school.Group;
import school.Student;
import school.User;
import utility.UtilityStudenti;
import utility.UtilityUser;

import java.io.IOException;

public class ViewMembers {

    @FXML
    private TableView tv_member;

    @FXML
    private TableView tv_owner;



    private ObservableGroup groupob=new ObservableGroup();



    @FXML
    public void initialize() {


        addColumn1();
        addColumn2();


    }

    private void addColumn2() {

        TableColumn<ObservableUser, String> col_name_o = new TableColumn<ObservableUser, String>("Nome");
        col_name_o.setCellValueFactory(new PropertyValueFactory<ObservableUser, String>("name"));
        tv_owner.getColumns().add(col_name_o);

        TableColumn<ObservableUser, String> col_surname_o = new TableColumn<ObservableUser, String>("Cognome");
        col_surname_o.setCellValueFactory(new PropertyValueFactory<ObservableUser, String>("surname"));
        tv_owner.getColumns().add(col_surname_o);


        TableColumn<ObservableUser, String> col_email_o = new TableColumn<ObservableUser, String>("Email_istituzionale");
        col_email_o.setCellValueFactory(new PropertyValueFactory<ObservableUser, String>("email"));
        tv_owner.getColumns().add(col_email_o);


        TableColumn<ObservableUser, String> col_title_o = new TableColumn<ObservableUser, String>("Titolo");
        col_title_o.setCellValueFactory(new PropertyValueFactory<ObservableUser, String>("title"));
        tv_owner.getColumns().add(col_title_o);

        TableColumn<ObservableUser, Void> rimuovi_column = new TableColumn("Rimuovi");
        Callback<TableColumn<ObservableUser, Void>, TableCell<ObservableUser, Void>> elimina_cell_factory =
                new Callback<TableColumn<ObservableUser, Void>, TableCell<ObservableUser, Void>>() {
                    @Override
                    public TableCell<ObservableUser,Void> call(TableColumn<ObservableUser,Void> tableColumn) {

                        final TableCell<ObservableUser, Void> cell = new TableCell<>(){
                            private final Button btn = new Button("Rimuovi");

                            {
                                btn.setOnAction((ActionEvent event) ->{
                                    ObservableUser os = getTableView().getItems().get(getIndex());
                                    groupob.remove_owner(os);
                                    //System.out.println(selected_u.getObjectId());
                                    User user=UtilityUser.findUser(os.getObjectId(), App.getStudentList(), App.getTeacherList());
                                    PSGroup.remove_owner(groupob.getObjectId(), user);
                                    tv_owner.refresh();
                                    setOwners(groupob.getOwners());
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

        rimuovi_column.setCellFactory(elimina_cell_factory);
        tv_owner.getColumns().add(rimuovi_column);


    }


    private void addColumn1()
    {
        TableColumn<ObservableUser, String> col_name = new TableColumn<ObservableUser,String>("Nome");
        col_name.setCellValueFactory(new PropertyValueFactory<ObservableUser, String>("name"));
        tv_member.getColumns().add(col_name);


        TableColumn<ObservableUser, String> col_surname = new TableColumn<ObservableUser,String>("Cognome");
        col_surname.setCellValueFactory(new PropertyValueFactory<ObservableUser, String>("surname"));
        tv_member.getColumns().add(col_surname);


        TableColumn<ObservableUser, String> col_email = new TableColumn<ObservableUser,String>("Email_istituzionale");
        col_email.setCellValueFactory(new PropertyValueFactory<ObservableUser, String>("email"));
        tv_member.getColumns().add(col_email);


        TableColumn<ObservableUser, String> col_title = new TableColumn<ObservableUser,String>("Titolo");
        col_title.setCellValueFactory(new PropertyValueFactory<ObservableUser, String>("title"));
        tv_member.getColumns().add(col_title);

        TableColumn<ObservableUser, Void> rimuovi_column = new TableColumn("Rimuovi");
        Callback<TableColumn<ObservableUser, Void>, TableCell<ObservableUser, Void>> elimina_cell_factory =
                new Callback<TableColumn<ObservableUser, Void>, TableCell<ObservableUser, Void>>() {
                    @Override
                    public TableCell<ObservableUser,Void> call(TableColumn<ObservableUser,Void> tableColumn) {

                        final TableCell<ObservableUser, Void> cell = new TableCell<>(){
                            private final Button btn = new Button("Rimuovi");

                            {
                                btn.setOnAction((ActionEvent event) ->{
                                    ObservableUser os = getTableView().getItems().get(getIndex());
                                    groupob.remove_member(os);
                                    //System.out.println(selected_u.getObjectId());
                                    User user=UtilityUser.findUser(os.getObjectId(), App.getStudentList(), App.getTeacherList());
                                    PSGroup.remove_member(groupob.getObjectId(), user);
                                    tv_member.refresh();
                                    setMembers(groupob.getMembers());
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

        rimuovi_column.setCellFactory(elimina_cell_factory);
        tv_member.getColumns().add(rimuovi_column);


    }
    public  void setMembers(ObservableList<ObservableUser> list)
    {
        tv_member.setItems(list);
    }

    public  void setOwners(ObservableList<ObservableUser> list)
    {
        tv_owner.setItems(list);
    }


    public void setObj(ObservableGroup objectId) {
        groupob=objectId;
    }

    public void  rimuovi()
    {
        ObservableList selectedItems = tv_member.getSelectionModel().getSelectedItems();
        ObservableList selectedOwner = tv_owner.getSelectionModel().getSelectedItems();
        if(selectedItems.isEmpty() && selectedOwner.isEmpty()){
            return;
        }
        else if(!selectedItems.isEmpty())
        {
            ObservableUser selected_u= (ObservableUser) selectedItems.get(0);
            Alert alert = GUIUtility.showAlert("Conferma rimozione", "Remove " + selected_u + " from this group ?", Alert.AlertType.WARNING, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

            if(alert.getResult()==ButtonType.YES)
            {
                //objgroup.getMembers().remove(selected_u);
                groupob.remove_member(selected_u);
                //System.out.println(selected_u.getObjectId());
                User user=UtilityUser.findUser(selected_u.getObjectId(), App.getStudentList(), App.getTeacherList());
                PSGroup.remove_member(groupob.getObjectId(), user);
                tv_member.refresh();
                setMembers(groupob.getMembers());

            }
        }
        else if(!selectedOwner.isEmpty())
        {
            ObservableUser selected_u= (ObservableUser) selectedOwner.get(0);
            Alert alert = GUIUtility.showAlert("Conferma rimozione", "Remove " + selected_u + " from this group ?", Alert.AlertType.WARNING, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

            if(alert.getResult()==ButtonType.YES)
            {
                //objgroup.getOwners().remove(selected_u);
                groupob.remove_owner(selected_u);
                //System.out.println(selected_u.getObjectId());
                User user=UtilityUser.findUser(selected_u.getObjectId(), App.getStudentList(), App.getTeacherList());
                PSGroup.remove_owner(groupob.getObjectId(), user);
                tv_owner.refresh();
                setOwners(groupob.getOwners());
            }
        }

    }
    public void add_member(ObservableGroup group) throws IOException {
        //  quiii

    }


    public void add_member(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = App.createFXMLLoader("show_add_user");
        Parent root = App.loadFXML(fxmlLoader);
        Show_Add_Users controller =fxmlLoader.<Show_Add_Users>getController();

        controller.setMode("Member");
        controller.setGroup(groupob);

        Scene scene =new Scene(root);
        scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tv_member.getScene().getWindow());
        //stage.setAlwaysOnTop(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
        stage.showAndWait();
    }

    public void add_owner(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = App.createFXMLLoader("show_add_user");
        Parent root = App.loadFXML(fxmlLoader);
        Show_Add_Users controller =fxmlLoader.<Show_Add_Users>getController();


        controller.setMode("Owner");
        controller.setGroup(groupob);

        Scene scene =new Scene(root);
        scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tv_owner.getScene().getWindow());
        //stage.setAlwaysOnTop(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
        stage.showAndWait();
    }
}