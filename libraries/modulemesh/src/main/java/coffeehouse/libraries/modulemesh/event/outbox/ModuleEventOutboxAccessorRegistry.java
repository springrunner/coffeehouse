package coffeehouse.libraries.modulemesh.event.outbox;

/**
 * @author springrunner.kr@gmail.com
 */
public interface ModuleEventOutboxAccessorRegistry {

    void registerModuleEventOutboxAccessor(ModuleEventOutboxAccessor moduleEventOutboxAccessor);
}
