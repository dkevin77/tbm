import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BookingOffice {

    private ArrayList<Reservation> allReservations;
    private Map<String, Boolean> tableAvailability;
    private static BookingOffice instance = new BookingOffice();
    private ArrayList<Table> tables;

    private BookingOffice() {
        allReservations = new ArrayList<>();
        tableAvailability = new HashMap<>();
        // Initialize table availability
        for (int i = 1; i <= 10; i++) {
            tableAvailability.put("T0" + i, true);
        }
        for (int i = 1; i <= 6; i++) {
            tableAvailability.put("F0" + i, true);
        }
        for (int i = 1; i <= 3; i++) {
            tableAvailability.put("H0" + i, true);
        }
    }

    public static BookingOffice getInstance() {
        return instance;
    }

    public ArrayList<Reservation> getAllReservations() {
        return allReservations;
    }

    public void listReservations() {
        Reservation.list(allReservations);
    }

    public Reservation addReservation(String guestName, String phoneNumber, int totPersons, String sDateDine,
            String ticketCode) throws ExBookingAlreadyExists {
        // Check if a reservation with the same guest name, phone number, and dining
        // date already exists
        for (Reservation r : allReservations) {
            if (r.getGuestName().equals(guestName) && r.getPhoneNumber().equals(phoneNumber)
                    && r.getDateDine().equals(sDateDine)) {
                throw new ExBookingAlreadyExists();
            }
        }

        // Create and add the reservation if no duplicate is found
        Reservation r = new Reservation(guestName, phoneNumber, totPersons, sDateDine, ticketCode);
        allReservations.add(r);
        Collections.sort(allReservations);
        return r;
    }

    public void removeReservation(Reservation r) {
        allReservations.remove(r);
    }

    public void addReservation(Reservation r) {
        allReservations.add(r);
        Collections.sort(allReservations);
    }

    public Reservation findReservation(String sDateDine, String ticketCode) {
        for (Reservation reservation : allReservations) {
            if (reservation.getDateDine().equals(sDateDine) && reservation.getTicketCode().equals(ticketCode)) {
                return reservation;
            }
        }
        return null; // Reservation not found
    }

    public void assignTables(String[] tableCodes) {
        ArrayList<Table> tables = new ArrayList<>();
        for (String code : tableCodes) {
            tables.add(new Table(code));
        }
        this.tables.addAll(tables);
        new RStateTableAllocated(tables);
    }

    public Reservation assignTable(String sDateDine, String ticketCode, String[] tableCodes) throws ExBookingNotFound {
        Reservation reservation = findReservation(sDateDine, ticketCode);
        if (reservation != null) {
            Set<String> assignedTableCodes = new HashSet<>(); // To track assigned table codes

            // Iterate through table codes provided in the command
            for (String tableCode : tableCodes) {
                // Check if the table is available for the specified date and has not already
                // been assigned
                if (isTableAvailableForDate(tableCode, sDateDine) && !assignedTableCodes.contains(tableCode)) {
                    // Add the table code to the reservation and mark it as unavailable
                    reservation.getTables().add(new Table(tableCode));
                    assignedTableCodes.add(tableCode); // Add the table code to the set
                    tableAvailability.put(tableCode, false);
                }
            }
            return reservation;
        } else {
            throw new ExBookingNotFound();
        }
    }

    private boolean isTableAvailableForDate(String table, String sDateDine) {
        for (Reservation reservation : allReservations) {
            if (reservation.getDateDine().equals(sDateDine)) {
                for (Table assignedTable : reservation.getTables()) {
                    if (assignedTable.getTableCode().equals(table)) {
                        return false; // Table is not available for the specified date
                    }
                }
            }
        }
        return isTableAvailable(table); // Check general availability if no reservation for the specified date
    }

    public boolean isTableAvailable(String table) {
        return tableAvailability.getOrDefault(table, false);
    }

    public void cancel(String sDateDine, String ticketCode) throws ExBookingNotFound {
        Reservation reservation = findReservation(sDateDine, ticketCode);
        if (reservation != null) {
            allReservations.remove(reservation);
            // Mark tables as available again
            for (Table table : reservation.getTables()) {
                tableAvailability.put(table.getTableCode(), true);
            }
        } else {
            throw new ExBookingNotFound();
        }
    }

    public void rebook(Reservation cancelledReservation, Reservation newReservation) {
        allReservations.remove(cancelledReservation);
        allReservations.add(newReservation);
        // Mark tables as available again for the cancelled reservation
        for (Table table : cancelledReservation.getTables()) {
            tableAvailability.put(table.getTableCode(), true);
        }
    }
}
