package org.pangdoo.duboo.handler.page;

public class PageDocumentExistException extends RuntimeException {

    private static final long serialVersionUID = -4000445678880609866L;

    public PageDocumentExistException() {
    }

    public PageDocumentExistException(String s) {
        super(s);
    }

    public PageDocumentExistException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PageDocumentExistException(Throwable throwable) {
        super(throwable);
    }

    public PageDocumentExistException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
