package tr.example;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import tr.example.data.StageUserData;
import tr.example.event.StageReadyEvent;

@Component
public class FirstViewService {

    @Bean
    public MessageChannel firstViewChannel(){
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow echo(){
        return IntegrationFlows
                .from("firstViewChannel")
                .channel("mainViewChannel")
                .get();
    }

    @Autowired
    @Qualifier("firstViewChannel")
    MessageChannel channel;

    @EventListener
    public void init(StageReadyEvent event){
        Stage stage = (Stage) event.getSource();
        if(stage.getUserData() instanceof StageUserData
                && ((StageUserData) stage.getUserData()).getStageId().equals("primaryStage"))
            channel.send(MessageBuilder.withPayload(FirstViewController.class).build());
    }
}
