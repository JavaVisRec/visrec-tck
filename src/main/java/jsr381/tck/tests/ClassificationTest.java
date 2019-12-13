package jsr381.tck.tests;

import jsr381.tck.spi.JSR381Configuration;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.visrec.ml.ClassifierCreationException;
import javax.visrec.ml.classification.ImageClassifier;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertNotNull;
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

    @Test(description = "4.2.2.1 Bad case: creating an ImageClassifier using the Map of configuration key-value which contain invalid keys.")
    @SpecAssertion(section = "4.2.2.1", id = "4221-A3")
    public void testBuildWithConfigMapInvalidKey() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("does_not_exist", "");
        try {
            ImageClassifier imageClassifier = ImageClassifier.builder().build(configMap);
            fail("Should have thrown ClassifierCreationException due to unknown key");
        } catch (ClassifierCreationException e) {
        }
    }

    @Test(description = "4.2.2.1 Bad case: creating an ImageClassifier using the Map of configuration key-value which contain invalid value types corresponding to the key. It must throw a ClassifierCreationException.")
    @SpecAssertion(section = "4.2.2.1", id = "4221-A4")
    public void testBuildWithconfigMapInvalidValueType() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("imageHeight", 0.28f); // normally imageHeight is int.
        try {
            ImageClassifier imageClassifier = ImageClassifier.builder().build(configMap);
            fail("Should have thrown ClassifierCreationException due to value type mismatch");
        } catch (ClassifierCreationException e) {}
    }

}
