package edu.asu.diging.gilesecosystem.nepomuk.service;

import edu.asu.diging.gilesecosystem.requests.FileType;


public interface IFileHandlerRegistry {

    public abstract IFileTypeHandler getHandler(FileType contentType);

}