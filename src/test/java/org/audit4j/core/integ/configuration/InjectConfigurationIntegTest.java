package org.audit4j.core.integ.configuration;

import org.audit4j.core.AuditManager;
import org.audit4j.core.Configuration;
import org.audit4j.core.integ.IntegTestBase;
import org.junit.After;
import org.junit.Test;

public class InjectConfigurationIntegTest extends IntegTestBase{

    @Test
    public void testStartWithConfiguration() {
        AuditManager.startWithConfiguration(Configuration.DEFAULT);
        AuditManager.getInstance().audit(getSampleAuditEvent());
    }

    @After
    public void after() {
        AuditManager.shutdown();
    }
}
