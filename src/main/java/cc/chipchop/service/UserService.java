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
        userDao.findByEmail(user.email()).
            ifPresentOrElse(
                u -> {throw new ResponseStatusException(HttpStatus.CONFLICT);},
                () -> userDao.insert(user));
    }

//  inteliJ is suggesting to change method to return void?
//    public boolean update(long id, User user) {
//        return findById(id)
//            .map(u -> userDao.update(id, user) > 0)
//            .orElse(false);
//    }

    public void update(long id, User user) {
        findById(id).ifPresent(u -> userDao.update(id, user));
    }

//    public boolean delete(long id) {
//        return findById(id)
//            .map(u -> userDao.delete(id) > 0)
//            .orElse(false);
//    }

    public void delete(long id) {
        findById(id).ifPresent(u -> userDao.delete(id));
    }
}
