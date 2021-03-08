package exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {
    private Map<String,String> errors = new HashMap<>();

    public ValidationException (String m){
        super(m);
    }

    public Map<String,String> getErrors(){
        return this.errors;
    }

    public void addErrors(String name, String cause){
        errors.put(name,cause);
    }

}
