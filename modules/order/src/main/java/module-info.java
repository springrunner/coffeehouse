module coffeehouse.modules.order {
    requires coffeehouse.libraries.base;
    requires coffeehouse.libraries.security;
    requires coffeehouse.libraries.spring.extensions;
    requires coffeehouse.modules.user;
    requires coffeehouse.modules.catalog;

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

    exports coffeehouse.modules.order.domain;
    exports coffeehouse.modules.order.domain.service;
}
