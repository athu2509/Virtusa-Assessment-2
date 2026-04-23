package smartvault;

public class UnauthorizedAccessException extends Exception {

    public UnauthorizedAccessException(String what) {
        super("Sorry, only admins can do: " + what);
    }
}
