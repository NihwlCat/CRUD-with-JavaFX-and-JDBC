package db;

public class DBIntegrityException extends RuntimeException {
    public DBIntegrityException (String m){
        super(m);
    }
}