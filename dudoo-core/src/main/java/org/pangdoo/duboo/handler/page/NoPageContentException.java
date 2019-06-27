package org.pangdoo.duboo.handler.page;

public class NoPageContentException extends RuntimeException {

    private static final long serialVersionUID = 5036519108713425987L;

    public NoPageContentException() {
    }

    public NoPageContentException(String s) {
        super(s);
    }

    public NoPageContentException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NoPageContentException(Throwable throwable) {
        super(throwable);
    }

    public NoPageContentException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
