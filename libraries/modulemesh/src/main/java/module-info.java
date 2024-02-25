module coffeehouse.libraries.modulemesh {
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires com.fasterxml.jackson.databind;

    exports coffeehouse.libraries.modulemesh;
    exports coffeehouse.libraries.modulemesh.function;
    exports coffeehouse.libraries.modulemesh.mapper;
}
