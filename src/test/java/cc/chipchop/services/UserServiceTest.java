package cc.chipchop.services;

import cc.chipchop.dao.UserDao;
import cc.chipchop.entity.User;
import cc.chipchop.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void serUp()
    {
        when(userDao.findById(1)).thenReturn(Optional.empty());
        when(userDao.findById(2)).thenReturn(Optional.of(new User(2, "bob@example.com", "secretpass")));
        when(userDao.findById(3)).thenReturn(Optional.of(new User(3, "alice@example.com", "supersecretpass")));
        when(userDao.findById(4)).thenReturn(Optional.of(new User(4, "alex@example.com", "topsecretpass")));
    }
    @AfterEach
    public void tearDown(){
        Mockito.reset(userDao);
    }
}
