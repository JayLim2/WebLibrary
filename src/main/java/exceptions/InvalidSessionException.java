package exceptions;

public class InvalidSessionException extends Exception {
    public InvalidSessionException() {
        super("Сессия сформирована некорректно.");
    }

    public InvalidSessionException(String message) {
        super("Сессия сформирована некорректно.\nПодробности: " + message);
    }
}
