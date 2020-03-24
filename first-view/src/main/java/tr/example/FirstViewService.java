package tr.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

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

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        channel.send(MessageBuilder.withPayload(FirstViewController.class).build());
    }
}
