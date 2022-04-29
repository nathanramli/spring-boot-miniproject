package com.nathan.miniproject.service;

import com.nathan.miniproject.constant.ResponseMessage;
import com.nathan.miniproject.domain.dao.Users;
import com.nathan.miniproject.domain.dto.TokenResponse;
import com.nathan.miniproject.domain.dto.UsersRequest;
import com.nathan.miniproject.repository.UsersRepository;
import com.nathan.miniproject.config.JwtTokenProvider;
import com.nathan.miniproject.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    public Users register(UsersRequest usersRequest) throws RuntimeException {
        Users exists = usersRepository.getDistinctTopByUsername(usersRequest.getUsername());
        if (exists != null) {
            throw new RuntimeException("User already exists");
        }

        Users users = Users.builder()
                .username(usersRequest.getUsername())
                .balance(0D)
                .active(true)
                .isAdmin(false)
                .name(usersRequest.getName())
                .password(passwordEncoder.encode(usersRequest.getPassword())).build();
        return usersRepository.save(users);
    }

    public ResponseEntity<?> authenticatedAndGenerateToken(UsersRequest usersRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usersRequest.getUsername(),
                        usersRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        return Response.build(
                ResponseMessage.SUCCESS,
                HttpStatus.OK,
                TokenResponse.builder().token(jwt).build()
        );
    }
}
