package carsharing;

public class EmptyListException extends Exception {

    public EmptyListException() {
        super("list is empty!");
    }

    public EmptyListException(String message) {
        super(message);
    }
}
