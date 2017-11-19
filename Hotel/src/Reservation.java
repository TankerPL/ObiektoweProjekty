import java.util.Date;

public class Reservation {
    private Date checkIn = null;
    private Date checkOut = null;
    private int days = 0;
    private int roomNumber = 0;
    private String guestUsername = null;

    public Reservation(int roomNumber, String guestUsername, Date checkIn, Date checkOut) {
        this.roomNumber = roomNumber;
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

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
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
}
