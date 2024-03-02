package coffeehouse.libraries.modulemesh.event;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * @author springrunner.kr@gmail.com
 */
interface ModuleEventExtensions {

    default Optional<Object> getExtension(String extensionName) {
        return Optional.empty();
    }

    default Set<String> getExtensionNames() {
        return Collections.emptySet();
    }
}
