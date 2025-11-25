package cc.chipchop.dao;

import cc.chipchop.entity.UserRole;
import cc.chipchop.mapper.UserRoleRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

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
}
