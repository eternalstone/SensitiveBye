package com.github.eternalstone.example.entity;

import com.github.eternalstone.annotation.CipherField;
import com.github.eternalstone.annotation.SensitiveBye;
import com.github.eternalstone.enums.SensitiveType;
import com.github.eternalstone.example.sensitvebye.MobileAlgorithm;
import com.github.eternalstone.example.sensitvebye.PasswordAlgorithm;

import java.io.Serializable;

/**
 * to do something
 *
 * @author Justzone on 2022/10/9 17:05
 */
public class User implements Serializable {

    private Integer id;

    @SensitiveBye("test")
    private String username;

    @CipherField(PasswordAlgorithm.class)
    private String password;

    @CipherField(MobileAlgorithm.class)
    @SensitiveBye(strategy = SensitiveType.MOBILE)
    private String mobile;

    @SensitiveBye(strategy = SensitiveType.EMAIL)
    private String email;

    @SensitiveBye(strategy = SensitiveType.ADDRESS)
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
