package cc.chipchop.service;

import cc.chipchop.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailServiceImpl(UserService userService) {
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Not found");
        }

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.email())
            .password(user.password())
            .build();
    }
}
