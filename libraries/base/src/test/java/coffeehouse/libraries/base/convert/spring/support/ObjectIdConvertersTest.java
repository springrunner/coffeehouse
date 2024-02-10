package coffeehouse.libraries.base.convert.spring.support;

import coffeehouse.libraries.base.lang.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * @author springrunner.kr@gmail.com
 */
class ObjectIdConvertersTest {

    static final DefaultConversionService conversionService = new DefaultConversionService();

    @BeforeAll
    static void registerObjectIdConverters() {
        conversionService.addConverter(ObjectIdConverters.objectIdToString(StringObjectId.class));
        conversionService.addConverter(ObjectIdConverters.stringToObjectId(StringObjectId.class));
        conversionService.addConverter(ObjectIdConverters.objectIdToString(IntegerObjectId.class));
        conversionService.addConverter(ObjectIdConverters.stringToObjectId(IntegerObjectId.class));
    }

    @Test
    void convertObjectIdToString() {
        Assertions.assertEquals(
                "springrunner",
                conversionService.convert(new StringObjectId("springrunner"), String.class)
        );

        Assertions.assertEquals(
                "2024",
                conversionService.convert(new IntegerObjectId(2024), String.class)
        );
    }

    @Test
    void convertStringToObjectId() {
        Assertions.assertEquals(
                new StringObjectId("springrunner"),
                conversionService.convert("springrunner", StringObjectId.class)
        );

        Assertions.assertEquals(
                new IntegerObjectId(2024),
                conversionService.convert("2024", IntegerObjectId.class)
        );
    }

    static class StringObjectId extends ObjectId<String> {
        public StringObjectId(String value) {
            super(value);
        }
    }

    static class IntegerObjectId extends ObjectId<Integer> {
        public IntegerObjectId(Integer value) {
            super(value);
        }
    }
}
