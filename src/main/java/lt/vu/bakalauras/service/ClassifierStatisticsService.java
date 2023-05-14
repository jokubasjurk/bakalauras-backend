package lt.vu.bakalauras.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lt.vu.bakalauras.model.ClassifierStatistics;
import lt.vu.bakalauras.repository.ClassifierStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassifierStatisticsService {

  private final ClassifierStatisticsRepository classifierStatisticsRepository;

  @Autowired
  public ClassifierStatisticsService(
      ClassifierStatisticsRepository classifierStatisticsRepository) {
    this.classifierStatisticsRepository = classifierStatisticsRepository;
  }

  @Transactional
  public void calculateStatistics(
      String classifierType, boolean authenticationResult, boolean isImpostor) {
    ClassifierStatistics classifierStatistics =
        classifierStatisticsRepository.findByClassifierType(classifierType);

    if (authenticationResult) {
      if (isImpostor) {
        classifierStatistics.setFalsePositives(classifierStatistics.getFalsePositives() + 1);
      } else {
        classifierStatistics.setTruePositives(classifierStatistics.getTruePositives() + 1);
      }
    } else {
      if (isImpostor) {
        classifierStatistics.setTrueNegatives(classifierStatistics.getTrueNegatives() + 1);
      } else {
        classifierStatistics.setFalseNegatives(classifierStatistics.getFalseNegatives() + 1);
      }
    }
    // Calculate the new metrics
    int total =
        classifierStatistics.getTruePositives()
            + classifierStatistics.getTrueNegatives()
            + classifierStatistics.getFalsePositives()
            + classifierStatistics.getFalseNegatives();

    double falseAcceptanceRate =
        total != 0 ? (double) classifierStatistics.getFalsePositives() / total : 0;
    double falseRejectionRate =
        total != 0 ? (double) classifierStatistics.getFalseNegatives() / total : 0;
    double accuracy =
        total != 0
            ? (double)
                    (classifierStatistics.getTruePositives()
                        + classifierStatistics.getTrueNegatives())
                / total
            : 0;

    // Calculate the simplified EER
    double equalErrorRate = (falseAcceptanceRate + falseRejectionRate) / 2;

    classifierStatistics.setFalseAcceptanceRate(falseAcceptanceRate);
    classifierStatistics.setFalseRejectionRate(falseRejectionRate);
    classifierStatistics.setEqualErrorRate(equalErrorRate);
    classifierStatistics.setAccuracy(accuracy);

    // Save the updated statistics to the database
    classifierStatisticsRepository.save(classifierStatistics);
  }

  public List<ClassifierStatistics> getClassifiersStatistics() {
    List<ClassifierStatistics> classifierStatistics = new ArrayList<>();
    classifierStatisticsRepository.findAll().forEach(classifierStatistics::add);
    return classifierStatistics;
  }
}
