package jsr381.tck.spi;

import javax.visrec.ml.classification.BinaryClassifier;
import javax.visrec.ml.classification.ImageClassifier;
import java.util.Optional;
import java.util.ServiceLoader;

public interface JSR381Configuration {

    ImageClassifier.BuildingBlock getABImageClassificationBuildingBlock();

    BinaryClassifier.BuildingBlock getSpamBinaryClassificationBuildingBlock();

    static JSR381Configuration Load() {
        ServiceLoader<JSR381Configuration> configurationServiceLoader = ServiceLoader.load(JSR381Configuration.class);
        Optional<JSR381Configuration> configuration = configurationServiceLoader.findFirst();
        if (configuration.isEmpty())
            throw new IllegalStateException("No JSR381Configuration implementations are found");
        return configuration.get();
    }

}