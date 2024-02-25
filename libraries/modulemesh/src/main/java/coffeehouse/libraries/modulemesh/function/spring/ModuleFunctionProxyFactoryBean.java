package coffeehouse.libraries.modulemesh.function.spring;

import coffeehouse.libraries.modulemesh.mapper.ObjectMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author springrunner.kr@gmail.com
 */
public class ModuleFunctionProxyFactoryBean<T> implements FactoryBean<T> {

    private final BeanFactory beanFactory;
    private final ObjectMapper objectMapper;
    private final Class<T> serviceInterface;

    public ModuleFunctionProxyFactoryBean(BeanFactory beanFactory, ObjectMapper objectMapper, Class<T> serviceInterface) {
        this.beanFactory = Objects.requireNonNull(beanFactory);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.serviceInterface = Objects.requireNonNull(serviceInterface);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() throws Exception {
        var classLoader = ClassUtils.getDefaultClassLoader();
        var interfaces = new Class[]{serviceInterface};
        var handler = new ModuleFunctionInvoker();

        return (T) Proxy.newProxyInstance(classLoader, interfaces, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    private class ModuleFunctionInvoker implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] providedArgs) throws Throwable {
            var moduleFunction = resolveModuleFunction(method);
            var args = mapModuleFunctionArguments(providedArgs, moduleFunction.getMethodParameters());
            var result = moduleFunction.invoke(args);

            if (method.getReturnType() == Optional.class) {
                return Optional.ofNullable(objectMapper.map(result, resolveGenericReturnType(method)));
            } else if (method.getReturnType() == moduleFunction.getReturnType().getParameterType()) {
                return result;
            }
            return objectMapper.map(result, method.getReturnType());
        }

        private InvocableModuleFunctionMethod resolveModuleFunction(Method method) {
            var moduleFunctionPath = Optional.ofNullable(method.getAnnotation(ModuleFunction.class)).map(ModuleFunction::path).orElse(null);
            if (Objects.isNull(moduleFunctionPath) || moduleFunctionPath.isEmpty()) {
                throw new IllegalStateException("module-function path is not set: " + method.getName());

            }

            return beanFactory.getBean(moduleFunctionPath, InvocableModuleFunctionMethod.class);
        }

        private Object[] mapModuleFunctionArguments(Object[] providedArgs, MethodParameter[] parameters) {
            if (Objects.isNull(providedArgs)) {
                return new Object[0];
            }

            return IntStream.range(0, providedArgs.length)
                    .mapToObj(idx -> {
                        if (providedArgs[idx].getClass() == parameters[idx].getParameterType()) {
                            return providedArgs[idx];
                        } else {
                            return objectMapper.map(providedArgs[idx], parameters[idx].getParameterType());
                        }
                    }).toArray();
        }

        private Class<?> resolveGenericReturnType(Method method) {
            try {
                var idValueType = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
                return Class.forName(idValueType.getTypeName());
            } catch (Exception error) {
                throw new IllegalStateException("Cannot resolve return type of `%s`".formatted(method.getGenericReturnType()));
            }
        }
    }
}
