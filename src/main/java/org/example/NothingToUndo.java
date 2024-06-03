package org.example;

public class NothingToUndo extends RuntimeException{
    public NothingToUndo(String message) {
        super(message);
    }
}
