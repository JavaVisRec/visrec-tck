package jsr381.tck.tests;

import jsr381.tck.spi.JSR381Configuration;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.visrec.ml.classification.ClassifierCreationException;
import javax.visrec.ml.classification.ImageClassifier;
import javax.visrec.ml.classification.NeuralNetImageClassifier;
import javax.visrec.spi.ServiceProvider;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

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
        ImageClassifier<BufferedImage> imageClassifier = config.getABImageClassificationBuilder(NeuralNetImageClassifier.builder()
                .inputClass(BufferedImage.class)
                .imageHeight(28)
                .imageWidth(28)
                .maxError(0.4f)
                .maxEpochs(100)
                .learningRate(0.01f))
                .build();
        assertNotNull(imageClassifier);
    }

    @Test(description = "4.2.2.1 Use a created ImageClassifier to classify MNIST and verify the output formation (not the accuracy). The key must be the label and the value must be the float of accuracy.")
    @SpecAssertion(section = "4.2.2.1", id = "4221-B1")
    public void testClassifyPartialMNIST() throws ClassifierCreationException {
        ImageClassifier<BufferedImage> imageClassifier = config.getABImageClassificationBuilder(NeuralNetImageClassifier.builder()
                .inputClass(BufferedImage.class)
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
    public void testClassifyWithInputStreamAsInput() throws ClassifierCreationException, FileNotFoundException {
        ImageClassifier<BufferedImage> imageClassifier = config.getABImageClassificationBuilder(NeuralNetImageClassifier.builder()
                .inputClass(BufferedImage.class)
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
    public void testClassifyWithBufferedImageAsInput() throws ClassifierCreationException, IOException {
        ImageClassifier<BufferedImage> imageClassifier = config.getABImageClassificationBuilder(NeuralNetImageClassifier.builder()
                .inputClass(BufferedImage.class)
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
        ImageClassifier<BufferedImage> imageClassifier = config.getABImageClassificationBuilder(NeuralNetImageClassifier.builder()
                .inputClass(BufferedImage.class)
                .imageHeight(28)
                .imageWidth(28)
                .maxError(0.4f)
                .maxEpochs(100)
                .learningRate(0.01f))
                .build();

        try {
            imageClassifier.classify(new FileInputStream(getRandomTXTFromResources()));
            fail("Classify() should fail due to invalid input type (txt file wrapped in an InputStream object)");
        } catch (ClassificationException e) {
            // Good
        }
    }

    @Test(description = "4.2.2.1 Attempt to classify input which is not an image and can't be transformed to a BufferedImage using a InputStream object as input. It must throw a ClassificationException.")
    @SpecAssertion(section = "4.2.2.1", id = "4221-B5")
    public void testClassifyWithInvalidInputAsFileInput() throws ClassifierCreationException, ClassificationException, IOException {
        ImageClassifier<BufferedImage> imageClassifier = config.getABImageClassificationBuilder(NeuralNetImageClassifier.builder()
                .inputClass(BufferedImage.class)
                .imageHeight(28)
                .imageWidth(28)
                .maxError(0.4f)
                .maxEpochs(100)
                .learningRate(0.01f))
                .build();

        try {
            imageClassifier.classify(getRandomTXTFromResources());
            fail("Classify() should fail due to invalid input type (txt file wrapped in an File object)");
        } catch (ClassificationException e) {
            // Good
        }
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
