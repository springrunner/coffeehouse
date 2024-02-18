module coffeehouse.libraries.spring.extensions {
    requires org.slf4j;
    requires spring.core;
    requires spring.beans;
    requires spring.aop;
    requires spring.context;
    
    exports coffeehouse.libraries.spring.beans.factory.annotation;
    exports coffeehouse.libraries.spring.beans.factory.config;
    exports coffeehouse.libraries.spring.beans.factory.support;
    exports coffeehouse.libraries.spring.context.annotation;
}
