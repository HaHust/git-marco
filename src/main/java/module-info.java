module com.example.gitmacro {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.eclipse.jgit;
requires lombok;

    exports com.example.gitmacro.controller;
    opens com.example.gitmacro.controller to javafx.fxml;

    opens com.example.gitmacro to javafx.fxml;
    exports com.example.gitmacro;
    exports com.example.gitmacro.model;
    opens com.example.gitmacro.model to javafx.fxml;
}