package jsr381.tck.tests;

import jsr381.tck.spi.JSR381Configuration;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.visrec.ImageFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import static org.testng.Assert.fail;

@SpecVersion(spec = "JSR 381", version = "1.0.0")
public class ImageFactoryTest {

    private JSR381Configuration config;

    @BeforeTest
    public void setUp() {
        config = JSR381Configuration.Load();
    }

    @Test(description = "4.2.6.1 Each implementation must be able to return non-null output using a File object as input.")
    @SpecAssertion(section = "4.2.6.1", id = "4261-A1")
    public void testGetImageWithFileAsInput() throws IOException {
        File file = new File(getRedJPGFromResources().getFile());
        for (ImageFactory<?> imageFactory : config.getImageFactories()) {
            Object obj = imageFactory.getImage(file);
            if (obj == null) {
                fail("obj == null using ImageFactory: " + imageFactory.getClass().getName());
            }
        }
    }

    @Test(description = "4.2.6.1 Each implementation must be able to return non-null output using an InputStream object as input.")
    @SpecAssertion(section = "4.2.6.1", id = "4261-A2")
    public void testGetImageWithInputStreamAsInput() throws IOException {
        File file = new File(getRedJPGFromResources().getFile());
        FileInputStream fis = new FileInputStream(file);
        for (ImageFactory<?> imageFactory : config.getImageFactories()) {
            Object obj = imageFactory.getImage(fis);
            if (obj == null) {
                fail("obj == null using ImageFactory: " + imageFactory.getClass().getName());
            }
        }
    }

    @Test(description = "4.2.6.1 Each implementation must be able to return non-null output using a URL object as input.")
    @SpecAssertion(section = "4.2.6.1", id = "4261-A3")
    public void testGetImageWithURLAsInput() throws IOException {
        URL redJPG = getRedJPGFromResources();
        for (ImageFactory<?> imageFactory : config.getImageFactories()) {
            Object obj = imageFactory.getImage(redJPG);
            if (obj == null) {
                fail("obj == null using ImageFactory: " + imageFactory.getClass().getName());
            }
        }
    }

    @Test(description = "4.2.6.1 Each implementation must throw an IOException if the File input is not an image.")
    @SpecAssertion(section = "4.2.6.1", id = "4261-A4")
    public void testGetImageWithInvalidFileInput() {
        File randomTXT = new File(getRandomTXTFromResources().getFile());
        for (ImageFactory<?> imageFactory : config.getImageFactories()) {
            try {
                imageFactory.getImage(randomTXT);
                fail(String.format("ImageFactory [%s] should have thrown an IOException", imageFactory.getClass().getName()));
            } catch (IOException e) {}
        }
    }

    @Test(description = "4.2.6.1 Each implementation must throw an IOException if the InputStream input is not an image.")
    @SpecAssertion(section = "4.2.6.1", id = "4261-A5")
    public void testGetImageWithInvalidInputStreamInput() throws IOException {
        FileInputStream randomTXT = new FileInputStream(new File(getRandomTXTFromResources().getFile()));
        for (ImageFactory<?> imageFactory : config.getImageFactories()) {
            try {
                imageFactory.getImage(randomTXT);
                fail(String.format("ImageFactory [%s] should have thrown an IOException", imageFactory.getClass().getName()));
            } catch (IOException e) {}
        }
    }

    @Test(description = "4.2.6.1 Each implementation must throw an IOException if the URL input is not an image.")
    @SpecAssertion(section = "4.2.6.1", id = "4261-A6")
    public void testGetImageWithInvalidURLInput() throws IOException {
        URL randomTXT = getRandomTXTFromResources();
        for (ImageFactory<?> imageFactory : config.getImageFactories()) {
            try {
                imageFactory.getImage(randomTXT);
                fail(String.format("ImageFactory [%s] should have thrown an IOException", imageFactory.getClass().getName()));
            } catch (IOException e) {}
        }
    }

    private URL getRedJPGFromResources() {
        URL url = ImageFactoryTest.class.getClassLoader().getResource("red.jpg");
        if (url == null)
            throw new IllegalStateException("Unable to find red.jpg in resources.");
        return url;
    }

    private URL getRandomTXTFromResources() {
        URL url = ImageFactoryTest.class.getClassLoader().getResource("random.txt");
        if (url == null)
            throw new IllegalStateException("Unable to find random.txt in resources.");
        return url;
    }
}
