package exceptions;

public class NotFoundStationException extends RuntimeException {

  public NotFoundStationException(String message) {
    super(message);
  }
}
