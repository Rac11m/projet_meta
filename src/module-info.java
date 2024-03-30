module Projet.meta {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens inteface to javafx.fxml;

    exports inteface;
}