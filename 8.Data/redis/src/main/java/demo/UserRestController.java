package demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    ResponseEntity create(@RequestBody User user){
        Assert.notNull(user,"User cannot be null");
        return Optional.ofNullable(userService.createUser(user))
                .map(result -> new ResponseEntity<>(result, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @GetMapping("/{id}")
    ResponseEntity<Optional<User>> get(@PathVariable String id) {
        return Optional.ofNullable(userService.getUser(id))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user){
        Assert.notNull(user, "the user can't be null");
        user.setId(id);
        return Optional.ofNullable(userService.updateUser(id,user))
                .map(result-> new ResponseEntity<User>(HttpStatus.NO_CONTENT))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{/id}")
    ResponseEntity deleteUser(@PathVariable(value="id") String id){
        return Optional.ofNullable(userService.deleteUser(id))
                .map(result -> new ResponseEntity<>(result,HttpStatus.NO_CONTENT))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
