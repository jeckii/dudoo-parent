package org.pangdoo.duboo.url;

public class URLNullErrorException extends RuntimeException {

    private static final long serialVersionUID = 7116997083901157870L;

    public URLNullErrorException() {
    }

    public URLNullErrorException(String s) {
        super(s);
    }

    public URLNullErrorException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public URLNullErrorException(Throwable throwable) {
        super(throwable);
    }

    public URLNullErrorException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
