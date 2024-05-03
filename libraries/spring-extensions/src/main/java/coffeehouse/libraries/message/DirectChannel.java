package coffeehouse.libraries.message;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.Observable;

/**
 * @author springrunner.kr@gmail.com
 */
public class DirectChannel extends Observable implements MessageChannel {
    @Override
    public boolean send(Message<?> message) {
        this.setChanged();
        this.notifyObservers(message);
        return MessageChannel.super.send(message);
    }

    @Override
    public boolean send(Message<?> message, long timeout) {
        return false;
    }
}
