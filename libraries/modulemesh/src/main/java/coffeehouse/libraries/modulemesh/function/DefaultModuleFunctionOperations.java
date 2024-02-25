package coffeehouse.libraries.modulemesh.function;

import coffeehouse.libraries.modulemesh.mapper.ObjectMapper;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public class DefaultModuleFunctionOperations implements ModuleFunctionOperations {

    private final ModuleFunctionRegistry moduleFunctionRegistry;
    private final ObjectMapper objectMapper;

    public DefaultModuleFunctionOperations(ModuleFunctionRegistry moduleFunctionRegistry, ObjectMapper objectMapper) {
        this.moduleFunctionRegistry = Objects.requireNonNull(moduleFunctionRegistry, "moduleFunctionRegistry must not be null");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, R> R execute(String path, T parameters, Class<R> returnType) {
        var moduleFunction = moduleFunctionRegistry.<T, Object>getModuleFunction(path);

        Object result;
        if (moduleFunction.getInputType().isAssignableFrom(parameters.getClass())) {
            result = moduleFunction.apply(parameters);
        } else {
            result = moduleFunction.apply(objectMapper.map(parameters, moduleFunction.getInputType()));
        }

        if (Objects.isNull(result)) {
            return null;
        } else if (returnType.isAssignableFrom(result.getClass())) {
            return (R) result;
        }
        return objectMapper.map(result, returnType);
    }
}
