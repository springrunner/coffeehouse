package coffeehouse.libraries.modulemesh.event.outbox;

/**
 * @author springrunner.kr@gmail.com
 */
public interface ModuleEventOutboxRegistry {

    void registerModuleEventOutbox(ModuleEventOutbox moduleEventOutbox);
}
