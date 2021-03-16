package jsr381.tck.tests;

import jsr381.tck.spi.JSR381Configuration;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.visrec.ml.classification.BinaryClassifier;
import javax.visrec.ml.classification.NeuralNetBinaryClassifier;
import javax.visrec.ml.model.InvalidConfigurationException;
import javax.visrec.ml.model.ModelCreationException;
import java.util.Map;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

/**
 * @author Kevin Berendsen
 */
@SpecVersion(spec = "JSR 381", version = "1.0.0")
public class BinaryClassificationTest {

    private JSR381Configuration config;

    @BeforeTest
    public void setUp() {
        config = JSR381Configuration.Load();
    }

    @Test(description = "4.2.2.2 Create an BinaryClassifier using the building blocks in the builder design pattern.")
    @SpecAssertion(section = "4.2.2.2", id = "4222-A1")
    public void testBuildWithBuildingBlock() throws ModelCreationException {
        BinaryClassifier<float[]> classifier = config.getSpamBinaryClassificationBuilder(NeuralNetBinaryClassifier.builder()
                .inputClass(float[].class)
                .inputsNum(57)
                .hiddenLayers(30, 15)
                .maxError(0.03f)
                .maxEpochs(15000)
                .learningRate(0.01f))
                .build();
        assertNotNull(classifier);
    }

    @Test(description = "4.2.2.2 Create an BinaryClassifier using a Map of configuration key-value which reflect to the methods of the building blocks.")
    @SpecAssertion(section = "4.2.2.2", id = "4222-A2")
    public void testBuildWithMapConfiguration() throws ModelCreationException {
        Map<String, Object> configuration = Map.of(
                "inputClass", float[].class,
                "inputsNum", 57,
                "hiddenLayers", new int[]{30, 15},
                "maxError", 0.03f,
                "maxEpochs", 15000,
                "learningRate", 0.01f
        );
        BinaryClassifier<float[]> classifier = config.getSpamBinaryClassificationBuilder(NeuralNetBinaryClassifier.builder().inputClass(float[].class))
                .build(configuration);
        assertNotNull(classifier);
    }

    @Test(description = "4.2.2.2 Creating an BinaryClassifier using the Map of configuration key-value which contain invalid value types corresponding to the key. It must throw a InvalidBuilderConfigurationException.")
    @SpecAssertion(section = "4.2.2.2", id = "4222-A3")
    public void testBuildWithMapConfigurationWithInvalidValueTypes() throws ModelCreationException {
        Map<String, Object> configuration = Map.of(
                "inputClass", 1
        );
        try {
            config.getSpamBinaryClassificationBuilder(NeuralNetBinaryClassifier.builder().inputClass(float[].class))
                    .build(configuration);
            fail("Build() should result into a InvalidBuilderConfigurationException");
        } catch (InvalidConfigurationException e) {
            // Good
        }
    }

    @Test(description = "4.2.2.2 Use a created BinaryClassifier to classify SPAM and verify the output. The output must either return true or false.")
    @SpecAssertion(section = "4.2.2.2", id = "4222-B1")
    public void testClassifySpam() throws ModelCreationException {
        BinaryClassifier<float[]> classifier = config.getSpamBinaryClassificationBuilder(NeuralNetBinaryClassifier.builder()
                .inputClass(float[].class)
                .inputsNum(57)
                .hiddenLayers(30, 15)
                .maxError(0.03f)
                .maxEpochs(15000)
                .learningRate(0.01f))
                .build();

        float[] input = new float[57];
        input[56] = 1;
        Float result = classifier.classify(input);
        assertNotNull(result);
    }
}
