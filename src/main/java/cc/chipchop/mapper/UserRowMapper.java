package cc.chipchop.mapper;

import cc.chipchop.entity.User;

import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
            rs.getLong( "id"),
            rs.getString("email"),
            rs.getString("password")
        );
    }

}
