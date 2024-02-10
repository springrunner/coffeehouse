package coffeehouse.libraries.base.serialize.jackson;

import coffeehouse.libraries.base.lang.ObjectId;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectIdModuleTest {

    final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new ObjectIdModule(StringObjectId.class, NumberObjectId.class));
    }

    @Test
    void sederStringValueType() throws JsonProcessingException {
        String objectIdValue = "springrunner";

        var serialized = objectMapper.writeValueAsString(new StringObjectId(objectIdValue));
        Assertions.assertEquals("\"springrunner\"", serialized);

        var deserialized = objectMapper.readValue(serialized, StringObjectId.class);
        Assertions.assertEquals(new StringObjectId(objectIdValue), deserialized);
    }

    @Test
    void unsupportedValueType() {
        Assertions.assertThrows(JsonGenerationException.class, () -> {
            objectMapper.writeValueAsString(new NumberObjectId(0));
        });

        Assertions.assertThrows(JsonMappingException.class, () -> {
            objectMapper.readValue("0", NumberObjectId.class);
        });
    }

    static class StringObjectId extends ObjectId<String> {
        public StringObjectId(String value) {
            super(value);
        }
    }

    static class NumberObjectId extends ObjectId<Number> {
        public NumberObjectId(Number value) {
            super(value);
        }
    }
}
