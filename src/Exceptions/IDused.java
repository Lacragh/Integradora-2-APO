package Exceptions;

public class IDused extends Exception{

    public IDused(){
        super("The id is already used");
    }
}
