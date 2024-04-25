public class CmdCancel implements Command {

    @Override
    public void execute(String[] cmdParts) {
        BookingOffice bookingOffice = BookingOffice.getInstance();
        String sDateDine = cmdParts[1];
        String ticketCode = cmdParts[2];

        try {
            // Check if the date has already passed
            checkDate(sDateDine);
            checkNumberFormat(cmdParts[2]);
            bookingOffice.cancel(sDateDine, ticketCode);
            System.out.println("Done.");
        } catch (ExBookingNotFound e) {
            System.out.println(e.getMessage());
        } catch (ExDateHasAlreadyPassed e) {
            System.out.println(e.getMessage());
        } catch (ExWrongNumberFormat e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkDate(String date) throws ExDateHasAlreadyPassed {
        SystemDate sd = SystemDate.getInstance();
        String currentDate = sd.toString();

        // Compare the date with the current date
        if (date.compareTo(currentDate) < 0) {
            throw new ExDateHasAlreadyPassed();
        }
    }

    private void checkNumberFormat(String arg) throws ExWrongNumberFormat {
        if (!NumberFormatChecker.isValidInteger(arg)) {
            throw new ExWrongNumberFormat();
        }
    }
}
