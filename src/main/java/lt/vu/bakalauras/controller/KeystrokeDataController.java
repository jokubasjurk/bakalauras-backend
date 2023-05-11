package lt.vu.bakalauras.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lt.vu.bakalauras.model.*;
import lt.vu.bakalauras.service.Classifier;
import lt.vu.bakalauras.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static lt.vu.bakalauras.service.TemplateGenerator.calculateMeanAndStdDev;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
@AllArgsConstructor
public class KeystrokeDataController {

    @Autowired
    private final UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody UserRegistrationData userRegistrationData) {
        Map<String, TemplateData> templateForKeyPairs = calculateMeanAndStdDev(userRegistrationData.getFlightTimes());
        User user = new User();
        user.setEmail(userRegistrationData.getEmail());
        user.setUsername(userRegistrationData.getUsername());
        user.setPassword(userRegistrationData.getPassword());
        user.setTemplateData(templateForKeyPairs);

        userService.saveOrUpdate(user);
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .success(true)
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody UserLoginData userLoginData) {
        User user = userService.getAllUsers().stream()
                .filter(user1 -> user1.getUsername().equals(userLoginData.getUsername()))
                .toList().get(0);
        Map<String, TemplateData> templateForKeyPairs =  user.getTemplateData();
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .success(Classifier.classify(userLoginData.getFlightTimes(), templateForKeyPairs))
                .build());
    }
}
