import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CmdSuggestTable implements Command {

    @Override
    public void execute(String[] cmdParts) {
        try {
            BookingOffice bookingOffice = BookingOffice.getInstance();
            String sDateDine = cmdParts[1];
            checkNumberFormat(cmdParts[2]);
            int ticketNumber = Integer.parseInt(cmdParts[2]);

            Reservation targetReservation = findReservation(bookingOffice, sDateDine, ticketNumber);

            int totalPersons = targetReservation.getTotPersons();
            Map<String, Integer> tableCapacity = new HashMap<>();
            tableCapacity.put("H", 8);
            tableCapacity.put("F", 4);
            tableCapacity.put("T", 2);

            System.out.print("Suggestion for " + totalPersons + " persons: ");

            // Get available tables for the specified date
            List<String> availableTables = getAvailableTablesForDate(sDateDine, bookingOffice);

            StringBuilder suggestion = new StringBuilder();
            int remainingPersons = totalPersons;

            // First suggest H tables
            for (int i = 1; i <= 3; i++) {
                if (remainingPersons >= 7 && availableTables.contains("H0" + i)) {
                    suggestion.append("H0").append(i).append(" ");
                    remainingPersons -= 8;
                }
            }

            // Then suggest F tables
            for (int i = 1; i <= 6; i++) {
                if (remainingPersons >= 3 && availableTables.contains("F0" + i)) {
                    suggestion.append("F0").append(i).append(" ");
                    remainingPersons -= 4;
                }
            }

            // Finally suggest T tables
            for (int i = 1; i <= 10; i++) {
                if (remainingPersons >= 1 && availableTables.contains("T0" + i)) {
                    suggestion.append("T0").append(i).append(" ");
                    remainingPersons -= 2;
                }
            }

            System.out.println(suggestion.toString().trim());
        } catch (ExBookingNotFound e) {
            System.out.println(e.getMessage());
        } catch (ExWrongNumberFormat e) {
            System.out.println(e.getMessage());
        }
    }

    private Reservation findReservation(BookingOffice bookingOffice, String sDateDine, int ticketNumber)
            throws ExBookingNotFound {
        for (Reservation reservation : bookingOffice.getAllReservations()) {
            if (reservation.getDateDine().equals(sDateDine)
                    && Integer.parseInt(reservation.getTicketCode()) == ticketNumber) {
                return reservation;
            }
        }
        throw new ExBookingNotFound();
    }

    // Helper method to get available tables for the specified date
    private List<String> getAvailableTablesForDate(String sDateDine, BookingOffice bookingOffice) {
        List<String> availableTables = new ArrayList<>();
        Set<String> allTables = new TreeSet<>(); // All possible tables
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

        Set<String> allocatedTables = new HashSet<>();
        for (Reservation reservation : bookingOffice.getAllReservations()) {
            if (reservation.getDateDine().equals(sDateDine) && !reservation.getTables().isEmpty()) {
                for (Table table : reservation.getTables()) {
                    allocatedTables.add(table.getTableCode()); // Assuming Table has a method getCode()
                }
            }
        }

        // Add tables that are not allocated for the specified date
        for (String table : allTables) {
            if (!allocatedTables.contains(table)) {
                availableTables.add(table);
            }
        }

        return availableTables;
    }

    private void checkNumberFormat(String arg) throws ExWrongNumberFormat {
        if (!NumberFormatChecker.isValidInteger(arg)) {
            throw new ExWrongNumberFormat();
        }
    }
}
