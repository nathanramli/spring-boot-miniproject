package com.nathan.miniproject.service;

import com.nathan.miniproject.domain.dao.Users;
import com.nathan.miniproject.domain.dto.UsersRequest;
import com.nathan.miniproject.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UsersService.class)
class UsersServiceTest {
    @MockBean
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;

    @Test
    void loadUserByUsernameSuccess_Test() {
        when(usersRepository.getDistinctTopByUsername(any())).thenReturn(
                Users.builder()
                        .username("nathan")
                        .password("123456")
                        .name("Nathan Ramli").build());

        UserDetails userDetails = usersService.loadUserByUsername("nathan");
        assertEquals("nathan", userDetails.getUsername());
        assertEquals("123456", userDetails.getPassword());
    }

    @Test
    void loadUserByUsernameError_Test() {
        when(usersRepository.getDistinctTopByUsername(any())).thenReturn(null);

        try {
            UserDetails userDetails = usersService.loadUserByUsername("nathan");
            fail();
        } catch (UsernameNotFoundException e) {
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    void findAllSuccess_Test() {
        when(usersRepository.findAll()).thenReturn(
                Arrays.asList(
                        Users.builder().name("User 1").build(),
                        Users.builder().name("User 2").build()
                )
        );

        List<UsersRequest> usersList = usersService.find();
        assertEquals(2, usersList.size());
        assertEquals(usersList.get(0).getName(), "User 1");
        assertEquals(usersList.get(1).getName(), "User 2");
    }

    @Test
    void findUserSuccess_Test() {
        when(usersRepository.findById(any())).thenReturn(Optional.of(Users.builder().id(1L).username("nathan").name("Nathan Ramli").build()));

        UsersRequest user = usersService.find(1L);
        assertEquals(1L, user.getId());
        assertEquals("nathan", user.getUsername());
        assertEquals("Nathan Ramli", user.getName());
    }

    @Test
    void findUserError_Test() {
        when(usersRepository.findById(any())).thenReturn(Optional.empty());

        try {
            UsersRequest user = usersService.find(1L);
            fail();
        } catch (RuntimeException e) {
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void findUserBalanceSuccess_Test() {
        when(usersRepository.findById(any())).thenReturn(Optional.of(
                Users.builder()
                        .id(1L)
                        .username("nathan")
                        .name("Nathan Ramli")
                        .balance(100D)
                        .build()
        ));

        try {
            UsersRequest user = usersService.find(1L, true);
            assertEquals(100D, user.getBalance());
            assertEquals(1L, user.getId());
        } catch (RuntimeException e) {
            fail();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void findUserWithoutBalanceSuccess_Test() {
        when(usersRepository.findById(any())).thenReturn(Optional.of(
                Users.builder()
                        .id(1L)
                        .username("nathan")
                        .name("Nathan Ramli")
                        .balance(100D)
                        .build()
        ));

        try {
            UsersRequest user = usersService.find(1L, false);
            assertEquals(null, user.getBalance());
            assertEquals(1L, user.getId());
        } catch (RuntimeException e) {
            fail();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void fundUserBalanceSuccess_Test() {
        when(usersRepository.findById(any())).thenReturn(Optional.of(
                Users.builder()
                        .id(1L)
                        .balance(1000D)
                        .build()
        ));

        try {
            UsersRequest user = usersService.fundBalance(UsersRequest.builder()
                    .id(1L)
                    .balance(1000D)
                    .build());
            assertEquals(2000D, user.getBalance());
        } catch (RuntimeException e) {
            fail();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void findUserBalanceFail_Test() {
        when(usersRepository.findById(any())).thenReturn(Optional.empty());

        try {
            UsersRequest user = usersService.find(1L, true);
            fail();
        } catch (RuntimeException e) {
        } catch (Exception e) {
            fail();
        }
    }
}