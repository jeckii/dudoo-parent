package org.pangdoo.duboo.http.authentication;

public class BasicCredential extends Credential {

    private String host;

    private int port;

    public BasicCredential(String username, String password, String host, int port) {
        super(username, password);
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
