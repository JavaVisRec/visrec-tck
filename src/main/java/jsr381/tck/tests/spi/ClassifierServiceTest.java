package jsr381.tck.tests.spi;

import jsr381.tck.spi.JSR381Configuration;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.visrec.ml.classification.NeuralNetImageClassifier;
import javax.visrec.ml.model.ModelCreationException;
import javax.visrec.spi.ClassifierFactoryService;
import javax.visrec.spi.ServiceProvider;
import java.awt.image.BufferedImage;

import static org.testng.Assert.assertNotNull;

@SpecVersion(spec = "JSR 381", version = "1.0.0")
public class ClassifierServiceTest {

    private JSR381Configuration config;

    @BeforeTest
    public void setUp() {
        config = JSR381Configuration.Load();
    }

    @Test(description = "4.2.1 Must use the ImageClassifier.BuildingBlock to return an implemented and trained classification model as ImageClassifier.")
    @SpecAssertion(section = "4.2.1", id = "421-D1")
    public void testCreateImageClassifier() throws ModelCreationException {
        ServiceProvider serviceProvider = ServiceProvider.current();
        ClassifierFactoryService classifierFactoryService = serviceProvider.getClassifierFactoryService();
        assertNotNull(classifierFactoryService);
        classifierFactoryService.createNeuralNetImageClassifier(config.getABImageClassificationBuildingBlock(
                config.getABImageClassificationBuilder(NeuralNetImageClassifier.builder()
                        .inputClass(BufferedImage.class)
                        .imageHeight(28)
                        .imageWidth(28)
                        .maxError(0.4f)
                        .maxEpochs(100)
                        .learningRate(0.01f))
        ));
    }

    @Test(description = "Must use the BinaryClassifier.BuildingBlock to return an implemented and trained classification model as BinaryClassifier.")
    @SpecAssertion(section = "4.2.1", id = "421-D2")
    public void testCreateBinaryClassifier() throws ModelCreationException {
        ServiceProvider serviceProvider = ServiceProvider.current();
        ClassifierFactoryService classifierFactoryService = serviceProvider.getClassifierFactoryService();
        assertNotNull(classifierFactoryService);
        classifierFactoryService.createNeuralNetBinaryClassifier(config.getSpamBinaryClassificationBuildingBlock());
    }

}
