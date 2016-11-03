package edu.asu.nepomuk.core;

import edu.asu.nepomuk.db4o.IStorableObject;


public interface IUpload extends IStorableObject {

    public abstract String getId();

    public abstract void setId(String id);

    public abstract String getUsername();

    public abstract void setUsername(String username);

    public abstract String getCreatedDate();

    public abstract void setCreatedDate(String createdDate);

}