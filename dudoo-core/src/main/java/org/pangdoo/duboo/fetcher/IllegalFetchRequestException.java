package org.pangdoo.duboo.fetcher;

public class IllegalFetchRequestException extends RuntimeException {

    private static final long serialVersionUID = -4237697966154849337L;

    public IllegalFetchRequestException() {
    }

    public IllegalFetchRequestException(String s) {
        super(s);
    }

    public IllegalFetchRequestException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public IllegalFetchRequestException(Throwable throwable) {
        super(throwable);
    }

    public IllegalFetchRequestException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
