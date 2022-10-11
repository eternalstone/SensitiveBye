package com.github.eternalstone.attachment.file.handle;

/**
 * to do something
 *
 * @author Justzone on 2022/10/8 20:47
 */
public abstract class AbstractFileHandler implements IFileHandler {

    protected IFileHandler nextHandler;

    public void setNextHandler(IFileHandler handler) {
        this.nextHandler = handler;
    }
}
