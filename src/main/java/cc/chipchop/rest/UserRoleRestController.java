package cc.chipchop.rest;

import cc.chipchop.entity.Role;
import cc.chipchop.entity.UserRole;
import cc.chipchop.service.UserRoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
