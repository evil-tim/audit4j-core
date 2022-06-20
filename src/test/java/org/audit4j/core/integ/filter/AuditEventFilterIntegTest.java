package org.audit4j.core.integ.filter;

import java.util.concurrent.TimeUnit;

import org.audit4j.core.AuditManager;
import org.audit4j.core.Configuration;
import org.audit4j.core.IAuditManager;
import org.audit4j.core.dto.AuditEvent;
import org.audit4j.core.integ.IntegTestBase;
import org.audit4j.core.mock.AuditEventFilterMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuditEventFilterIntegTest extends IntegTestBase {

    @Override
    @Before
    public void before() {
        Configuration config = getDefaultConfiguration();
        config.addFilter(new AuditEventFilterMock());
        AuditManager.startWithConfiguration(config);
    }

    @Test
    public void testFilter_filter() throws InterruptedException {
        IAuditManager manager = AuditManager.getInstance();
        AuditEvent event = getSampleAuditEvent();
        event.setAction("Filter");
        manager.audit(event);

        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void testFilter_not_filter() throws InterruptedException {
        IAuditManager manager = AuditManager.getInstance();
        AuditEvent event = getSampleAuditEvent();
        event.setAction("NOTFilter");
        manager.audit(event);

        TimeUnit.SECONDS.sleep(2);
    }

    @Override
    @After
    public void after() {
        AuditManager.shutdown();
    }
}
