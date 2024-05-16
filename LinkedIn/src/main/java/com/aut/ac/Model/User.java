package com.aut.ac.Model;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @SerializedName("id")
    private long id;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
}