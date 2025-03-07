package cc.chipchop.dao;

import cc.chipchop.entity.User;
import cc.chipchop.mapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper rowMapper = new UserRowMapper();

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<User> findAll(){
        var query = "SELECT id, email, password FROM users";
        return this.jdbcTemplate.query(query, this.rowMapper);
    }

    public Optional<User> findById(long id){
        var query = "SELECT id, email, password FROM users WHERE id=?";
        try {
            return Optional.ofNullable(this.jdbcTemplate.queryForObject(query, this.rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void insert(User user){
        var query = "INSERT INTO users(email, password) VALUES(?,?)";
        this.jdbcTemplate.update(query, user.email(), user.password());
    }

    public void update(long id, User user){
        var query = "UPDATE users SET email=?, password=? WHERE id=?";
        this.jdbcTemplate.update(query, user.email(), user.password(), id);
    }

    public void delete(long id){
        var query = "DELETE FROM users WHERE id=?";
        this.jdbcTemplate.update(query, id);
    }
}
