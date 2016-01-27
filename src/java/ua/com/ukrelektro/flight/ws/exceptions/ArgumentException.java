package ua.com.ukrelektro.flight.ws.exceptions;

import javax.xml.ws.WebFault;

@WebFault
public class ArgumentException extends TraceException{
    
    public ArgumentException(String string) {
        super(string);
    }
    

}
