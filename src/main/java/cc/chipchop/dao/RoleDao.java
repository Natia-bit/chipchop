package cc.chipchop.dao;

import cc.chipchop.entity.Role;
import cc.chipchop.mapper.RoleRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class RoleDao {
    private final JdbcTemplate jdbcTemplate;
    private final RoleRowMapper rowMapper = new RoleRowMapper();

    @Autowired
    public RoleDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Role> findAll(){
        var query = "SELECT id, name FROM roles";
        return this.jdbcTemplate.query(query, this.rowMapper);
    }
}
