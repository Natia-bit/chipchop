package cc.chipchop.rest;

import cc.chipchop.entity.UserRole;
import cc.chipchop.service.UserRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRoleRestController {
    private final UserRoleService userRoleService;

    public UserRoleRestController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping("/roles")
    public List<UserRole> getAllRoles(){
        return userRoleService.findAll();
    }

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public void assignRole(@RequestBody UserRole userRole) {
        userRoleService.assignRole(userRole);
    }
}

