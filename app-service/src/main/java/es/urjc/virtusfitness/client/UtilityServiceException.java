package es.urjc.virtusfitness.client;

/** Thrown when a call to the remote utility-service cannot be fulfilled. */
public class UtilityServiceException extends RuntimeException {

  public UtilityServiceException(String message) {
    super(message);
  }

  public UtilityServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
