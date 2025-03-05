package cc.chipchop.service;

import cc.chipchop.dao.UserDao;
import cc.chipchop.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserDao userDao;
    private final Log logger = LogFactory.getLog(this.getClass());

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public Optional<User> findById(long id){
        var temp = userDao.findById(id);
        if (temp.isEmpty()) {
            logger.info("ID not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return temp;
    }

    public void insert(User user){
        userDao.insert(user);
        logger.info("New user added");
    }

    public boolean update(long id, User user) {
        boolean isUpdated = false;
        var temp = findById(id);
        if (temp.isPresent()) {
            userDao.update(id, user);
            logger.info("User updated");
            isUpdated = true;
        }
        return isUpdated;
    }

    public boolean delete(long id) {
        boolean isDeleted = false;
        var temp = findById(id);
        if (temp.isPresent()) {
            userDao.delete(id);
            logger.info("User deleted");
            isDeleted= true;
        }
        return isDeleted;

    }

}
