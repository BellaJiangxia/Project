package com.winmine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {
    private int id;
    private String userName;
    private String passWord;
    private String role;
}
