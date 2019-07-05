package org.pangdoo.duboo.http.authentication;

public class CredentialTypeErrorException extends RuntimeException {

    private static final long serialVersionUID = 1402213236965971147L;

    public CredentialTypeErrorException() {
    }

    public CredentialTypeErrorException(String s) {
        super(s);
    }

    public CredentialTypeErrorException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CredentialTypeErrorException(Throwable throwable) {
        super(throwable);
    }

    public CredentialTypeErrorException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
