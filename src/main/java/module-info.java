module edu.utsa.cs3443.hotwheel {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.utsa.cs3443.hotwheel.controller to javafx.fxml;
    opens edu.utsa.cs3443.hotwheel.view to javafx.fxml;

    exports edu.utsa.cs3443.hotwheel.view;
    exports edu.utsa.cs3443.hotwheel.controller;
    exports edu.utsa.cs3443.hotwheel.manager;
    exports edu.utsa.cs3443.hotwheel.model;
}