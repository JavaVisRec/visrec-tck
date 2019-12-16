package jsr381.tck.tests;

import jsr381.tck.spi.JSR381Configuration;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.visrec.ml.ClassificationException;
import javax.visrec.ml.ClassifierCreationException;
import javax.visrec.ml.classification.ImageClassifier;
import javax.visrec.spi.ServiceProvider;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * @author Kevin Berendsen
 */
@SpecVersion(spec = "JSR 381", version = "1.0.0")
public class ClassificationTest {

    private JSR381Configuration config;

    @BeforeTest
    public void setUp() {
        config = JSR381Configuration.Load();
    }

    @Test(description = "4.2.2.1 Create an ImageClassifier using the building blocks in the builder design pattern.")
    @SpecAssertion(section = "4.2.2.1", id = "4221-A1")
    public void testBuildWithBuildingBlock() throws ClassifierCreationException {
        ImageClassifier imageClassifier = config.getABImageClassificationBuilder(ImageClassifier.builder()
                .imageHeight(28)
                .imageWidth(28)
                .maxError(0.4f)
                .maxEpochs(100)
                .learningRate(0.01f))
                .build();
        assertNotNull(imageClassifier);
    }

    @Test(description = "4.2.2.1 Create an ImageClassifier using a Map of configuration key-value which reflect to the methods of the building blocks.")
    @SpecAssertion(section = "4.2.2.1", id = "4221-A2")
    public void testBuildWithConfigMap() throws ClassifierCreationException {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("imageHeight", 28);
        configMap.put("imageWidth", 28);
        configMap.put("maxError", 0.4f);
        configMap.put("maxEpochs", 100);
        configMap.put("learningRate", 0.01f);
        ImageClassifier imageClassifier = ImageClassifier.builder().build(config.getABImageClassificationConfigMap(configMap));
        assertNotNull(imageClassifier);
    }

    @Test(description = "4.2.2.1 Creating an ImageClassifier using the Map of configuration key-value which contain invalid value types corresponding to the key. It must throw a ClassifierCreationException.")
    @SpecAssertion(section = "4.2.2.1", id = "4221-A3")
    public void testBuildWithconfigMapInvalidValueType() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("imageHeight", 0.28f); // normally imageHeight is int.
        try {
            ImageClassifier imageClassifier = ImageClassifier.builder().build(configMap);
            fail("Should have thrown ClassifierCreationException due to value type mismatch");
        } catch (ClassifierCreationException e) {}
    }

    @Test(description = "4.2.2.1 Use a created ImageClassifier to classify MNIST and verify the output formation (not the accuracy). The key must be the label and the value must be the float of accuracy.")
    @SpecAssertion(section = "4.2.2.1", id = "4221-B1")
    public void testClassifyPartialMNIST() throws ClassifierCreationException, ClassificationException {
        ImageClassifier imageClassifier = config.getABImageClassificationBuilder(ImageClassifier.builder()
                .imageHeight(28)
                .imageWidth(28)
                .maxError(0.4f)
                .maxEpochs(100)
                .learningRate(0.01f))
                .build();
        Map<String, Float> result = imageClassifier.classify(new File(getOnePNGFromResources().getFile()));
        assertEquals(2, result.entrySet().size());
        Float oneAccuracy = result.get("1");
        assertTrue(oneAccuracy > 0 && oneAccuracy < 1);
        Float twoAccuracy = result.get("2");
        assertTrue(twoAccuracy > 0 && twoAccuracy < 1);
    }

    @Test(description = "4.2.2.1 Classify input using a InputStream object as input for the ImageClassifier.")
    @SpecAssertion(section = "4.2.2.1", id = "4221-B2")
    public void testClassifyWithInputStreamAsInput() throws ClassifierCreationException, ClassificationException, FileNotFoundException {
        ImageClassifier imageClassifier = config.getABImageClassificationBuilder(ImageClassifier.builder()
                .imageHeight(28)
                .imageWidth(28)
                .maxError(0.4f)
                .maxEpochs(100)
                .learningRate(0.01f))
                .build();
        Map<String, Float> result = imageClassifier.classify(
                new FileInputStream(new File(getOnePNGFromResources().getFile())));
        assertEquals(2, result.entrySet().size());
    }

    @Test(description = "4.2.2.1 Classify input using a BufferedImage object as input for the ImageClassifier.")
    @SpecAssertion(section = "4.2.2.1", id = "4221-B3")
    public void testClassifyWithBufferedImageAsInput() throws ClassifierCreationException, ClassificationException, IOException {
        ImageClassifier imageClassifier = config.getABImageClassificationBuilder(ImageClassifier.builder()
                .imageHeight(28)
                .imageWidth(28)
                .maxError(0.4f)
                .maxEpochs(100)
                .learningRate(0.01f))
                .build();

        BufferedImage image = ServiceProvider.current()
                .getImageFactoryService()
                .getByImageType(BufferedImage.class)
                .get()
                .getImage(getOnePNGFromResources());

        Map<String, Float> result = imageClassifier.classify(image);
        assertEquals(2, result.entrySet().size());
    }

    @Test(description = "4.2.2.1 Attempt to classify input which is not an image and can't be transformed to a BufferedImage using a File object as input. It must throw a ClassificationException.")
    @SpecAssertion(section = "4.2.2.1", id = "4221-B4")
    public void testClassifyWithInvalidInputAsInputStreamInput() throws ClassifierCreationException, IOException {
        ImageClassifier imageClassifier = config.getABImageClassificationBuilder(ImageClassifier.builder()
                .imageHeight(28)
                .imageWidth(28)
                .maxError(0.4f)
                .maxEpochs(100)
                .learningRate(0.01f))
                .build();

        try {
            imageClassifier.classify(new FileInputStream(getRandomTXTFromResources()));
            fail("Classify() should fail due to invalid input type (txt file wrapped in an InputStream object)");
        } catch (ClassificationException e) {}
    }

    @Test(description = "4.2.2.1 Attempt to classify input which is not an image and can't be transformed to a BufferedImage using a InputStream object as input. It must throw a ClassificationException.")
    @SpecAssertion(section = "4.2.2.1", id = "4221-B5")
    public void testClassifyWithInvalidInputAsFileInput() throws ClassifierCreationException, ClassificationException, IOException {
        ImageClassifier imageClassifier = config.getABImageClassificationBuilder(ImageClassifier.builder()
                .imageHeight(28)
                .imageWidth(28)
                .maxError(0.4f)
                .maxEpochs(100)
                .learningRate(0.01f))
                .build();

        try {
            imageClassifier.classify(getRandomTXTFromResources());
            fail("Classify() should fail due to invalid input type (txt file wrapped in an File object)");
        } catch (ClassificationException e) {}
    }

    private URL getOnePNGFromResources() {
        URL url = ClassificationTest.class.getClassLoader().getResource("1.png");
        if (url == null)
            throw new IllegalStateException("Unable to find 1.png in resources.");
        return url;
    }

    private File getRandomTXTFromResources() {
        URL url = ClassificationTest.class.getClassLoader().getResource("random.txt");
        if (url == null)
            throw new IllegalStateException("Unable to find random.txt in resources.");
        return new File(url.getFile());
    }
}
