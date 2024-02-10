package coffeehouse.libraries.security.converter;

import coffeehouse.libraries.security.PlainToken;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @author springrunner.kr@gmail.com
 */
public class JacksonSecurityModule extends SimpleModule {

    public JacksonSecurityModule() {
        super("coffeehouse.libraries.security");
        addSerializer(PlainToken.class, new StdSerializer<>(PlainToken.class) {
            @Override
            public void serialize(PlainToken plainToken, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(plainToken.toString());
            }
        });
    }
}
