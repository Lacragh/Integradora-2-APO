package Exceptions;

public class NotFoundCountryID extends Exception {

    public NotFoundCountryID(){
        super("The country ID does not exist in the database");
    }
}
