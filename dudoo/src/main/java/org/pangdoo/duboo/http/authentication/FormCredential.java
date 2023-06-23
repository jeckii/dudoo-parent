package org.pangdoo.duboo.http.authentication;

public class FormCredential extends Credential {

    private String loginUrl;

    private String usernameKey;

    private String passwordKey;

    private FormCredential() {
    }

    public static FormCredential custom() {
        return new FormCredential();
    }

    private FormCredential(String username, String password) {
        super(username, password);
    }

    public static FormCredential custom(String username, String password) {
        return new FormCredential(username, password);
    }

    public FormCredential username(String username) {
        super.setUsername(username);
        return this;
    }

    public FormCredential password(String password) {
        super.setPassword(password);
        return this;
    }

    public FormCredential usernameKey(String usernameKey) {
        this.usernameKey = usernameKey;
        return this;
    }

    public FormCredential passwordKey(String passwordKey) {
        this.passwordKey = passwordKey;
        return this;
    }

    public FormCredential loginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
        return this;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getUsernameKey() {
        return usernameKey;
    }

    public void setUsernameKey(String usernameKey) {
        this.usernameKey = usernameKey;
    }

    public String getPasswordKey() {
        return passwordKey;
    }

    public void setPasswordKey(String passwordKey) {
        this.passwordKey = passwordKey;
    }

}