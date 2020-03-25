package tr.example;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import tr.example.data.StageUserData;
import tr.example.event.StageReadyEvent;

@Component
public class SecondViewService {
    @Bean
    public MessageChannel secondViewChannel(){
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow secondViewEcho(){
        return IntegrationFlows
                .from("secondViewChannel")
                .channel("mainViewChannel")
                .get();
    }

    @Autowired
    @Qualifier("secondViewChannel")
    MessageChannel channel;

    @EventListener
    public void init(StageReadyEvent event){
        Stage stage = (Stage) event.getSource();
        if(stage.getUserData() instanceof StageUserData
                && ((StageUserData) stage.getUserData()).getStageId().equals("primaryStage"))
            channel.send(MessageBuilder.withPayload(SecondViewController.class).build());
    }
}
