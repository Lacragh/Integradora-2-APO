package Exceptions;

public class FormatIncorrect extends RuntimeException{

    public FormatIncorrect(){
        super("The format of the command is incorrect");
    }
}
