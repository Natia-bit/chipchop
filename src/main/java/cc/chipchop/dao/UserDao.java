package cc.chipchop.dao;

import cc.chipchop.entity.User;
import cc.chipchop.mapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper rowMapper = new UserRowMapper();

    @Autowired
    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<User> findAll() {
        var query = "SELECT id, email, password FROM users";
        return this.jdbcTemplate.query(query, this.rowMapper);
    }

    public Optional<User> findById(long id) {
        var query = "SELECT id, email, password FROM users WHERE id=?";
        try {
            return Optional.ofNullable(this.jdbcTemplate.queryForObject(query, this.rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        var query = "SELECT id, email, password FROM users WHERE email=?";
        try {
            return Optional.of(Objects.requireNonNull(this.jdbcTemplate.queryForObject(query, this.rowMapper, email)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long insert(User user) {
        var query = "INSERT INTO users(email, password) VALUES(?,?) RETURNING id";
        return this.jdbcTemplate.queryForObject( query, Long.class, user.email(), user.password());
    }

    public int update(long id, User user) {
        var query = "UPDATE users SET email=?, password=? WHERE id=?";
        return this.jdbcTemplate.update(query, user.email(), user.password(), id);
    }

    public int delete(long id) {
        var query = "DELETE FROM users WHERE id=?";
        return this.jdbcTemplate.update(query, id);
    }
}
