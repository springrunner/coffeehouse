module coffeehouse.libraries.modulemesh {
    requires java.sql;

    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.jdbc;
    requires spring.tx;
    requires spring.integration.core;
    requires spring.amqp;
    requires spring.rabbit;
    requires com.fasterxml.jackson.databind;
    requires org.slf4j;

    exports coffeehouse.libraries.modulemesh;
    exports coffeehouse.libraries.modulemesh.event;
    exports coffeehouse.libraries.modulemesh.event.inbox;
    exports coffeehouse.libraries.modulemesh.event.outbox;
    exports coffeehouse.libraries.modulemesh.event.serializer;
    exports coffeehouse.libraries.modulemesh.event.spring.jdbc;
    exports coffeehouse.libraries.modulemesh.function;
    exports coffeehouse.libraries.modulemesh.mapper;
}
