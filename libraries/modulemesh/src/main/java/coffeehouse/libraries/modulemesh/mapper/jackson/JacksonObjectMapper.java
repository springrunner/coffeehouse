package coffeehouse.libraries.modulemesh.mapper.jackson;

import coffeehouse.libraries.modulemesh.mapper.ObjectMapper;

import java.util.Objects;

/**
 * @author springrunner.kr@gmail.com
 */
public class JacksonObjectMapper implements ObjectMapper {

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    public JacksonObjectMapper(com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper) {
        this.objectMapper = Objects.requireNonNull(jacksonObjectMapper, "JacksonObjectMapper must not be null");
    }

    @Override
    public <T> T map(Object source, Class<T> targetType) {
        if (Objects.isNull(source)) {
            return null;
        }
        return objectMapper.convertValue(source, targetType);
    }
}
