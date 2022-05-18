module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.java;

//    exports com.example.demo1;
//    opens com.example.demo1 to javafx.fxml;
//    exports com.example.demo1.Controller;
//    opens com.example.demo1.Controller to javafx.fxml;
//    exports com.example.demo1.Applications;
//    opens com.example.demo1.Applications to javafx.fxml;
    exports com.example.demo1;
    opens com.example.demo1 to javafx.fxml;
}