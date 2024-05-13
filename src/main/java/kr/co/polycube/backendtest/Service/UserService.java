package kr.co.polycube.backendtest.Service;

import kr.co.polycube.backendtest.Domain.User;
import kr.co.polycube.backendtest.Exceptions.UserAlreadyExistsException;
import kr.co.polycube.backendtest.Exceptions.UserDoesNotExistException;
import kr.co.polycube.backendtest.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        // 유저의 이름에 증복을 허용합니다
        // 이름은 같아도 아이디는 고유성을 가집니다
        user.setId(UUID.randomUUID().toString());
        // 혹시라도 UUID collision 있을 경우에 확인합니다
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException();
        }
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserDoesNotExistException(
                        "User with id {" + id + "} does not exist."
                ));
    }
}
