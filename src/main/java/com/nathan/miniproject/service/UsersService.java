package com.nathan.miniproject.service;

import com.nathan.miniproject.constant.ResponseMessage;
import com.nathan.miniproject.domain.dao.Users;
import com.nathan.miniproject.domain.dto.UsersRequest;
import com.nathan.miniproject.repository.UsersRepository;
import com.nathan.miniproject.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.getDistinctTopByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Username not found");
        return user;
    }

    public UsersRequest find(Long id) {
        Optional<Users> optionalUsers = usersRepository.findById(id);
        if (optionalUsers.isEmpty())
            throw new RuntimeException("User not found");
        Users user = optionalUsers.get();
        return UsersRequest
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    public UsersRequest fundBalance(UsersRequest usersRequest) {
        Users user = usersRepository.findById(usersRequest.getId()).get();

        user.setBalance(user.getBalance() + usersRequest.getBalance());
        usersRepository.save(user);

        return UsersRequest
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .balance(user.getBalance())
                .build();
    }

    public UsersRequest find(Long id, boolean balance) {
        Optional<Users> optionalUsers = usersRepository.findById(id);
        if (optionalUsers.isEmpty())
            throw new RuntimeException("User not found");

        Users user = optionalUsers.get();
        UsersRequest ret = UsersRequest
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .build();
        if (balance)
            ret.setBalance(user.getBalance());
        return ret;
    }

    public List<UsersRequest> find() {
        List<Users> usersList = usersRepository.findAll();
        List<UsersRequest> usersRequests = new ArrayList<>();
        for(Users user : usersList) {
            usersRequests.add(UsersRequest
                    .builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getName())
                    .build());
        }
        return usersRequests;
    }

    public void deleteUser(Long id) {
        Optional<Users> optionalUsers = usersRepository.findById(id);
        if (optionalUsers.isEmpty())
            throw new RuntimeException("User not found");
        usersRepository.delete(optionalUsers.get());
    }
}
