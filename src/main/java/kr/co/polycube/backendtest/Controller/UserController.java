package kr.co.polycube.backendtest.Controller;

import jakarta.validation.Valid;
import kr.co.polycube.backendtest.Domain.User;
import kr.co.polycube.backendtest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping // 유저 등록
    public ResponseEntity<Map<String, String>> createUser(@Valid @RequestBody User user) {
        User newUser = userService.createUser(user);
        Map<String, String> response = new HashMap<>();
        response.put("id", newUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getUserById(@PathVariable String id) {
        User retreivedUser = userService.getUserById(id);
        Map<String, String> response = new HashMap<>();
        response.put("id", retreivedUser.getId());
        response.put("name", retreivedUser.getName());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable String id, @Valid @RequestBody User user) {
        // 유저 수정 request에서 body에 유저의 아이디를 받는 경우에 필요합니다
        /*if(!id.equals(user.getId())) {
            throw new IllegalArgumentException(
                    "Mismatched ID from path {" + id + "} and user {" + user.getId() + "}"
            );
        }*/

        userService.getUserById(id); // 유저가 존재하지 않으면, exception thrown
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        Map<String, String> response = new HashMap<>();
        response.put("id", updatedUser.getId());
        response.put("name", updatedUser.getName());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
