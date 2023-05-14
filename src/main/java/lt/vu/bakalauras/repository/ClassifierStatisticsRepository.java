package lt.vu.bakalauras.repository;

import lt.vu.bakalauras.model.ClassifierStatistics;
import org.springframework.data.repository.CrudRepository;

public interface ClassifierStatisticsRepository extends CrudRepository<ClassifierStatistics, Integer> {
    ClassifierStatistics findByClassifierType(String classifierType);
}
