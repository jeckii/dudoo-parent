package org.pangdoo.duboo.http;

public class NoCredentialException extends RuntimeException {

    private static final long serialVersionUID = -2865259102925145910L;

    public NoCredentialException() {
    }

    public NoCredentialException(String s) {
        super(s);
    }

    public NoCredentialException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NoCredentialException(Throwable throwable) {
        super(throwable);
    }

    public NoCredentialException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
