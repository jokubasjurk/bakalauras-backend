package lt.vu.bakalauras.service;

import lt.vu.bakalauras.model.User;
import lt.vu.bakalauras.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean isPasswordCorrect(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getPassword().equals(password);
        }
        return false;
    }

    public void saveOrUpdate(User user) {
        userRepository.save(user);
    }
}
