package smartvault;

public class InsufficientFundsException extends Exception {

    private double gap;

    public InsufficientFundsException(double gap) {
        super(String.format("Not enough balance. You're short by $%.2f", gap));
        this.gap = gap;
    }

    public double getGap() {
        return gap;
    }
}
