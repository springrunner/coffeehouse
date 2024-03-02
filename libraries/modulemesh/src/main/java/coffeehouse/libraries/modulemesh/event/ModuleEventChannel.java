package coffeehouse.libraries.modulemesh.event;

/**
 * @author springrunner.kr@gmail.com
 */
public interface ModuleEventChannel {

    void sendEvent(ModuleEvent event);
}
