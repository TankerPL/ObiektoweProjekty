import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Reservation implements CSVInterface {
    private Date checkIn = null;
    private Date checkOut = null;
    private int days = 0;
    private ArrayList<Integer> roomNumbers = new ArrayList<>();
    private String guestUsername = null;

    public Reservation(int roomNumber, String guestUsername, Date checkIn, Date checkOut) {
        this.roomNumbers.add(roomNumber);
        this.guestUsername = guestUsername;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.days = getDaysBetweenDates(checkIn, checkOut);
    }

    public Reservation(List<Integer> roomNumbers, String guestUsername, Date checkIn, Date checkOut) {
        this.roomNumbers.addAll(roomNumbers);
        this.guestUsername = guestUsername;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.days = getDaysBetweenDates(checkIn, checkOut);
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public Reservation setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
        return this;
    }

    public Reservation setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
        return this;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public String getGuestUsername() {
        return guestUsername;
    }

    public Reservation setGuest(String guestUsername) {
        this.guestUsername = guestUsername;
        return this;
    }

    public List<Integer> getRoomNumbers() {
        return roomNumbers;
    }

    public Reservation addRoomNumber(int roomNumber) {
        this.roomNumbers.add(roomNumber);
        return this;
    }

    public Reservation addRoomNumber(List<Integer> roomNumbers) {
        this.roomNumbers.addAll(roomNumbers);
        return this;
    }

    public Reservation removeRoomNumber(int roomNumber) {
        this.roomNumbers.remove(roomNumber);
        return this;
    }

    public Reservation removeRoomNumber(List<Integer> roomNumbers) {
        this.roomNumbers.removeAll(roomNumbers);
        return this;
    }

    public Reservation setRoomNumbers(int roomNumber) {
        this.roomNumbers.clear();
        return addRoomNumber(roomNumber);
    }

    public Reservation setRoomNumbers(List<Integer> roomNumbers) {
        this.roomNumbers.clear();
        return addRoomNumber(roomNumbers);
    }

    public int getDays() {
        return days;
    }

    private Reservation setDays(int days) {
        this.days = days;
        return this;
    }

    private Reservation setDays(Date checkIn, Date checkOut) {
        this.days = getDaysBetweenDates(checkIn, checkOut);
        return this;
    }

    private int getDaysBetweenDates(Date checkIn, Date checkOut) {
        return Math.abs((int) ((checkIn.getTime() - checkOut.getTime()) / (1000 * 60 * 60 * 24)));
    }

    @Override
    public String toCSV() {
        return String.format("%s,%s,%s,%s", guestUsername, checkIn.getTime(), checkOut.getTime(),
                roomNumbers.stream().map(Object::toString).collect(Collectors.joining(",")));
    }
}
