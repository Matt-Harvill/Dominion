module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.matthew to javafx.fxml;
    exports org.matthew;
    exports org.matthew.controller;
    opens org.matthew.controller to javafx.fxml;
    exports org.matthew.controller.fxml;
    opens org.matthew.controller.fxml to javafx.fxml;
}
