import java.util.ArrayList;

public class Reservation implements Comparable<Reservation> {
    private String guestName;
    private String phoneNumber;
    private int totPersons;
    private Day dateDine;
    private Day dateRequest;
    private String ticketCode;
    private ArrayList<Table> tables;
    private RState state;

    public Reservation(String guestName, String phoneNumber, int totPersons, String sDateDine, String ticketCode) {
        this.guestName = guestName;
        this.phoneNumber = phoneNumber;
        this.totPersons = totPersons;
        this.dateDine = new Day(sDateDine);
        this.dateRequest = SystemDate.getInstance().clone();
        this.ticketCode = ticketCode;
        this.tables = new ArrayList<>();
        this.state = new RStatePending();
    }

    public void process() {
        state.process(this);
    }

    public void setState(RState state) {
        this.state = state;
    }

    public RState getState() {
        return state;
    }

    public String getStatus() {
        return state.getStatus(this);
    }

    public static void list(ArrayList<Reservation> allReservations) {
        System.out.printf("%-13s%-11s%-14s%-25s%-11s%s\n", "Guest Name", "Phone", "Request Date",
                "Dining Date and Ticket", "#Persons", "Status");
        for (Reservation r : allReservations) {
            String status = (r.tables.isEmpty()) ? "Pending" : "Table assigned: " + r.getTableCodesAsString();
            System.out.printf("%-13s%-11s%-14s%-27s%-9s%s\n", r.getGuestName(), r.phoneNumber, r.dateRequest,
                    r.dateDine + " (Ticket " + r.ticketCode + ")", r.totPersons, status);
        }
    }

    public String getTableCodesAsString() {
        StringBuilder sb = new StringBuilder();
        for (Table table : tables) {
            sb.append(table.getTableCode()).append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public int compareTo(Reservation another) {
        // First compare by guest name
        int nameComparison = this.guestName.compareTo(another.guestName);
        if (nameComparison != 0) {
            return nameComparison;
        } else {
            // If guest names are the same, compare by phone number
            int phoneNumberComparison = this.phoneNumber.compareTo(another.phoneNumber);
            if (phoneNumberComparison != 0) {
                return phoneNumberComparison;
            } else {
                // If phone numbers are the same, compare by dining date
                return this.dateDine.toString().compareTo(another.dateDine.toString());
            }
        }
    }

    public String getDateDine() {
        return dateDine.toString();
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void assignTables(ArrayList<Table> tables) {
        this.tables.addAll(tables);
        this.state = new RStateTableAllocated(tables);
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public int getTotPersons() {
        return totPersons;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
