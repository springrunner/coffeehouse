package coffeehouse.libraries.modulemesh.event.spring;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public class PayloadModuleEvent<T extends ModuleEvent> extends ApplicationEvent implements ResolvableTypeProvider {

    private final T payload;

    private final ResolvableType payloadType;

    private PayloadModuleEvent(Object source, T payload, ResolvableType payloadType) {
        super(source);
        this.payload = Objects.requireNonNull(payload, "Payload must not be null");
        this.payloadType = Objects.requireNonNull(payloadType, "PayloadType must not be null");
    }

    public T getPayload() {
        return this.payload;
    }

    @Override
    public ResolvableType getResolvableType() {
        return this.payloadType;
    }

    public static <T extends ModuleEvent> PayloadModuleEvent<T> of(Object source, T payload) {
        return of(source, payload, null);
    }

    public static <T extends ModuleEvent> PayloadModuleEvent<T> of(Object source, T payload, ResolvableType payloadType) {
        return new PayloadModuleEvent<>(source, payload, (payloadType != null ? payloadType : ResolvableType.forInstance(payload)));
    }
}
