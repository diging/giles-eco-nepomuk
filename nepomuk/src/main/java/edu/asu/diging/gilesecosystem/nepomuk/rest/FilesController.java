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
import edu.asu.diging.gilesecosystem.nepomuk.core.files.IFilesManager;

@RestController
public class FilesController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    public final static String FILE_ID_PLACEHOLDER = "{fileId}";
    public final static String GET_FILE_URL = "/files/" + FILE_ID_PLACEHOLDER;
    
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
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
