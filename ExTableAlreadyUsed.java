public class ExTableAlreadyUsed extends Exception {
    public ExTableAlreadyUsed(String tableCode) {
        super(String.format("Table %s is already reserved by another booking!", tableCode));
    }
}
