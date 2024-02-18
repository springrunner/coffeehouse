module coffeehouse.libraries.security {
    requires jakarta.annotation;
    requires jakarta.servlet;
    requires org.slf4j;
    
    requires spring.core;
    requires spring.context;
    requires spring.web;
    requires spring.webmvc;
    requires spring.jcl;
    requires com.fasterxml.jackson.databind;

    exports coffeehouse.libraries.security;
    exports coffeehouse.libraries.security.authentication;
    exports coffeehouse.libraries.security.web.context;
    exports coffeehouse.libraries.security.web.filter;
    exports coffeehouse.libraries.security.web.servlet;
}
