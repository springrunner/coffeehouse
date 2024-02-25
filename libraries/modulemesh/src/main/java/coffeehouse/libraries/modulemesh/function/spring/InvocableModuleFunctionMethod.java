package coffeehouse.libraries.modulemesh.function.spring;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author springrunner.kr@gmail.com
 */
class InvocableModuleFunctionMethod {

    private final Object bean;
    private final Class<?> beanType;
    private final Method method;
    private final String path;

    public InvocableModuleFunctionMethod(Object bean, Class<?> beanType, Method method) {
        this.bean = Objects.requireNonNull(bean);
        this.beanType = Objects.requireNonNull(beanType);
        this.method = Objects.requireNonNull(method);
        this.path = resolveModuleFunctionPath(beanType, method);
    }

    public Object getBean() {
        return bean;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public Method getMethod() {
        return method;
    }

    public MethodParameter[] getMethodParameters() {
        return IntStream.range(0, method.getParameterCount())
                .mapToObj(idx -> new MethodParameter(method, idx))
                .toArray(MethodParameter[]::new);
    }

    public MethodParameter getReturnType() {
        return new MethodParameter(method, -1);
    }

    public String getPath() {
        return path;
    }

    public Object invoke(Object[] args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(bean, args);
    }

    private String resolveModuleFunctionPath(Class<?> serviceType, Method method) {
        var mapping = Objects.requireNonNull(AnnotationUtils.getAnnotation(method, ModuleFunctionMapping.class));
        var customPath = mapping.path();

        if (Objects.isNull(customPath) || customPath.trim().isEmpty()) {
            var beanName = decapitalize(serviceType.getSimpleName());
            var functionName = decapitalize(method.getName());
            return beanName + "/" + functionName;
        }
        return customPath;
    }

    private String decapitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}
