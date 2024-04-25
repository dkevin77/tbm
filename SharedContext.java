public class SharedContext {
    private static int totalPersons;

    public static void setTotalPersons(int value) {
        totalPersons = value;
    }

    public static int getTotalPersons() {
        return totalPersons;
    }
}
