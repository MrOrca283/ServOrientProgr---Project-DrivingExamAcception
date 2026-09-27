package edu.rutmiit.candidateapi.candidateapicontract.exception;

public class BlankCodeAlreadyExistsException extends RuntimeException {
    public BlankCodeAlreadyExistsException(String blankCode) {
        super("Medblank with blankCode=" + blankCode + " already exists");
    }
}