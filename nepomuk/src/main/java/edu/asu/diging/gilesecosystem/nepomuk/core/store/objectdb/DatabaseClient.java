package edu.asu.diging.gilesecosystem.nepomuk.core.store.objectdb;

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.gilesecosystem.nepomuk.core.exception.UnstorableObjectException;
import edu.asu.diging.gilesecosystem.nepomuk.core.store.IDatabaseClient;
import edu.asu.diging.gilesecosystem.nepomuk.core.store.IStorableObject;

public abstract class DatabaseClient<T extends IStorableObject> implements IDatabaseClient<T> {

    @Override
    public String generateId() {
        String id = null;
        while (true) {
            id = getIdPrefix() + generateUniqueId();
            Object existingFile = getById(id);
            if (existingFile == null) {
                break;
            }
        }
        return id;
    }
    
    protected List<? extends T> searchByProperty(String propName, String propValue, Class<? extends T> clazz) {
        String query = "SELECT t FROM " + clazz.getName()  + " t WHERE t." + propName + " = '" + propValue + "'";
        TypedQuery<? extends T> docs = getClient().createQuery(query, clazz);
        return docs.getResultList();
    }
    
    @Transactional
    @Override
    public T store(T element) throws UnstorableObjectException {
        if (element.getId() == null) {
            throw new UnstorableObjectException("The object does not have an id.");
        }
        
        EntityManager em = getClient();
        em.persist(element);
        em.flush();
        return element;
    }
    
    @Transactional
    @Override
    public void delete(T element) {
        EntityManager em = getClient();
        em.remove(element);
    }

    protected abstract String getIdPrefix();

    protected abstract Object getById(String id);
    
    protected abstract EntityManager getClient();

    /**
     * This methods generates a new 6 character long id. Note that this method
     * does not assure that the id isn't in use yet.
     * 
     * Adapted from
     * http://stackoverflow.com/questions/9543715/generating-human-readable
     * -usable-short-but-unique-ids
     * 
     * @return 12 character id
     */
    protected String generateUniqueId() {
        char[] chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
                .toCharArray();

        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            builder.append(chars[random.nextInt(62)]);
        }

        return builder.toString();
    }
}
