package Exceptions;

public class FormatIncorrect extends Exception{

    public FormatIncorrect(){
        super("The format of the command is incorrect");
    }
}
