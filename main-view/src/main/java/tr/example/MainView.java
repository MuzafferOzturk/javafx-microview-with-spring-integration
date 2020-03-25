package tr.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.scheduling.annotation.EnableScheduling;
import tr.example.data.StageUserData;
import tr.example.event.StageReadyEvent;

@SpringBootApplication
@EnableScheduling
public class MainView extends Application {
    Logger logger = LogManager.getLogger(MainView.class);
    ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
                context = new SpringApplicationBuilder().
                        sources(MainView.class)
                        .run("");
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StageController.getInstance().setStarterClass(MainViewController.class);
        StageController.getInstance().startStage(primaryStage);
        primaryStage.setUserData(new StageUserData("primaryStage"));
        context.publishEvent(new StageReadyEvent(primaryStage));
    }


    @ServiceActivator(inputChannel = "mainViewChannel")
    public void getController(Class<?> controller){
        try{
            StageController.getInstance().addStage(controller);
        }
        catch (Exception ex){
            logger.error(ex.toString());
        }
    }

}
