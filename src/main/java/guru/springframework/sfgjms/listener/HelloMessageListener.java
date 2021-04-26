package guru.springframework.sfgjms.listener;

import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloMessageListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers, Message message, org.springframework.messaging.Message<HelloWorldMessage> springMessage) {
        System.out.println("Got a message");

        System.out.println(helloWorldMessage);
    }

    @JmsListener(destination = JmsConfig.MY_RECEIVE_QUEUE)
    public void listenForSendAndReceive(@Payload HelloWorldMessage helloWorldMessage,
                                        @Headers MessageHeaders headers, Message message) throws JMSException {

        var payload = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Confirm message")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), payload);

    }
}
