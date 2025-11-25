package cc.chipchop.dao;

import cc.chipchop.entity.UserRole;
import cc.chipchop.mapper.UserRoleRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRoleDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserRoleRowMapper rowMapper = new UserRoleRowMapper();

    @Autowired
    public UserRoleDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<UserRole> findAll(){
        var query = "SELECT user_id, role FROM user_roles";
        return this.jdbcTemplate.query(query, this.rowMapper);
    }

    public int insert(UserRole userRole) {
        var query = "INSERT INTO user_roles(user_id, role) VALUES (?, ?) ";
            return this.jdbcTemplate.update(query, userRole.userId(), userRole.role().name());
    }

    public Optional<UserRole> findRoleByUserId( long userId){
        var query = "SELECT user_id, role FROM user_roles WHERE user_id=?";
        try {
            return Optional.ofNullable(this.jdbcTemplate.queryForObject(query, this.rowMapper, userId));
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }

    }
}
