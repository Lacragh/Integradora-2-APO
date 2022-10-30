package Exceptions;

public class NotFoundCountryID extends RuntimeException {

    public NotFoundCountryID(){
        super("The country ID does not exist in the database");
    }
}
