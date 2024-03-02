package coffeehouse.libraries.modulemesh.event.spring;

import coffeehouse.libraries.modulemesh.event.ModuleEvent;

/**
 * @author springrunner.kr@gmail.com
 */
public abstract class ApplicationModuleEventInvokers {

    public static ModuleEventInvoker simple() {
        return (moduleEvent, moduleEventHandler) -> moduleEventHandler.run();
    }

    public interface ModuleEventInvoker {

        void invoke(ModuleEvent moduleEvent, Runnable moduleEventHandler);
    }
}
