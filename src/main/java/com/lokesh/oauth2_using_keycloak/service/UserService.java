package com.lokesh.oauth2_using_keycloak.service;

import com.lokesh.oauth2_using_keycloak.dto.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements InitializingBean {

    private List<User> inmemoryUserList;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Initialized in-memory user list!!");
        this.inmemoryUserList = new ArrayList<>();
    }

    public boolean addUser(User user) {
        boolean sameIdUserExists = inmemoryUserList.stream()
                .anyMatch(u -> u.id().equals(user.id()));
        if (sameIdUserExists)
            return Boolean.FALSE;
        else {
            this.inmemoryUserList.add(user);
            return Boolean.TRUE;
        }
    }

    public User findUserById(Long id) {
        return inmemoryUserList.stream()
                .filter(u -> u.id() == id)
                .findAny()
                .orElse(null);
    }

    public List<User> findAll() {
        return inmemoryUserList;
    }

    public boolean deleteUserById(Long id) {
        User user = findUserById(id);
        if (user != null) {
            inmemoryUserList.remove(user);
            return Boolean.TRUE;
        } else
            return Boolean.FALSE;
    }
}
