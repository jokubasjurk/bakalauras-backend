package lt.vu.bakalauras.model;

import jakarta.persistence.*;
import lt.vu.bakalauras.service.TemplateDataMapConverter;

import java.util.Map;

@Entity
@Table(name = "Users")
@SuppressWarnings("JpaAttributeTypeInspection")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;
    private String email;

    @Column(name = "TEMPLATEDATA")
    @Convert(converter = TemplateDataMapConverter.class)
    private Map<String, TemplateData> templateData;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTemplateData(Map<String, TemplateData> templateData) {
        this.templateData = templateData;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Map<String, TemplateData> getTemplateData() {
        return templateData;
    }
}
