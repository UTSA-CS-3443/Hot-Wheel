module com.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.utsa.cs3443.hot_wheel to javafx.fxml;
    exports edu.utsa.cs3443.hot_wheel;
}
