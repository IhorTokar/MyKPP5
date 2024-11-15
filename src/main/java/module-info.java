module com.example.mykpp5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;


    opens com.example.mykpp5 to javafx.fxml;
    exports com.example.mykpp5;
}