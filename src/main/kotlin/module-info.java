module trtox.graphvisualizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens trtox.graphvisualizer to javafx.fxml;
    exports trtox.graphvisualizer;
}