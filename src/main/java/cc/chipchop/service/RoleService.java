package cc.chipchop.service;

import cc.chipchop.dao.RoleDao;
import cc.chipchop.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {
    private final RoleDao roleDao;

    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleDao.findAll();
    }
}
