package lt.vu.bakalauras.model;

import lombok.Data;

import java.util.List;

@Data
public class UserLoginData {
    private String username;
    private String password;
    private String inputType;
    private List<FlightTime> flightTimes;
}
