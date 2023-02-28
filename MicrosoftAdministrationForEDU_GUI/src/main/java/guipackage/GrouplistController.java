package guipackage;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.w3c.dom.Text;
import powershell.PSGroup;
import powershell.PSUsers;
import school.Group;
import school.Student;
import utility.UtilityGroup;
import utility.UtilityStudenti;


import java.io.IOException;
import java.util.List;

public class GrouplistController {

    @FXML
    private TableView tableview;

    @FXML
    private TextField field_ricerca;

    @FXML
    private Button view_member_btn;

    private static ObservableList<ObservableGroup> group_observable_list = FXCollections.observableArrayList();
    public static ObservableList<ObservableGroup> getGroupsObservableList() {
        return group_observable_list;
    }

    @FXML
    public void initialize() {

        ObservableUtility.buildObservableGroupList(App.getGroupList());

        group_observable_list.addListener(new ListChangeListener<ObservableGroup>() {
            @Override
            public void onChanged(Change<? extends ObservableGroup> change) {

                //System.out.println("ho riaggiornato la lista");
            }
        });

        // System.out.println("SONO QUI");
        TableColumn<ObservableGroup, String> col_name = new TableColumn<ObservableGroup,String>("Nome");
        col_name.setCellValueFactory(new PropertyValueFactory<ObservableGroup, String>("group_name"));
        tableview.getColumns().add(col_name);

        TableColumn<ObservableGroup, String> col_desc = new TableColumn<ObservableGroup,String>("Descrizione");
        col_desc.setCellValueFactory(new PropertyValueFactory<ObservableGroup, String>("description"));
        tableview.getColumns().add(col_desc);

        /*TableColumn<ObservableGroup, String> col_email = new TableColumn<ObservableGroup,String>("Email istituzionale");
        col_email.setCellValueFactory(new PropertyValueFactory<ObservableGroup, String>("email_address"));
        tableview.getColumns().add(col_email);*/

        /*TableColumn<ObservableGroup, String> col_type = new TableColumn<ObservableGroup,String>("Tipo");
        col_type.setCellValueFactory(new PropertyValueFactory<ObservableGroup, String>("group_type"));*/

        TableColumn<ObservableGroup, Void> elimina_column = new TableColumn("Elimina");
        Callback<TableColumn<ObservableGroup, Void>, TableCell<ObservableGroup, Void>> elimina_cell_factory =
                new Callback<TableColumn<ObservableGroup, Void>, TableCell<ObservableGroup, Void>>() {
                    @Override
                    public TableCell<ObservableGroup,Void> call(TableColumn<ObservableGroup,Void> tableColumn) {

                        final TableCell<ObservableGroup, Void> cell = new TableCell<>(){
                            private final Button btn = new Button("Elimina");

                            {
                                btn.setOnAction((ActionEvent event) ->{
                                    ObservableGroup og = getTableView().getItems().get(getIndex());
                                    group_observable_list.remove(og);
                                    Group g = UtilityGroup.findGroup(App.getGroupList(),og.getObjectId());
                                    PSGroup.delete_group(g.getObjectId());
                                    App.getGroupList().remove(g);
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
        //tableview.getColumns().add(col_type);

        tableview.setItems(group_observable_list);

    }

    public void view_comp_btn() throws IOException {
        ObservableList selectedItems = tableview.getSelectionModel().getSelectedItems();
        if(selectedItems.isEmpty()){
            return;
        }

        ObservableGroup selected_group= (ObservableGroup) selectedItems.get(0);

        /*
        System.out.println("MEMBRI: ");
        for(ObservableUser u: selected_group.getMembers())
        {
            System.out.println(u);
        }
        System.out.println("OWNER: ");
        for(ObservableUser u: selected_group.getOwners())
        {
            System.out.println(u);
        }*/
        view_members_owners(selected_group);
        searchgroup();

    }

    public void view_members_owners(ObservableGroup group) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = App.createFXMLLoader("view_members_owners");
        Parent root = App.loadFXML(fxmlLoader);
        ViewMembers controller =fxmlLoader.<ViewMembers>getController();
        //ViewOwners controllerO =fxmlLoader.<ViewOwners>getController();

        //controller.initialize();

        controller.setObj(group);
        controller.setMembers(group.getMembers());
        controller.setOwners(group.getOwners());
        //controllerO.setOwners(group.getOwners());

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tableview.getScene().getWindow());
        //stage.setAlwaysOnTop(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
        stage.showAndWait();
        searchgroup();

    }


    public void edit_group() throws IOException {
        ObservableList selectedItems = tableview.getSelectionModel().getSelectedItems();
        if(selectedItems.isEmpty()){
            return;
        }

        ObservableGroup selected_group = (ObservableGroup) selectedItems.get(0);

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = App.createFXMLLoader("edit_group");
        Parent root = App.loadFXML(fxmlLoader);
        EditGroup controller = fxmlLoader.<EditGroup>getController();

        controller.setObjectId(selected_group.getObjectId());
        controller.setName(selected_group.getGroup_name());
        controller.setDescription(selected_group.getDescription());

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
        stage.setScene(scene);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tableview.getScene().getWindow());
        //stage.setAlwaysOnTop(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
        stage.showAndWait();
        ObservableUtility.buildObservableGroupList(App.getGroupList());
        searchgroup();
    }

    public void newgroup(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Scene scene = new Scene(App.loadFXML("new_group"));
        scene.getStylesheets().add(getClass().getResource(App.css).toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tableview.getScene().getWindow());
        //stage.setAlwaysOnTop(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(App.favicon)));
        stage.showAndWait();
        searchgroup();
        //searchstudent();
    }

    public void searchgroup()
    {
        String name=field_ricerca.getText();
        List<Group> listGrup=UtilityGroup.searchGroup(
                App.getGroupList(),
                name);

        ObservableUtility.buildObservableGroupList(listGrup);
    }
}

