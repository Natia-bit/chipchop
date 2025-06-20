package cc.chipchop.service;

import cc.chipchop.dao.UserRoleDao;
import cc.chipchop.entity.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
