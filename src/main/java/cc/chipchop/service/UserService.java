package cc.chipchop.service;

import cc.chipchop.dao.UserDao;
import cc.chipchop.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public List<User> findAll() {
        return userDao.findAll();
    }


    public Optional<User> findById(long id) {
        return Optional.ofNullable(userDao.findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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
