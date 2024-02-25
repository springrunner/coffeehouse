module coffeehouse.modules.catalog {
    requires coffeehouse.libraries.base;
    requires coffeehouse.libraries.modulemesh;
    requires coffeehouse.libraries.spring.extensions;

    requires java.sql;
    requires jakarta.annotation;
    requires jakarta.validation;
    requires jakarta.servlet;
    requires org.slf4j;

    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.jdbc;
    requires spring.web;
    requires spring.data.commons;
    requires spring.data.jdbc;
    requires spring.data.relational;
    requires com.fasterxml.jackson.annotation;
    requires org.javamoney.moneta;

    exports coffeehouse.modules.catalog.domain;
    exports coffeehouse.modules.catalog.domain.service;
}
