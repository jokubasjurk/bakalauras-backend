package lt.vu.bakalauras.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "CLASSIFIERSTATISTICS")
public class ClassifierStatistics {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "CLASSIFIERTYPE")
    private String classifierType;
    @Column(name = "FALSEPOSITIVES")
    private int falsePositives;
    @Column(name = "FALSENEGATIVES")
    private int falseNegatives;
    @Column(name = "TRUEPOSITIVES")
    private int truePositives;
    @Column(name = "TRUENEGATIVES")
    private int trueNegatives;
    @Column(name = "FALSEACCEPTANCERATE")
    private double falseAcceptanceRate;
    @Column(name = "FALSEREJECTIONRATE")
    private double falseRejectionRate;
    @Column(name = "EQUALERRORRATE")
    private double equalErrorRate;
    private double accuracy;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
