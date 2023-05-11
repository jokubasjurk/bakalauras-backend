package lt.vu.bakalauras.service;

import lt.vu.bakalauras.model.FlightTime;
import lt.vu.bakalauras.model.TemplateData;

import java.util.List;
import java.util.Map;

public class Classifier {

    private static final double THRESHOLD = 0.5;

    public static boolean classify(List<FlightTime> testSamples, Map<String, TemplateData> template) {
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
                double distance = templateData.getStdDev() != 0
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
        double tdd = 0.5;
        double tdu = 0.5;
        double tud = 0.5;

        // Check if the condition is satisfied
        return ddd <= tdd && ddu <= tdu && dud <= tud;
    }

}
