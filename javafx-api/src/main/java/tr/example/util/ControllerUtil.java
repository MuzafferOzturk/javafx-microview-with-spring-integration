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
            View view = getControllerProperties(controller);
            if(view != null){
                URL fxmlPath = controller.getClass().getResource("/" + view.value());
                FXMLLoader fxmlLoader = new FXMLLoader(fxmlPath);
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                fxmlLoader.setController(controllerInstance);
                fxmlLoader.setControllerFactory(param -> controllerInstance);
                return fxmlLoader;
            }
            return null;
        }
        catch (Exception ex){
            logger.log(Level.SEVERE, ex.toString());
            return null;
        }
    }

    public <T> View getControllerProperties(Class<?> controller) throws IllegalAccessException, InstantiationException {
        T controllerInstance = (T) controller.newInstance();
        return controllerInstance.getClass().getAnnotation(View.class);
    }
}
