package org.pangdoo.duboo.processor.page;

public class PageIOException extends RuntimeException {

    private static final long serialVersionUID = 5036519108713425987L;

    public PageIOException() {
    }

    public PageIOException(String s) {
        super(s);
    }

    public PageIOException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PageIOException(Throwable throwable) {
        super(throwable);
    }

    public PageIOException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
