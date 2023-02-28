module org.microsoftadministration {
    requires javafx.controls;
    requires javafx.fxml;
    //requires gson;
    requires opencsv;
    //requires msal4j;
    requires org.json;
    requires java.sql;
    //requires com.microsoft.graph;
    //requires com.azure.identity;
    //requires com.azure.core;
    //requires com.microsoft.graph.core;


    opens guipackage to javafx.fxml;
    exports guipackage;
}