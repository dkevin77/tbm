public class CmdListReservations implements Command {

    public void execute(String[] cmdParts) {
        BookingOffice b = BookingOffice.getInstance();
        b.listReservations();
    }

}