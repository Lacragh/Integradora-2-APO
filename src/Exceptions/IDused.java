package Exceptions;

public class IDused extends RuntimeException{

    public IDused(){
        super("The id is already used");
    }
}
