import java.util.ArrayList;
import java.util.HashMap;

public class CmdRequest extends RecordedCommand {

    private Reservation r;
    private static HashMap<String, Integer> maxTicketCodes = new HashMap<>();

    public void execute(String[] cmdParts) {
        BookingOffice b = BookingOffice.getInstance();
        String sDateDine = cmdParts[4];

        try {
            checkDate(sDateDine);
            checkNumberFormat(cmdParts[3]);
            String ticketCode = generateTicketCode(cmdParts, sDateDine, b.getAllReservations());
            r = b.addReservation(cmdParts[1], cmdParts[2], Integer.parseInt(cmdParts[3]), sDateDine, ticketCode);
            SharedContext.setTotalPersons(Integer.parseInt(cmdParts[3]));
            // Update the maximum ticket code for the current day
            maxTicketCodes.put(sDateDine, Integer.parseInt(ticketCode));
            addUndoCommand(this);
            clearRedoList();
            System.out.println("Done. Ticket code for " + sDateDine + ": " + ticketCode);
        } catch (ExBookingAlreadyExists e) {
            System.out.println(e.getMessage());
        } catch (ExDateHasAlreadyPassed e) {
            System.out.println(e.getMessage());
        } catch (ExWrongNumberFormat e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkDate(String newDate) throws ExDateHasAlreadyPassed {
        SystemDate sd = SystemDate.getInstance();
        String currentDate = sd.toString();
        // Compare the new date with the current date
        if (newDate.compareTo(currentDate) < 0) {
            throw new ExDateHasAlreadyPassed();
        }
    }

    private String generateTicketCode(String[] cmdParts, String sDateDine, ArrayList<Reservation> allReservations)
            throws ExBookingAlreadyExists {
        // Implement the logic to generate ticket code here
        // Check if there's already a booking for the same person and dining date
        for (Reservation reservation : allReservations) {
            if (reservation.getDateDine().equals(sDateDine)
                    && reservation.getGuestName().equals(cmdParts[1])
                    && reservation.getPhoneNumber().equals(cmdParts[2])) {
                throw new ExBookingAlreadyExists();
            }
        }

        // If no existing booking, generate the ticket code
        int count = maxTicketCodes.getOrDefault(sDateDine, 0) + 1;
        return Integer.toString(count);
    }

    private void checkNumberFormat(String arg) throws ExWrongNumberFormat {
        if (!NumberFormatChecker.isValidInteger(arg)) {
            throw new ExWrongNumberFormat();
        }
    }

    @Override
    public void undoMe() {
        BookingOffice b = BookingOffice.getInstance();
        b.removeReservation(r);
        // Reset the maximum ticket code for the undone booking's date
        maxTicketCodes.put(r.getDateDine(), maxTicketCodes.getOrDefault(r.getDateDine(), 0) - 1);
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        BookingOffice b = BookingOffice.getInstance();
        b.addReservation(r);
        // Update the maximum ticket code for the redone booking's date
        maxTicketCodes.put(r.getDateDine(), Integer.parseInt(r.getTicketCode()));
        addUndoCommand(this);
    }
}
