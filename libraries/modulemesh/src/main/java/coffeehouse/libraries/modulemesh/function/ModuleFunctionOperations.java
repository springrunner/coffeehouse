package coffeehouse.libraries.modulemesh.function;

/**
 * @author springrunner.kr@gmail.com
 */
public interface ModuleFunctionOperations {

    <T, R> R execute(String path, T parameters, Class<R> returnType);
}
