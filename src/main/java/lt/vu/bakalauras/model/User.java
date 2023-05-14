package lt.vu.bakalauras.model;

import jakarta.persistence.*;
import lombok.*;
import lt.vu.bakalauras.classifier.template.TemplateDataMapConverter;

import java.util.Map;

@Entity
@Table(name = "Users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Data
@SuppressWarnings("JpaAttributeTypeInspection")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;
    private String email;
    private String inputType;

    @Column(name = "TEMPLATEDATA")
    @Convert(converter = TemplateDataMapConverter.class)
    private Map<String, TemplateData> templateData;
}
