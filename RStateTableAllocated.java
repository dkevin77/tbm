import java.util.ArrayList;

public class RStateTableAllocated implements RState {
    private ArrayList<Table> tables;

    // Constructor accepting an ArrayList<Table>
    public RStateTableAllocated(ArrayList<Table> tables) {
        this.tables = tables;
    }

    @Override
    public void process(Reservation reservation) {
        // Process the reservation in the table allocated state
    }

    @Override
    public String getStatus(Reservation reservation) {
        return "Table assigned: " + tablesToString();
    }

    private String tablesToString() {
        StringBuilder builder = new StringBuilder();
        for (Table table : tables) {
            builder.append(table.getTableCode()).append(" ");
        }
        return builder.toString().trim();
    }
}
