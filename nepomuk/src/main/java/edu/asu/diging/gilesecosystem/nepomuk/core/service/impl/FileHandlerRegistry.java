package edu.asu.diging.gilesecosystem.nepomuk.core.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileHandlerRegistry;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.IFileTypeHandler;
import edu.asu.diging.gilesecosystem.requests.FileType;

@Service
public class FileHandlerRegistry implements IFileHandlerRegistry {

    @Autowired
    private ApplicationContext ctx;
    
    private Map<FileType, IFileTypeHandler> handlers;
    
    @PostConstruct
    public void init() {
        handlers = new HashMap<FileType, IFileTypeHandler>();
        
        Map<String, IFileTypeHandler> ctxMap = ctx.getBeansOfType(IFileTypeHandler.class);
        Iterator<Entry<String, IFileTypeHandler>> iter = ctxMap.entrySet().iterator();
        
        while(iter.hasNext()){
            Entry<String, IFileTypeHandler> handlerEntry = iter.next();
            IFileTypeHandler handler = (IFileTypeHandler) handlerEntry.getValue();
            for (FileType type : handler.getHandledFileTypes()) {
                handlers.put(type, handler);
            }
        }
    }
    
    /* (non-Javadoc)
     * @see edu.asu.giles.service.impl.IFileHandlerRegistry#getHandler(java.lang.String)
     */
    @Override
    public IFileTypeHandler getHandler(FileType contentType) {
        IFileTypeHandler handler = handlers.get(contentType);
        if (handler == null) {
            handler = handlers.get(FileType.OTHER);
        }
        return handler;
    }
    
}
