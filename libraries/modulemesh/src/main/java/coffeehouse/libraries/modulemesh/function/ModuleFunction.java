package coffeehouse.libraries.modulemesh.function;

import java.util.function.Function;

/**
 * @author springrunner.kr@gmail.com
 */
public interface ModuleFunction<T, R> extends Function<T, R> {
    
    String getPath();
    
    Class<T> getInputType();
    
    Class<R> getResultType();
}
