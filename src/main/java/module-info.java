module com.stickhero {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.stickhero to javafx.fxml;
    exports com.stickhero;
}