package cc.chipchop.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;
import java.sql.SQLException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({DataAccessException.class, SQLException.class, ConnectException.class})
    public ResponseEntity<String> handleDatabaseDown(Exception e){
        return new ResponseEntity<>( HttpStatus.SERVICE_UNAVAILABLE );
    }


}
