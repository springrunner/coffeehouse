module coffeehouse.modules.user {
    requires coffeehouse.libraries.base;
    requires coffeehouse.libraries.security;
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
    requires spring.data.relational;
    requires com.fasterxml.jackson.annotation;
    requires spring.data.jdbc;

    exports coffeehouse.modules.user.domain;
    exports coffeehouse.modules.user.domain.service;
}
