package jsr381.tck.tests.spi;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import javax.visrec.spi.BuilderService;
import javax.visrec.spi.ClassifierService;
import javax.visrec.spi.ImageFactoryService;
import javax.visrec.spi.ServiceProvider;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

/**
 * @author Kevin Berendsen
 */
@SpecVersion(spec = "JSR 381", version = "1.0.0")
public class ServiceProviderTest {

    /**
     * Obtain the current service provider
     */
    @Test(description = "4.2.1 Ensure there's a minimum of a single implementation of the Service Provider.")
    @SpecAssertion(section = "4.2.1", id = "421-A1")
    public void testGetServiceProviderImpl() {
        try {
            final ServiceProvider serviceProvider = ServiceProvider.current();
            assertNotNull(serviceProvider);
        } catch (IllegalStateException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Obtain the BuilderService from the service provider
     */
    @Test(description = "4.2.1 Obtain the implementation of BuilderService through the Service Provider.")
    @SpecAssertion(section = "4.2.1", id = "421-B1")
    public void testGetBuilderService() {
        try {
            final ServiceProvider serviceProvider = ServiceProvider.current();
            final BuilderService service = serviceProvider.getBuilderService();
            assertNotNull(service);
        } catch (IllegalStateException | UnsupportedOperationException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Obtain the ClassifierService from the service provider
     */
    @Test(description = "4.2.1 Obtain the implementation of ClassifierService through the Service Provider.")
    @SpecAssertion(section = "4.2.1", id = "421-B2")
    public void testGetClassifierService() {
        try {
            final ServiceProvider serviceProvider = ServiceProvider.current();
            final ClassifierService service = serviceProvider.getClassifierService();
            assertNotNull(service);
        } catch (IllegalStateException | UnsupportedOperationException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Obtain the ImageFactoryService from the service provider
     */
    @Test(description = "4.2.1 Obtain the implementation of ImageFactoryService through the Service Provider.")
    @SpecAssertion(section = "4.2.1", id = "421-B3")
    public void testGetImageFactoryService() {
        try {
            final ServiceProvider serviceProvider = ServiceProvider.current();
            final ImageFactoryService service = serviceProvider.getImageFactoryService();
            assertNotNull(service);
        } catch (IllegalStateException | UnsupportedOperationException e) {
            fail(e.getMessage());
        }
    }

}
