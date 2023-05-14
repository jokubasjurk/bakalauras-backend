package lt.vu.bakalauras.classifier;

import java.util.List;
import java.util.Map;
import lt.vu.bakalauras.model.FlightTime;
import lt.vu.bakalauras.model.TemplateData;
import lt.vu.bakalauras.service.ClassifierStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EuclideanClassifier {

  private static final double EUCLIDEAN_DISTANCE_THRESHOLD = 350d;
  private static final double PROBABILITY_THRESHOLD = 0.7d;
  private static final double WEIGHTED_PROBABILITY_THRESHOLD = 0.6d;
  private static final Map<String, Double> FEATURE_WEIGHTS =
      Map.ofEntries(
          Map.entry("th", 1.0),
          Map.entry("he", 0.9),
          Map.entry("eq", 0.1),
          Map.entry("qu", 0.4),
          Map.entry("ui", 0.4),
          Map.entry("ic", 0.8),
          Map.entry("ck", 0.6),
          Map.entry("br", 0.5),
          Map.entry("ro", 0.8),
          Map.entry("ow", 0.7),
          Map.entry("wn", 0.6),
          Map.entry("fo", 0.7),
          Map.entry("ox", 0.3),
          Map.entry("xj", 0.1),
          Map.entry("ju", 0.6),
          Map.entry("um", 0.7),
          Map.entry("mp", 0.6),
          Map.entry("ps", 0.6),
          Map.entry("so", 0.8),
          Map.entry("ov", 0.7),
          Map.entry("ve", 0.9),
          Map.entry("er", 1.0),
          Map.entry("rt", 0.8),
          Map.entry("tl", 0.5),
          Map.entry("la", 0.9),
          Map.entry("az", 0.2),
          Map.entry("zy", 0.2),
          Map.entry("yd", 0.2),
          Map.entry("do", 0.8),
          Map.entry("og", 0.4));
  private static final String CLASSIFIER_TYPE = "euclidean";
  private final ClassifierStatisticsService classifierStatisticsService;

  @Autowired
  public EuclideanClassifier(ClassifierStatisticsService classifierStatisticsService) {
    this.classifierStatisticsService = classifierStatisticsService;
  }

  private static double calculateEuclideanDistance(
      Map<String, TemplateData> userTemplate, List<FlightTime> authenticationData) {
    double sum = 0;

    for (FlightTime flightTime : authenticationData) {
      String key = flightTime.getKeyPair() + flightTime.getType();
      TemplateData templateData = userTemplate.get(key);

      if (templateData != null) {
        double diff = flightTime.getFlightTime() - templateData.getMean();
        sum += Math.pow(diff, 2);
      }
    }

    return Math.sqrt(sum);
  }

  private static double calculateNonWeightedProbabilityScore(
      Map<String, TemplateData> userTemplate, List<FlightTime> authenticationData) {
    double totalScore = 0;

    for (FlightTime flightTime : authenticationData) {
      String key = flightTime.getKeyPair() + flightTime.getType();
      TemplateData templateData = userTemplate.get(key);

      if (templateData != null) {
        double probability =
            calculateProbability(
                flightTime.getFlightTime(), templateData.getMean(), templateData.getStdDev());
        totalScore += probability;
      }
    }

    return totalScore;
  }

  private static double calculateProbability(double value, double mean, double stdDev) {
    if (stdDev < 1) {
      return 0;
    }

    double exponent = -Math.pow(value - mean, 2) / (2 * Math.pow(stdDev, 2));
    double base = 1 / (stdDev * Math.sqrt(2 * Math.PI));

    return base * Math.exp(exponent);
  }

  private static double calculateWeightedProbabilityScore(
      Map<String, TemplateData> userTemplate, List<FlightTime> authenticationData) {
    double totalScore = 0;

    for (FlightTime flightTime : authenticationData) {
      String key = flightTime.getKeyPair() + flightTime.getType();
      TemplateData templateData = userTemplate.get(key);

      if (templateData != null) {
        double probability =
            calculateProbability(
                flightTime.getFlightTime(), templateData.getMean(), templateData.getStdDev());
        double weight = FEATURE_WEIGHTS.getOrDefault(flightTime.getKeyPair(), 1.0);
        totalScore += probability * weight;
      }
    }

    return totalScore;
  }

  public boolean classify(
      List<FlightTime> authenticationData,
      Map<String, TemplateData> userTemplate,
      boolean isImpostor) {
    // Calculate the Euclidean distance between the authentication data and the user's template
    double euclideanDistance = calculateEuclideanDistance(userTemplate, authenticationData);

    // Calculate the non-weighted probability score between the authentication data and the user's
    // template
    double probabilityScore =
        calculateNonWeightedProbabilityScore(userTemplate, authenticationData);

    double weightedProbabilityScore =
        calculateWeightedProbabilityScore(userTemplate, authenticationData);

    // Check if all scores are above their respective thresholds for authentication
    boolean authenticationResult =
        euclideanDistance <= EUCLIDEAN_DISTANCE_THRESHOLD
            && probabilityScore >= PROBABILITY_THRESHOLD
            && weightedProbabilityScore >= WEIGHTED_PROBABILITY_THRESHOLD;

    classifierStatisticsService.calculateStatistics(
        CLASSIFIER_TYPE, authenticationResult, isImpostor);

    return authenticationResult;
  }
}
