package exceptions;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException() {
        super("Неверные данные учетной записи пользователя. Проверьте правильность ввода логина и пароля.");
    }

    public InvalidCredentialsException(String message) {
        super("Неверные данные учетной записи пользователя: " + message);
    }
}
