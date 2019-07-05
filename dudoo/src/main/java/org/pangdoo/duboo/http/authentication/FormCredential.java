package org.pangdoo.duboo.http.authentication;

public class FormCredential extends Credential {

    private String loginUrl;

    private String usernameKey;

    private String passwordKey;

    public FormCredential() {
    }

    public FormCredential(String username, String password, String loginUrl, String usernameKey, String passwordKey) {
        super(username, password);
        this.loginUrl = loginUrl;
        this.usernameKey = usernameKey;
        this.passwordKey = passwordKey;
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