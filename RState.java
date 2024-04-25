public interface RState {
    void process(Reservation reservation);

    String getStatus(Reservation reservation);
}