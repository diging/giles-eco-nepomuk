package edu.asu.diging.gilesecosystem.nepomuk.core.service;

public interface ISystemMessageHandler {

    public abstract void handleError(String msg, Exception exception);

}