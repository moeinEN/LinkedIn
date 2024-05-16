package com.aut.ac.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


public class Storage {
    @Getter
    private static List<User> allUsers = new ArrayList<User>();

}
