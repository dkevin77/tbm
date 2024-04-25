import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class CmdListTableAllocations implements Command {

    @Override
    public void execute(String[] cmdParts) {
        BookingOffice bookingOffice = BookingOffice.getInstance();
        String sDateDine = cmdParts[1];

        Set<String> allocatedTables = new HashSet<>();
        ArrayList<String> pendingReservations = new ArrayList<>();
        int totalPendingRequests = 0;
        int totalPersonsPending = 0;

        System.out.println("\nAllocated tables:");
        if (!bookingOffice.getAllReservations().stream()
                .anyMatch(reservation -> reservation.getDateDine().equals(sDateDine)
                        && !reservation.getTables().isEmpty())) {
            System.out.println("[None]");
        } else {
            for (Reservation reservation : bookingOffice.getAllReservations()) {
                if (reservation.getDateDine().equals(sDateDine)) {
                    if (!reservation.getTables().isEmpty()) {
                        for (Table table : reservation.getTables()) {
                            allocatedTables.add(table.getTableCode()); // Assuming Table has a method getCode()
                            System.out.println(table.getTableCode() + " (Ticket " + reservation.getTicketCode() + ")");
                        }
                    } else {
                        totalPendingRequests++;
                        totalPersonsPending += reservation.getTotPersons();
                        pendingReservations.add(reservation.getGuestName());
                    }
                }
            }
        }

        System.out.println("\nAvailable tables:");
        Set<String> allTables = new TreeSet<>(); // Use TreeSet for sorted order
        allTables.add("T01");
        allTables.add("T02");
        allTables.add("T03");
        allTables.add("T04");
        allTables.add("T05");
        allTables.add("T06");
        allTables.add("T07");
        allTables.add("T08");
        allTables.add("T09");
        allTables.add("T10");
        allTables.add("F01");
        allTables.add("F02");
        allTables.add("F03");
        allTables.add("F04");
        allTables.add("F05");
        allTables.add("F06");
        allTables.add("H01");
        allTables.add("H02");
        allTables.add("H03");

        // Create three separate StringBuilders for each table type
        StringBuilder availableTTables = new StringBuilder();
        StringBuilder availableFTables = new StringBuilder();
        StringBuilder availableHTables = new StringBuilder();

        // Iterate through all tables and append to the corresponding StringBuilder
        for (String table : allTables) {
            if (!allocatedTables.contains(table)) {
                if (table.startsWith("T")) {
                    availableTTables.append(table).append(" ");
                } else if (table.startsWith("F")) {
                    availableFTables.append(table).append(" ");
                } else if (table.startsWith("H")) {
                    availableHTables.append(table).append(" ");
                }
            }
        }

        // Combine all available tables and print
        StringBuilder allAvailableTables = new StringBuilder();
        allAvailableTables.append(availableTTables).append(availableFTables).append(availableHTables);
        System.out.println(allAvailableTables.toString());

        if (!pendingReservations.isEmpty()) {
            System.out.println("\nTotal number of pending requests = " + totalPendingRequests
                    + " (Total number of persons = " + totalPersonsPending + ")");
        } else {
            System.out.println("\nTotal number of pending requests = 0 (Total number of persons = 0)");
        }
    }
}
