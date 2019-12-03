package jsr381.tck.tests.spi;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import javax.visrec.spi.ImplementationService;
import javax.visrec.spi.ServiceProvider;

import static org.testng.Assert.*;

@SpecVersion(spec = "JSR 381", version = "1.0.0")
public class ImplementationServiceTest {

    @Test(description = "4.2.1 Must be able to return the name of the implementation which may not be empty nor null.")
    @SpecAssertion(section = "4.2.1", id = "421-F1")
    public void testGetName() {
        try {
            ServiceProvider serviceProvider = ServiceProvider.current();
            ImplementationService implementationService = serviceProvider.getImplementationService();
            assertNotNull(implementationService);
            assertNotNull(implementationService.getName());
            assertNotEquals( "", implementationService.getName().trim());
        } catch (IllegalStateException e) {
            fail(e.getMessage());
        }
    }

    @Test(description = "4.2.1 Must be able to return the version of the implementation which may not be empty nor null.")
    @SpecAssertion(section = "4.2.1", id = "421-F2")
    public void testGetVersion() {
        try {
            ServiceProvider serviceProvider = ServiceProvider.current();
            ImplementationService implementationService = serviceProvider.getImplementationService();
            assertNotNull(implementationService);
            assertNotNull(implementationService.getVersion());
            assertNotEquals( "", implementationService.getVersion().trim());
        } catch (IllegalStateException e) {
            fail(e.getMessage());
        }
    }
}
