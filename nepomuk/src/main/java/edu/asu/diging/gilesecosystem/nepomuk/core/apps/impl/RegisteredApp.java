package edu.asu.diging.gilesecosystem.nepomuk.core.apps.impl;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import edu.asu.diging.gilesecosystem.nepomuk.core.apps.IRegisteredApp;

@Entity
public class RegisteredApp implements IRegisteredApp {

    @Id private String id;
    private String name;
    
    @ElementCollection @LazyCollection(LazyCollectionOption.FALSE) 
    private List<String> tokenIds;
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<String> getTokenIds() {
        return tokenIds;
    }

    @Override
    public void setTokenIds(List<String> tokenIds) {
        this.tokenIds = tokenIds;
    }     
    
}
