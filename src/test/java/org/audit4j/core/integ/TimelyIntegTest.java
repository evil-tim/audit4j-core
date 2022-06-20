package org.audit4j.core.integ;


public abstract class TimelyIntegTest extends IntegTestBase {
    
    /**
     * Before.
     */
    protected void before(Class<?> clazz){
        super.watchStart(clazz.getName());
    }
    
    /**
     * After.
     */
    @Override
    protected void after(){
        super.watchStop();
        watch.reset();
        watch = null;
    }
}
