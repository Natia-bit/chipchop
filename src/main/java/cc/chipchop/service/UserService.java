package cc.chipchop.service;

import cc.chipchop.dao.UserDao;
import cc.chipchop.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;
    private final Log logger = LogFactory.getLog(this.getClass());

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public List<User> findAll() {
        return userDao.findAll();
    }

    public Optional<User> findById(long id) {
        var temp = userDao.findById(id);
        if (temp.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return temp;
    }

    public Optional<User> findByEmail(String email) {
        var temp = userDao.findByEmail(email);
        if (temp.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return temp;
    }

    public void insert(User user) {
        var temp = userDao.findByEmail(user.email());

        if (temp.isEmpty()) {
            userDao.insert(user);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

    }

    public boolean update(long id, User user) {
        boolean isUpdated = false;
        var temp = findById(id);
        if (temp.isPresent()) {
            userDao.update(id, user);
            isUpdated = true;
        }
        return isUpdated;
    }

    public boolean delete(long id) {
        boolean isDeleted = false;
        var temp = findById(id);
        if (temp.isPresent()) {
            userDao.delete(id);
            isDeleted = true;
        }
        return isDeleted;
    }

}
