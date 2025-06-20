package cc.chipchop.mapper;

import cc.chipchop.entity.UserRole;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRoleRowMapper implements RowMapper<UserRole> {

    @Override
    public UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserRole(
            rs.getLong("user_id"),
            rs.getString("role").toUpperCase());
    }
}