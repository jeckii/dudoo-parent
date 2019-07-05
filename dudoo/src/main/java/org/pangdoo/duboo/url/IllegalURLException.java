package org.pangdoo.duboo.url;

public class IllegalURLException extends RuntimeException {

    private static final long serialVersionUID = -6500649598194953387L;

    public IllegalURLException() {
    }

    public IllegalURLException(String s) {
        super(s);
    }

    public IllegalURLException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public IllegalURLException(Throwable throwable) {
        super(throwable);
    }

    public IllegalURLException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

}
