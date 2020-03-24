package tr.example.util;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import tr.example.View;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControllerUtil {
    private static ControllerUtil instance;
    Logger logger = Logger.getLogger(ControllerUtil.class.getName());

    public static ControllerUtil getInstance(){
        if(instance == null)
            instance = new ControllerUtil();
        return instance;
    }

    public <T> FXMLLoader createLoader(Class<?> controller){
        try{
            T controllerInstance = (T) controller.newInstance();
            URL fxmlPath = controller.getClass().getResource("/" + getFxmlName(controllerInstance));
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlPath);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            fxmlLoader.setController(controllerInstance);
            fxmlLoader.setControllerFactory(param -> controllerInstance);
            return fxmlLoader;
        }
        catch (Exception ex){
            logger.log(Level.SEVERE, ex.toString());
            return null;
        }
    }


    private String getFxmlName(Object controller){
        String fxmlName = "";
        View annotation = controller.getClass().getAnnotation(View.class);
        if(annotation != null)
            fxmlName = annotation.value();
        return fxmlName;
    }

}
