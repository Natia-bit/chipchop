package cc.chipchop.rest;

import cc.chipchop.entity.User;
import cc.chipchop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ChipchopRestController {
    private final UserService userService;

    public ChipchopRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public Optional<User> findUserById(@PathVariable long id) {
        return userService.findById(id);
    }

    @PostMapping("/users")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createUser(@RequestBody User user) {
        userService.insert(user);
    }

    @PutMapping("/users/{id}")
    public void updateUser(@PathVariable long id, @RequestBody User user) {
        userService.update(id, user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.delete(id);
    }
}
