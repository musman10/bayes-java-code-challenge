package gg.bayes.challenge.rest.helper;

public enum Errors {

    MATCH_NOT_FOUND("Match not found with id = ");

    String message;

    private Errors(String message){
        this.message = message;
    }

    public String getErrorMessage(){
        return message;
    }
}
