package com.nathan.miniproject.service;

import com.nathan.miniproject.config.JwtTokenProvider;
import com.nathan.miniproject.domain.common.ApiResponse;
import com.nathan.miniproject.domain.dao.Users;
import com.nathan.miniproject.domain.dto.TokenResponse;
import com.nathan.miniproject.domain.dto.UsersRequest;
import com.nathan.miniproject.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AuthService.class)
class AuthServiceTest {
    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthService authService;

    @Test
    void registerUsersSuccess_Test() {
        when(usersRepository.getDistinctTopByUsername("nathan")).thenReturn(null);
        when(usersRepository.save(any())).thenReturn(Users
                .builder()
                .id(1L)
                .username("nathan")
                .password("123456")
                .name("Nathan Ramli")
                .build());

        Users user = authService.register(
                UsersRequest
                        .builder()
                        .username("nathan")
                        .password("123456")
                        .name("Nathan Ramli")
                        .build()
        );
        assertEquals(1L, user.getId());
        assertEquals("nathan", user.getUsername());
        assertEquals("Nathan Ramli", user.getName());
        assertEquals("123456", user.getPassword());
    }

    @Test
    void registerUsersError_Test() {
        when(usersRepository.getDistinctTopByUsername("nathan")).thenReturn(Users
                .builder()
                .id(1L)
                .username("nathan")
                .password("123456")
                .name("Nathan Ramli")
                .build());

        try {
            Users user = authService.register(
                    UsersRequest
                            .builder()
                            .username("nathan")
                            .password("123456")
                            .name("Nathan Ramli")
                            .build()
            );
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    void authenticatedAndGenerateTokenSuccess_Test() {
        when(jwtTokenProvider.generateToken(any())).thenReturn("THIS_IS_A_TOKEN");

        ResponseEntity<?> responseEntity = authService.authenticatedAndGenerateToken(
                UsersRequest
                        .builder()
                        .username("nathan")
                        .password("123456")
                        .name("Nathan Ramli")
                        .build()
        );
        ApiResponse response = (ApiResponse) responseEntity.getBody();
        TokenResponse tokenResponse = (TokenResponse) Objects.requireNonNull(response).getData();
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals("THIS_IS_A_TOKEN", tokenResponse.getToken());
    }
}