package coffeehouse.libraries.modulemesh.mapper;

/**
 * @author springrunner.kr@gmail.com
 */
public interface ObjectMapper {

    <T> T map(Object source, Class<T> targetType);
}
