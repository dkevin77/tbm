public class ExTableAlreadyBooked extends Exception {
    public ExTableAlreadyBooked(String tableCode) {
        super(String.format("Table %s already assigned for this booking", tableCode));
    }
}