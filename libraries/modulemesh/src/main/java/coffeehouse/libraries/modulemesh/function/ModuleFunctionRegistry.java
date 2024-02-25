package coffeehouse.libraries.modulemesh.function;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author springrunner.kr@gmail.com
 */
public interface ModuleFunctionRegistry {

    <T> ModuleFunction<T, Void> registerModuleFunction(String path, Class<T> inputType, Consumer<T> function) throws ModuleFunctionStoreException;

    <T, R> ModuleFunction<T, R> registerModuleFunction(String path, Class<T> inputType, Class<R> resultType, Function<T, R> function) throws ModuleFunctionStoreException;

    void removeModuleFunction(String path) throws NoSuchModuleFunctionException;

    <T, R> ModuleFunction<T, R> getModuleFunction(String path) throws NoSuchModuleFunctionException;

    boolean containsModuleFunction(String path);

    String[] getModuleFunctionPaths();
}
