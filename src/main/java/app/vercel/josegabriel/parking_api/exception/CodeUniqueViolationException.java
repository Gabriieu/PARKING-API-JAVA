package app.vercel.josegabriel.parking_api.exception;

public class CodeUniqueViolationException extends RuntimeException {

    public CodeUniqueViolationException(String message) {
        super(message);
    }
}
