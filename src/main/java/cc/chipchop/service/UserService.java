package cc.chipchop.service;

import cc.chipchop.dao.UserDao;
import cc.chipchop.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(long id) {
        return Optional.ofNullable(userDao.findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userDao.findByEmail(email).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void insert(User user) {
        userDao.findByEmail(user.email()).
            ifPresentOrElse(
                u -> {throw new ResponseStatusException(HttpStatus.CONFLICT);},
                () -> userDao.insert(user));
    }

    @Transactional
    public boolean update(long id, User user) {
        return userDao.update(id, user) > 0;
    }

    @Transactional
    public void delete(long id) {
        findById(id).ifPresent(u -> userDao.delete(id));
    }
}
