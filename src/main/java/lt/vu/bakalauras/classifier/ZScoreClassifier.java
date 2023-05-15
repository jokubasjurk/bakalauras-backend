package lt.vu.bakalauras.classifier;

import lt.vu.bakalauras.model.FlightTime;
import lt.vu.bakalauras.model.TemplateData;
import lt.vu.bakalauras.service.ClassifierStatisticsService;
import org.apache.commons.lang3.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ZScoreClassifier {

    private static final String CLASSIFIER_TYPE = "zscore";
    private static final Logger logger = LoggerFactory.getLogger(ZScoreClassifier.class);
    private final ClassifierStatisticsService classifierStatisticsService;

    @Autowired
    public ZScoreClassifier(ClassifierStatisticsService classifierStatisticsService) {
        this.classifierStatisticsService = classifierStatisticsService;
    }

    public boolean classify(List<FlightTime> testSamples, Map<String, TemplateData> template, boolean isImpostor) {
        double dddSum = 0.0;
        double dduSum = 0.0;
        double dudSum = 0.0;
        int dddCount = 0;
        int dduCount = 0;
        int dudCount = 0;

        for (FlightTime testFeature : testSamples) {
            String keyCode = testFeature.getKeyPair() + testFeature.getType();
            TemplateData templateData = template.get(keyCode);

            if (templateData != null) {
                double distance = templateData.getStdDev() > 1
                        ? (testFeature.getFlightTime() - templateData.getMean()) / templateData.getStdDev()
                        : 0;

                switch (testFeature.getType()) {
                    case "DD" -> {
                        dddSum += distance;
                        dddCount++;
                    }
                    case "DU" -> {
                        dduSum += distance;
                        dduCount++;
                    }
                    case "UD" -> {
                        dudSum += distance;
                        dudCount++;
                    }
                }
            }
        }

        double ddd = dddCount > 0 ? dddSum / dddCount : 0;
        double ddu = dduCount > 0 ? dduSum / dduCount : 0;
        double dud = dudCount > 0 ? dudSum / dudCount : 0;

        // Define the thresholds for DD, DU, and UD features
        double tdd = 1;
        double tdu = 1;
        double tud = 1;

        // Check if the condition is satisfied
        // boolean authenticationResult = ddd <= tdd && ddu <= tdu && dud <= tud;
        boolean authenticationResult = Range.between(-tdd, tdd).contains(ddd)
                && Range.between(-tdu, tdu).contains(ddu)
                && Range.between(-tud, tud).contains(dud);
        logger.info(String.format("ddd: %s, ddu: %s, dud: %s", ddd, ddu, dud));
        classifierStatisticsService.calculateStatistics(CLASSIFIER_TYPE, authenticationResult, isImpostor);
        return ddd <= tdd && ddu <= tdu && dud <= tud;
    }

}
