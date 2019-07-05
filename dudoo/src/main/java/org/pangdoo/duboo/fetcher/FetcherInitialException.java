package org.pangdoo.duboo.fetcher;

public class FetcherInitialException extends RuntimeException {

    private static final long serialVersionUID = 9199174689573407559L;

    public FetcherInitialException() {
    }

    public FetcherInitialException(String s) {
        super(s);
    }

    public FetcherInitialException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FetcherInitialException(Throwable throwable) {
        super(throwable);
    }

    public FetcherInitialException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
