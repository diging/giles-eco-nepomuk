package edu.asu.nepomuk.service;


public interface IFileHandlerRegistry {

    public abstract IFileTypeHandler getHandler(String contentType);

}