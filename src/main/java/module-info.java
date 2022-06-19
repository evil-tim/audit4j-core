module audit4j.core {
    requires cdi.api;
    requires commons.lang3;
    requires commons.net;
    requires fastjson;
    requires gs.collections;
    requires gs.collections.api;
    requires jmte;
    requires java.management;
    requires java.sql;
    requires javassist;
    requires javax.interceptor.api;
    requires javax.enterprise.concurrent.api;
    requires javax.servlet.api;
    requires org.apache.commons.io;
    requires reactor.core;
    requires slf4j.api;
    requires xstream;
    requires yamlbeans;

    exports org.audit4j.core;
    exports org.audit4j.core.exception;
    exports org.audit4j.core.filter;
    exports org.audit4j.core.handler;
    exports org.audit4j.core.jmx;
    exports org.audit4j.core.layout;
    exports org.audit4j.core.util.annotation;
}
