package lt.vu.bakalauras.classifier.template;

import lt.vu.bakalauras.model.FlightTime;
import lt.vu.bakalauras.model.TemplateData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateGenerator {
    public static Map<String, TemplateData> calculateMeanAndStdDev(List<List<FlightTime>> flightTimes) {
        // Group FlightTime objects by key pair and type
        Map<String, List<Double>> flightTimeData = new HashMap<>();

        for (List<FlightTime> sample : flightTimes) {
            for (FlightTime flightTime : sample) {
                String key = flightTime.getKeyPair() + flightTime.getType();
                flightTimeData.putIfAbsent(key, new ArrayList<>());
                flightTimeData.get(key).add(flightTime.getFlightTime());
            }
        }

        // Calculate mean and standard deviation for each key pair and type
        Map<String, TemplateData> statistics = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : flightTimeData.entrySet()) {
            String key = entry.getKey();
            List<Double> values = entry.getValue();
            double mean = calculateMean(values);
            double stdDev = calculateStdDev(values, mean);
            TemplateData templateData = new TemplateData(mean, stdDev);
            statistics.put(key, templateData);
        }

        return statistics;
    }

    private static double calculateMean(List<Double> values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    private static double calculateStdDev(List<Double> values, double mean) {
        double sum = 0;
        for (double value : values) {
            sum += Math.pow(value - mean, 2);
        }
        return Math.max(Math.sqrt(sum / values.size()), 0.000005d);
    }
}

