public class ExTableCodeNotFound extends Exception {
    public ExTableCodeNotFound(String tableCode) {
        super(String.format("Table code %s not found!", tableCode));
    }
}
