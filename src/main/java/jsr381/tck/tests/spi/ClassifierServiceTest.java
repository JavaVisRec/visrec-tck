package jsr381.tck.tests.spi;

import jsr381.tck.tests.AbImageClassificationUtil;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import javax.visrec.ml.ClassifierCreationException;
import javax.visrec.spi.ClassifierService;
import javax.visrec.spi.ServiceProvider;

import static org.testng.Assert.assertNotNull;

@SpecVersion(spec = "JSR 381", version = "1.0.0")
public class ClassifierServiceTest {

    @Test(description = "4.2.1 Must use the ImageClassifier.BuildingBlock to return an implemented and trained classification model as ImageClassifier.")
    @SpecAssertion(section = "4.2.1", id = "421-D1")
    public void testCreateImageClassifier() throws ClassifierCreationException {
        ServiceProvider serviceProvider = ServiceProvider.current();
        ClassifierService classifierService = serviceProvider.getClassifierService();
        assertNotNull(classifierService);
        classifierService.createImageClassifier(AbImageClassificationUtil.getBuildingBlock());
    }

}
