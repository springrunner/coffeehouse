package coffeehouse.libraries.modulemesh.function;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author springrunner.kr@gmail.com
 */
public class DefaultModuleFunctionRegistry implements ModuleFunctionRegistry {

    @SuppressWarnings("rawtypes")
    private final List<ModuleFunctionRegistration> registrations = new CopyOnWriteArrayList<>();

    @Override
    public <T> ModuleFunction<T, Void> registerModuleFunction(String path, Class<T> inputType, Consumer<T> function) throws ModuleFunctionStoreException {
        var registration = new ModuleFunctionRegistration<>(path, inputType, Void.class, target -> {
            function.accept(target);
            return null;
        });
        registrations.add(registration);
        return registration;
    }

    @Override
    public <T, R> ModuleFunction<T, R> registerModuleFunction(String path, Class<T> inputType, Class<R> resultType, Function<T, R> function) throws ModuleFunctionStoreException {
        var registration = new ModuleFunctionRegistration<>(path, inputType, resultType, function);
        registrations.add(registration);
        return registration;
    }

    @Override
    public void removeModuleFunction(String path) throws NoSuchModuleFunctionException {
        if (!containsModuleFunction(path)) {
            throw new NoSuchModuleFunctionException("No module-function named '%s' available".formatted(path));
        }
        registrations.removeIf(it -> it.path().equals(path));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, R> ModuleFunction<T, R> getModuleFunction(String path) throws NoSuchModuleFunctionException {
        return registrations.stream()
                .filter(it -> it.path().equals(path))
                .findFirst()
                .orElseThrow(() -> new NoSuchModuleFunctionException("No module-function named '%s' available".formatted(path)));
    }

    @Override
    public boolean containsModuleFunction(String path) {
        return registrations.stream().anyMatch(it -> it.path().equalsIgnoreCase(path));
    }

    @Override
    public String[] getModuleFunctionPaths() {
        return registrations.stream().map(ModuleFunction::getPath).toArray(String[]::new);
    }

    private record ModuleFunctionRegistration<T, R>(String path, Class<T> inputType, Class<R> resultType, Function<T, R> action) implements ModuleFunction<T, R> {
        
        @Override
        public String getPath() {
            return path;
        }

        @Override
        public Class<T> getInputType() {
            return inputType;
        }

        @Override
        public Class<R> getResultType() {
            return resultType;
        }

        @Override
        public R apply(T t) {
            return action.apply(t);
        }
    }
}
