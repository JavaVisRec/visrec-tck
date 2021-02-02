package jsr381.tck.spi;

import javax.visrec.ImageFactory;
import javax.visrec.ml.classification.NeuralNetBinaryClassifier;
import javax.visrec.ml.classification.NeuralNetImageClassifier;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

public interface JSR381Configuration {

    NeuralNetImageClassifier.BuildingBlock<BufferedImage> getABImageClassificationBuildingBlock(NeuralNetImageClassifier.Builder<BufferedImage> builder);

    NeuralNetImageClassifier.Builder<BufferedImage> getABImageClassificationBuilder(NeuralNetImageClassifier.Builder<BufferedImage> builder);

    NeuralNetBinaryClassifier.BuildingBlock<float[]> getSpamBinaryClassificationBuildingBlock();

    NeuralNetBinaryClassifier.Builder<float[]> getSpamBinaryClassificationBuilder(NeuralNetBinaryClassifier.Builder<float[]> builder);

    List<ImageFactory<?>> getImageFactories();

    static JSR381Configuration Load() {
        ServiceLoader<JSR381Configuration> configurationServiceLoader = ServiceLoader.load(JSR381Configuration.class);
        Optional<JSR381Configuration> configuration = configurationServiceLoader.findFirst();
        if (configuration.isEmpty())
            throw new IllegalStateException("No JSR381Configuration implementations are found");
        return configuration.get();
    }

}
