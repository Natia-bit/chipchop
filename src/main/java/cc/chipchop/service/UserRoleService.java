package cc.chipchop.service;

import cc.chipchop.dao.UserRoleDao;
import cc.chipchop.entity.Role;
import cc.chipchop.entity.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserRoleService {
    private final UserRoleDao userRoleDao;

    public UserRoleService(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    @Transactional(readOnly = true)
    public List<UserRole> findAll() {
        return userRoleDao.findAll();
    }


    @Transactional
    public void assignRole(UserRole userRole) {
        userRoleDao.findRoleByUserId(userRole.userId()).
            ifPresentOrElse(ur -> {throw new ResponseStatusException(HttpStatus.CONFLICT);},
                () -> userRoleDao.insert(new UserRole(userRole.userId(), userRole.role())));
    }

    @Transactional
    public void revokeRole(long userId) {
        userRoleDao.findRoleByUserId(userId).ifPresent(ur -> userRoleDao.delete(userId));
    }
}
