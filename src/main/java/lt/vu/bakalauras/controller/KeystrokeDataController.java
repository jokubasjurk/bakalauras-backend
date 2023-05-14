package lt.vu.bakalauras.controller;

import static lt.vu.bakalauras.classifier.template.TemplateGenerator.calculateMeanAndStdDev;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lt.vu.bakalauras.classifier.EuclideanClassifier;
import lt.vu.bakalauras.classifier.ZScoreClassifier;
import lt.vu.bakalauras.model.*;
import lt.vu.bakalauras.service.ClassifierStatisticsService;
import lt.vu.bakalauras.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
@AllArgsConstructor
public class KeystrokeDataController {

  @Autowired private final UserService userService;
  @Autowired private final ClassifierStatisticsService classifierStatisticsService;
  @Autowired private final EuclideanClassifier euclideanClassifier;
  @Autowired private final ZScoreClassifier zScoreClassifier;

  @PostMapping(path = "/register")
  public ResponseEntity<AuthenticationResponse> registerUser(
      @RequestBody UserRegistrationData userRegistrationData) {
    userService.saveOrUpdate(
        User.builder()
            .email(userRegistrationData.getEmail())
            .username(userRegistrationData.getUsername())
            .password(userRegistrationData.getPassword())
            .inputType(userRegistrationData.getInputType())
            .templateData(calculateMeanAndStdDev(userRegistrationData.getFlightTimes()))
            .build());
    return ResponseEntity.ok(AuthenticationResponse.builder().success(true).build());
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> loginUser(
      @RequestBody UserLoginData userLoginData) {

    User user = userService.getUserByUsername(userLoginData.getUsername());

    if (!user.getInputType().equals(userLoginData.getInputType())) {
      return ResponseEntity.ok(AuthenticationResponse.builder().success(false).build());
    }

    Map<String, TemplateData> templateForKeyPairs = user.getTemplateData();

    boolean isImpostorSample =
        !userService.isPasswordCorrect(userLoginData.getUsername(), userLoginData.getPassword());

    boolean isAuthenticatedByEuclideanClassifier =
        euclideanClassifier.classify(
            userLoginData.getFlightTimes(), templateForKeyPairs, isImpostorSample);

    boolean isAuthenticatedByZScoreClassifier =
        zScoreClassifier.classify(
            userLoginData.getFlightTimes(), templateForKeyPairs, isImpostorSample);

    return ResponseEntity.ok(
        AuthenticationResponse.builder()
            .classifierResultMapping(
                Map.of(
                    "euclidean", isAuthenticatedByEuclideanClassifier,
                    "Z-score", isAuthenticatedByZScoreClassifier))
            .success(isAuthenticatedByEuclideanClassifier && isAuthenticatedByZScoreClassifier)
            .build());
  }

  @GetMapping("/statistics")
  public List<ClassifierStatistics> getClassifierStatistics() {
    return classifierStatisticsService.getClassifiersStatistics();
  }
}
