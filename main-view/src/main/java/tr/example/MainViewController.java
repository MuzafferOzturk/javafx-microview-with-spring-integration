package tr.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

@View(sceneId = "mainView", value = "MainView.fxml")
public class MainViewController implements Initializable {
    @FXML
    HBox fxMainHBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        fxMainHBox.setPrefSize(dimension.getWidth(), dimension.getHeight());
    }
}
