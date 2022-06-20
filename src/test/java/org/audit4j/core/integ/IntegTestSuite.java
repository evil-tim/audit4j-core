package org.audit4j.core.integ;

import org.audit4j.core.integ.configuration.InjectConfigurationIntegTest;
import org.audit4j.core.integ.configuration.URLConfigurationIntegTest;
import org.audit4j.core.integ.event.annotation.ClassAnnotationTest;
import org.audit4j.core.integ.event.annotation.DeIdentifyAnnotationTest;
import org.audit4j.core.integ.event.annotation.MethodAnnotationTest;
import org.audit4j.core.integ.filter.AuditEventFilterIntegTest;
import org.audit4j.core.integ.option.ScanAnnotationOptionIntegTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ MethodAnnotationTest.class, ClassAnnotationTest.class, DeIdentifyAnnotationTest.class,
        AuditEventFilterIntegTest.class, ScanAnnotationOptionIntegTest.class, InjectConfigurationIntegTest.class,
        URLConfigurationIntegTest.class })
public class IntegTestSuite {

}
