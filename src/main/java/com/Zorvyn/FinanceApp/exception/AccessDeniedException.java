package com.Zorvyn.FinanceApp.exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String msg){
        super(msg);
    }
}
