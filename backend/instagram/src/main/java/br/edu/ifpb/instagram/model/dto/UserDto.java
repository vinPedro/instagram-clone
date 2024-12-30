package br.edu.ifpb.instagram.model.dto;

import java.io.Serializable;

public class UserDto implements Serializable{

    private static final long serialVersionUID = 7293840579190289061L;
    private long id;
    private String userID;
    private String fullName;
    private String username;
    private String email;
    private String password;
    private String emailVerificationToken;
    private boolean emailVerificationStatus = false;
    private String encryptedPassword;


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }
    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }
    public boolean getEmailVerificationStatus() {
        return emailVerificationStatus;
    }
    public void setEmailVerificationStatus(boolean emailVerificationStatus) {
        this.emailVerificationStatus = emailVerificationStatus;
    }
    public String getEncryptedPassword() {
        return encryptedPassword;
    }
    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

}
