module coffeehouse.modules.brew {
    requires coffeehouse.libraries.base;
    requires coffeehouse.libraries.modulemesh;
    requires coffeehouse.libraries.spring.extensions;
    requires coffeehouse.modules.catalog;
    requires coffeehouse.modules.order;

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
    requires spring.data.relational;
    requires spring.data.jdbc;
    requires com.fasterxml.jackson.annotation;

    exports coffeehouse.modules.brew.domain;
    exports coffeehouse.modules.brew.domain.service;
}
