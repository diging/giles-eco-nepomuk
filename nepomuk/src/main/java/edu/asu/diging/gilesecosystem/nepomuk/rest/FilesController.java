package edu.asu.diging.gilesecosystem.nepomuk.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.asu.diging.gilesecosystem.nepomuk.core.aspect.annotations.AppTokenCheck;
import edu.asu.diging.gilesecosystem.nepomuk.core.domain.IFile;
import edu.asu.diging.gilesecosystem.nepomuk.core.exception.NoUniqueResultException;
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesManager;
import edu.asu.diging.gilesecosystem.nepomuk.core.service.ISystemMessageHandler;

@RestController
public class FilesController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISystemMessageHandler systemMessageHandler;
    
    public final static String FILE_ID_PLACEHOLDER = "{fileId}";
    public final static String GET_FILE_URL = "/files/" + FILE_ID_PLACEHOLDER;
    
    public final static String UPLOAD_ID_PLACEHOLDER = "{uploadId}";
    public final static String DOCUMENT_ID_PLACEHOLDER = "{documentId}";
    public final static String FILENAME_PLACEHOLDER = "{filename:.+?}";
    public final static String GET_FILE_BY_FILENAME = "/files/" + UPLOAD_ID_PLACEHOLDER + "/" + DOCUMENT_ID_PLACEHOLDER + "/" + FILENAME_PLACEHOLDER;
    
    @Autowired
    private IFilesManager filesManager;
    
    @AppTokenCheck
    @RequestMapping(value = GET_FILE_URL)
    public ResponseEntity<String> getFile(@PathVariable String fileId, HttpServletResponse response,
            HttpServletRequest request) {

        IFile file = filesManager.getFile(fileId);
        if (file == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        
        byte[] content = filesManager.getFileContent(file);
        response.setContentType(file.getContentType());
        response.setContentLength(content.length);
        response.setHeader("Content-disposition", "filename=\"" + file.getFilename() + "\""); 
        try {
            response.getOutputStream().write(content);
            response.getOutputStream().close();
        } catch (IOException e) {
            logger.error("Could not write to output stream.", e);
            systemMessageHandler.handleError("Could not write to output stream.", e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }
    
    @RequestMapping(value = GET_FILE_BY_FILENAME)
    public ResponseEntity<String> getFileByFilename(@PathVariable String uploadId, @PathVariable String documentId, @PathVariable String filename, HttpServletResponse response,
            HttpServletRequest request) {
        IFile file;
        try {
            file = filesManager.getFile(uploadId, documentId, filename);
        } catch (NoUniqueResultException e) {
            logger.error("Could not retrieve file.", e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        if (file == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        
        byte[] content = filesManager.getFileContent(file);
        response.setContentType(file.getContentType());
        response.setContentLength(content.length);
        response.setHeader("Content-disposition", "filename=\"" + file.getFilename() + "\""); 
        try {
            response.getOutputStream().write(content);
            response.getOutputStream().close();
        } catch (IOException e) {
            logger.error("Could not write to output stream.", e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
