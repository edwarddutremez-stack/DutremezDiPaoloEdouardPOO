module app.poo4examen {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens app.poo4examen to javafx.fxml;
    opens app.poo4examen.controller to javafx.fxml;
    opens app.poo4examen.model to com.google.gson, javafx.base;

    exports app.poo4examen;
    exports app.poo4examen.controller;
    exports app.poo4examen.model;
}