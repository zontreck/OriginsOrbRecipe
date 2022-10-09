package dev.zontreck.otemod.exceptions;

public class InvalidDeserialization extends Exception
{
    public InvalidDeserialization(String error){
        super(error);
    }    
    public InvalidDeserialization(){
        super("Incorrect information was provided to the deserializer");
    }
}
