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

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public String getGuestUsername() {
        return guestUsername;
    }

    public void setGuest(String guestUsername) {
        this.guestUsername = guestUsername;
    }

    public List<Integer> getRoomNumbers() {
        return roomNumbers;
    }

    public void setRoomNumbers(int roomNumber) {
        this.roomNumbers.clear();
        this.roomNumbers.add(roomNumber);
    }

    public void setRoomNumbers(List<Integer> roomNumbers) {
        this.roomNumbers.clear();
        this.roomNumbers.addAll(roomNumbers);
    }

    public int getDays() {
        return days;
    }

    private void setDays(int days) {
        this.days = days;
    }

    private void setDays(Date checkIn, Date checkOut) {
        this.days = getDaysBetweenDates(checkIn, checkOut);
    }

    private int getDaysBetweenDates(Date checkIn, Date checkOut) {
        return Math.abs((int) ((checkIn.getTime() - checkOut.getTime()) / (1000 * 60 * 60 * 24)));
    }

    @Override
    public String toCSV() {
        return String.format("%s,%s,%s,%s", guestUsername, checkIn, checkOut,
                roomNumbers.stream().map(Object::toString).collect(Collectors.joining(",")));
    }
}
