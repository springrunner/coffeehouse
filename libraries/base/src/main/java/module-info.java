module coffeehouse.libraries.base {
    requires spring.core;
    requires spring.data.commons;
    requires com.fasterxml.jackson.databind;
    requires org.javamoney.moneta;

    exports coffeehouse.libraries.base;
    exports coffeehouse.libraries.base.convert.spring;
    exports coffeehouse.libraries.base.convert.spring.support;
    exports coffeehouse.libraries.base.crypto;
    exports coffeehouse.libraries.base.lang;
    exports coffeehouse.libraries.base.serialize.jackson;
}
