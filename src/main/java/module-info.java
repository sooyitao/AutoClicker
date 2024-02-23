module com.autokeyboard.autoclicker {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.desktop;
    requires java.logging;
    requires com.github.kwhat.jnativehook;

    opens com.autokeyboard.autoclicker to javafx.fxml;
    exports com.autokeyboard.autoclicker;
}