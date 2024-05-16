package com.aut.ac.Service;

import com.aut.ac.Model.Storage;
import com.aut.ac.Model.User;
import retrofit2.Call;
import retrofit2.mock.Calls;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImp implements UserService {
    @Override
    public Call<User> addUser(String username, User user) {
        List<User> users = Storage.getAllUsers();
        users.add(user);
        return Calls.response(user);
    }

    @Override
    public Call<List<User>> getUsers() {
        return Calls.response(Storage.getAllUsers());
    }

    @Override
    public Call<User> getUser(String username) {
        User user = null;
        for (User users : Storage.getAllUsers()) {
            if (users.getUsername().equals(username)) {
                user = users;
            }
        }
        return Calls.response(user);
    }
}
