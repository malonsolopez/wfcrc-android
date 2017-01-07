package com.wfcrc.repository;

/**
 * Created by maria on 1/6/17.
 */
public class RepositoryException extends Exception {

    private String repositoryMessage = null;

    public RepositoryException(String message){
        super(message);
    }

    public RepositoryException(Throwable cause) {
        super(cause);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryException(String message, String repoMessage){
        super(message);
        this.repositoryMessage = repoMessage;
    }

    public RepositoryException(Throwable cause, String repoMessage) {
        super(cause);
        this.repositoryMessage = repoMessage;
    }

    public RepositoryException(String message, Throwable cause, String repoMessage) {
        super(message, cause);
        this.repositoryMessage = repoMessage;
    }

    public String getRepositoryMessage() {
        return repositoryMessage;
    }

    public void setRepositoryMessage(String repositoryMessage) {
        this.repositoryMessage = repositoryMessage;
    }

}
