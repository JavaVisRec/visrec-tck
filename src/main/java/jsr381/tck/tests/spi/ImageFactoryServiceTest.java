package jsr381.tck.tests.spi;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import javax.visrec.ImageFactory;
import javax.visrec.spi.ImageFactoryService;
import javax.visrec.spi.ServiceProvider;
import java.awt.image.BufferedImage;
import java.util.Optional;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@SpecVersion(spec = "JSR 381", version = "1.0.0")
public class ImageFactoryServiceTest {

    @Test(description = "4.2.1 Must be able to return an Optional after calling getByImageType(...)")
    @SpecAssertion(section = "4.2.1", id = "421-E1")
    public void testGetOptionalImageFactory() {
        ServiceProvider serviceProvider = ServiceProvider.current();
        ImageFactoryService service = serviceProvider.getImageFactoryService();
        assertNotNull(service);

        Optional<ImageFactory<Object>> imageFactory = service.getByImageType(Object.class);
        assertNotNull(imageFactory);
        assertTrue(imageFactory.isEmpty());
    }

    @Test(description = "4.2.1 Must be able to return an implementation of the ImageFactory that is able to handle type of BufferedImage.")
    @SpecAssertion(section = "4.2.1", id = "421-E2")
    public void testGetBufferedImageImageFactory() {
        ServiceProvider serviceProvider = ServiceProvider.current();
        ImageFactoryService service = serviceProvider.getImageFactoryService();
        assertNotNull(service);

        Optional<ImageFactory<BufferedImage>> imageFactory = service.getByImageType(BufferedImage.class);
        assertNotNull(imageFactory);
        assertTrue(imageFactory.isPresent());
    }
}
