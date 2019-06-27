package org.pangdoo.duboo.url;

public class NoURLException extends RuntimeException {

    private static final long serialVersionUID = 7116997083901157870L;

    public NoURLException() {
    }

    public NoURLException(String s) {
        super(s);
    }

    public NoURLException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NoURLException(Throwable throwable) {
        super(throwable);
    }

    public NoURLException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
