import java.util.Arrays;
import java.util.List;

public class CmdAssignTable extends RecordedCommand {
    private Reservation reservation;
    private static final List<String> VALID_TABLE_CODES = Arrays.asList(
            "T01", "T02", "T03", "T04", "T05", "T06", "T07", "T08", "T09", "T10",
            "F01", "F02", "F03", "F04", "F05", "F06",
            "H01", "H02", "H03");

    public void execute(String[] cmdParts) {
        try {
            BookingOffice bookingOffice = BookingOffice.getInstance();
            String sDateDine = cmdParts[1];
            String ticketCode = cmdParts[2];
            String[] tableCodes = Arrays.copyOfRange(cmdParts, 3, cmdParts.length);
            reservation = bookingOffice.assignTable(sDateDine, ticketCode, tableCodes);
            checkNumberFormat(cmdParts[2]);
            checkDate(sDateDine);
            checkValidTableCodes(tableCodes);

            // checkUsedTable(sDateDine, tableCodes);
            // removeUsedTables(tableCodes);

            // int totalPersons = SharedContext.getTotalPersons();
            // checkSeatsCapacity(tableCodes, totalPersons);

            reservation = bookingOffice.assignTable(sDateDine, ticketCode, tableCodes);
            addUndoCommand(this);
            clearRedoList();
            System.out.println("Done.");

        } catch (ExWrongNumberFormat e) {
            System.out.println(e.getMessage());
        } catch (ExBookingNotFound e) {
            System.out.println(e.getMessage());
        } catch (ExDateHasAlreadyPassed e) {
            System.out.println(e.getMessage());
            // } catch (ExNotEnoughSeats e) {
            // System.out.println(e.getMessage());
            // } catch (ExTableAlreadyUsed e) {
            // System.out.println(e.getMessage());
            // return;
        } catch (ExTableCodeNotFound e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void undoMe() {
        BookingOffice bookingOffice = BookingOffice.getInstance();
        try {
            bookingOffice.cancel(reservation.getDateDine(), reservation.getTicketCode());
        } catch (ExBookingNotFound e) {
            System.out.println(e.getMessage());
        }
        addRedoCommand(this);
    }

    public void redoMe() {
        addUndoCommand(this);
    }

    private void checkDate(String date) throws ExDateHasAlreadyPassed {
        SystemDate sd = SystemDate.getInstance();
        String currentDate = sd.toString();

        // Compare the date with the current date
        if (date.compareTo(currentDate) < 0) {
            throw new ExDateHasAlreadyPassed();
        }
    }

    // private void checkSeatsCapacity(String[] tableCodes, int totalPersons) throws
    // ExNotEnoughSeats {
    // int totalMaxCapacity = 0;

    // for (String tableCode : tableCodes) {
    // if (tableCode.startsWith("T")) {
    // totalMaxCapacity += 2; // Table T can seat 2 persons
    // } else if (tableCode.startsWith("F")) {
    // totalMaxCapacity += 4; // Table F can seat 4 persons
    // } else if (tableCode.startsWith("H")) {
    // totalMaxCapacity += 8; // Table H can seat 8 persons
    // }
    // }

    // if (totalPersons > totalMaxCapacity) {
    // throw new ExNotEnoughSeats();
    // }
    // }

    // private void checkUsedTable(String sDateDine, String[] tableCodes) throws
    // ExTableAlreadyUsed {
    // BookingOffice bookingOffice = BookingOffice.getInstance();
    // ArrayList<Reservation> reservations = bookingOffice.getAllReservations();

    // // Iterate through reservations for the same dining date
    // for (Reservation reservation : reservations) {
    // if (reservation.getDateDine().equals(sDateDine)) {
    // for (String tableCode : tableCodes) {
    // boolean tableFound = false;
    // for (Table table : reservation.getTables()) {
    // if (table.getTableCode().equals(tableCode)) {
    // tableFound = true;
    // break;
    // }
    // }
    // if (tableFound) {
    // throw new ExTableAlreadyUsed(tableCode);
    // }
    // }
    // }
    // }
    // }

    // private void removeUsedTables(String[] tableCodes) {
    // allTables.removeAll(Arrays.asList(tableCodes));
    // }

    private void checkValidTableCodes(String[] tableCodes) throws ExTableCodeNotFound {
        for (String tableCode : tableCodes) {
            if (!VALID_TABLE_CODES.contains(tableCode)) {
                throw new ExTableCodeNotFound(tableCode);
            }
        }
    }

    private void checkNumberFormat(String arg) throws ExWrongNumberFormat {
        if (!NumberFormatChecker.isValidInteger(arg)) {
            throw new ExWrongNumberFormat();
        }
    }

}
