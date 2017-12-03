import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Console {
    private Hotel hotel = Hotel.getInstance();
    private PrintStream out = System.out;
    private Scanner in = new Scanner(System.in);
    private Guest guest = null;
    private boolean isAdmin = false;
    private states state = states.MAINMENU;

    private enum states {
        MAINMENU,
        REGISTERRESERVATION,
        CANCELRESERVATION,
        EDITRESERVATION,
        LOGINGUEST,
        REGISTERGUEST,
        EDITGUEST,
        ADDROOMS,
        REMOVEROOMS,
        EDITROOMS,
        EXIT
    }

    public void run() {
        while (true) {
            out.println(state);
            switch (state) {
                case MAINMENU:
                    mainMenu();
                    break;
                case REGISTERRESERVATION:
                    registerReservations();
                    break;
                case CANCELRESERVATION:
                    cancelReservations();
                    break;
                case EDITRESERVATION:
                    editReservations();
                    break;
                case LOGINGUEST:
                    loginGuest();
                    break;
                case REGISTERGUEST:
                    registerGuest();
                    break;
                case EDITGUEST:
                    editGuest();
                    break;
                case ADDROOMS:
                    addRooms();
                    break;
                case REMOVEROOMS:
                    removeRooms();
                    break;
                case EDITROOMS:
                    editRooms();
                    break;
                case EXIT:
                    return;
                default:
                    break;
            }
        }
    }

    private void mainMenu() {
        if (guest != null) {
            out.println("1 - Zarezerwuj pokoj");
            out.println("2 - Anuluj rezerwacje");

        }

        if (isAdmin) {
            out.println("3 - Edytuj rezerwacje");
        } else {
            out.println();
        }

        if (guest == null) {
            out.println("4 - Zaloguj sie");
            out.println("5 - Zarejestruj sie");
        } else {
            out.println("4 - Wyloguj sie");
        }
        if (isAdmin) {
            out.println("6 - Edytuj uzytkownika");
        } else {
            out.println();
        }
        if (isAdmin) {
            out.println("7 - Dodaj pokoj");
            out.println("8 - Usun pokoj");
            out.println("9 - Edytuj pokoj");
            out.println();
        }

        String choice;
        while (true) {
            if (!in.hasNext()) continue;
            choice = in.next();

            if (choice.equals("quit")) {
                state = states.EXIT;
                return;
            }

            if (choice.equals("admin")) {
                isAdmin = true;
                continue;
            }

            switch (choice) {
                case "1":
                    state = states.REGISTERRESERVATION;
                    return;
                case "2":
                    state = states.CANCELRESERVATION;
                    return;
                /*case "3":
                    state = states.EDITRESERVATION;
                    return;*/
                case "4":
                    if (guest == null) {
                        state = states.LOGINGUEST;
                    } else {
                        guest = null;
                    }
                    return;
                case "5":
                    if (guest != null) return;
                    state = states.REGISTERGUEST;
                    return;
                case "6":
                    if (!isAdmin) return;
                    state = states.EDITGUEST;
                    return;
                case "7":
                    if (!isAdmin) return;
                    state = states.ADDROOMS;
                    return;
                case "8":
                    if (!isAdmin) return;
                    state = states.REMOVEROOMS;
                    return;
                case "9":
                    if (!isAdmin) return;
                    state = states.EDITROOMS;
                    return;
                default:
                    clear();
                    out.println("Prosze wybrac odpowiednia opcje");
            }
        }
    }

    private void registerReservations() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date checkIn;
        Date checkOut;
        String choice;
        Date now = new Date();

        while (true) {
            out.println("Podaj datę zameldowania (YYYY-MM-DD): ");
            try {
                checkIn = df.parse(in.next());
                if (checkIn.compareTo(now) < 0) {
                    out.println("Data zameldowania nie moze byc przed " + now);
                    continue;
                }
                break;
            } catch (ParseException ignored) {
                out.println("Prosze podac date w formacie YYYY-MM-DD");
            }
        }

        while (true) {
            out.println("Podaj datę wymeldowania (YYYY-MM-DD): ");
            try {
                checkOut = df.parse(in.next());
                if (!checkOut.after(checkIn)) {
                    out.println("Data wymeldowania musi byc pozniej niż data zameldowania");
                    continue;
                }
                break;
            } catch (ParseException ignored) {
                out.println("Prosze podac date w formacie YYYY-MM-DD");
            }
        }

        Map<Integer, ArrayList<Integer>> freeRooms = hotel.getFreeRooms(checkIn, checkOut);
        if (freeRooms.isEmpty()) {
            out.println("Niestety nie mamy wolnych pokoi dla podanego terminu");
            do {
                out.print("Czy chcesz sprawdzic inny termin? [y/n]");
                choice = in.next();
                if (choice.equals("y")) state = states.REGISTERRESERVATION;
                if (choice.equals("n")) state = states.MAINMENU;
            } while (!choice.equals("y") && !choice.equals("n"));
        }

        out.println("Dla podanego terminu mamy wolne pokoje:");
        for (Integer beds : freeRooms.keySet()) {
            out.printf("%s-osobowe: %s\n", beds, freeRooms.get(beds).size());
        }

        out.println("Prosze podac liczbe pokoi do zarezerwowania");
        HashMap<Integer, Integer> requiredRooms = new HashMap<>();
        int bed;

        CHOICE:
        for (Integer beds : freeRooms.keySet()) {
            out.printf("%s-osobowe (max %s): ", beds, freeRooms.get(beds).size());
            do {
                bed = in.nextInt();
                if (bed == 0) continue;
                if (bed > freeRooms.get(beds).size()) {
                    out.println("Brak wystarczającej liczby pokoi");
                    out.printf("Dostepnych jest %s %s-osobowych pokoi\n", freeRooms.get(beds).size(), beds);
                    continue;
                }
                requiredRooms.put(beds, bed);
                break;
            } while (true);
        }

        out.println();
        float price = 0.f;
        for (Integer i : requiredRooms.keySet()) {
            out.printf("%s-osobowy - %s\n", i + 1, requiredRooms.get(i));
            for (int j = 0; j < requiredRooms.get(i); j++) {
                price += hotel.getRoom(freeRooms.get(i).get(j)).getPriceWithDiscount();
            }
            price *= 1 - guest.getDiscount();
        }
        out.printf("Cena: %s PLN\n", price);
        String confirm;
        do {
            out.println("Czy potwierdzasz rezerwacje? [y/n]: ");
            confirm = in.next();
            if (confirm.equals("y")) {
                hotel.makeReservation(guest.getUsername(), requiredRooms, checkIn, checkOut);
                state = states.MAINMENU;
                return;
            }
        } while (!confirm.equals("n"));
        state = states.MAINMENU;
    }

    private void cancelReservations() {
        out.println("Zaplanowane rezerwacje");
        Reservation r;
        Integer choice;
        String confirm;
        ArrayList<Reservation> reservations = hotel.getReservationPerGuest(guest.getUsername());
        if (reservations == null || reservations.isEmpty()) {
            out.println("Brak dostepnych rezerwacji do anulowania");
            state = states.MAINMENU;
            return;
        }
        for (int i = 1; i <= reservations.size(); i++) {
            r = reservations.get(i - 1);
            out.printf("%s - %s %s %s", i, r.getCheckIn(), r.getCheckOut(), r.getRoomNumbers());
        }
        out.println("Prosze wybrac rezerwacje do anulowania: ");
        choice = in.nextInt();
        while (choice < 1 || choice >= reservations.size()) {
            out.println("Prosze podac poprawny numer rezerwacji");
            choice = in.nextInt();
        }
        r = reservations.get(choice - 1);
        do {
            out.print("Prosze potwierdzic anulowanie rezerwacji " + choice + " [y/n]: ");
            confirm = in.next();
            if (confirm.equals("y")) {
                hotel.cancelReservation(r);
                state = states.MAINMENU;
                return;
            }
        } while (!confirm.equals("n"));
    }

    private void editReservations() {
        if (!isAdmin) {
            state = states.MAINMENU;
            return;
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date checkIn, checkOut, now = new Date();
        String username = "";

        out.print("Podaj nazwe uzytkownika: ");
        while (username.equals("")) {
            username = in.next();
            if (username.isEmpty()) continue;
            if (!hotel.guestExists(username)) {
                out.println("Podany uzytkownik nie istnieje");
                return;
            }
        }


        ArrayList<Reservation> reservations = hotel.getReservationPerGuest(username);
        if (reservations.isEmpty()) {
            out.println("Podany uzytkownik nie posiada zadnych rezerwacji");
            return;
        }

        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            out.printf("%d - %s %s %s pokoi", i + 1, r.getCheckIn(), r.getCheckOut(), r.getRoomNumbers().size());
        }
    }

    private void loginGuest() {
        String username;
        String password;
        Guest guest;

        while (true) {
            out.println("Podaj 0, aby powrocic do menu");
            out.print("Prosze podac nazwe uzytkownika: ");
            username = in.next();
            if (username.isEmpty()) continue;
            if (username.equals("0")) {
                state = states.MAINMENU;
                return;
            }
            guest = hotel.getGuest(username);
            if (guest == null) {
                out.println("Nie znaleziono uzytkownika " + username);
                continue;
            }
            out.print("Prosze podac haslo: ");
            password = in.next();
            if (password.isEmpty()) {
                out.println("Podano nieprawidlowe haslo");
                continue;
            }

            if (!password.equals(guest.getPassword())) {
                out.println("Podano nieprawidlowe haslo");
                continue;
            }
            this.guest = guest;
            state = states.MAINMENU;
            break;

        }
    }

    private void registerGuest() {
        String firstName;
        String lastName;
        String username;
        String password;
        String choice;

        while (true) {
            out.println("Podaj 0, aby powrocic do menu");
            do {
                out.print("Prosze podac imie: ");
                firstName = in.next();
            } while (firstName.isEmpty());
            do {
                out.print("Prosze podac nazwisko: ");
                lastName = in.next();
            } while (lastName.isEmpty());
            do {
                out.print("Prosze podac nazwe uzytkownika: ");
                username = in.next();
                if (hotel.guestExists(username)) {
                    out.println("Podana nazwa uzytkownika jest juz zajeta");
                    continue;
                }
            } while (username.isEmpty());
            do {
                out.print("Prosze podac haslo: ");
                password = in.next();
            } while (password.isEmpty());

            out.println("Imie: " + firstName);
            out.println("Nazwisko: " + lastName);
            out.println("Nazwa uzytkownika: " + username);
            out.println("Haslo: " + password);

            do {
                out.print("Czy dane sa poprawne? [y/n] ");
                choice = in.next();
                if (choice.equals("y")) {
                    guest = hotel.createGuest(username, firstName, lastName, password);
                    state = states.MAINMENU;
                    hotel.updateGuestCSV();
                    return;
                }
            } while (!choice.equals("n"));
        }
    }

    private void editGuest() {
    }

    private void addRooms() {
        int number, beds;
        float price;
        String choice;

        while (true) {
            out.print("Prosze podac numer nowego pokoju: ");
            number = in.nextInt();
            if (hotel.getRoom(number) != null) {
                out.println("Pokoj juz istnieje. Wprowadz inny numer");
                continue;
            }
            break;
        }
        while (true) {
            out.print("Prosze podac ilosc lozek w pokoju: ");
            beds = in.nextInt();
            if (1 > beds || beds > 4) {
                out.println("Pokoj moze miec tylko od 1 do 4 lozek");
                continue;
            }
            break;
        }
        while (true) {
            out.print("Prosze podac cene za noc w nowym pokoju: ");
            price = in.nextFloat();
            if (price < 0) {
                out.println("Cena za noc nie moze byc mniejsza od 0");
                continue;
            }
            break;
        }

        out.println("Numer pokoju: " + number);
        out.println("Ilosc lozek: " + beds);
        out.println("Cena za noc: " + price);
        while (true) {
            out.print("Czy potwierdzasz dodanie nowego pokoju? [y/n]: ");
            choice = in.next();
            if (choice.equals("y")) {
                hotel.addRoom(number, beds, price, 0f);
                out.println("Dodano nowy pokoj");
                state = states.MAINMENU;
                return;
            } else if (choice.equals("n")) {
                state = states.MAINMENU;
                return;
            }
        }
    }

    private void removeRooms() {
    }

    private void editRooms() {
    }

    private String getHeader() {
        return "--- HOTEL ---";
    }

    private void clear() {
        System.out.print("\n\033[2J\033[1;1H");
    }
}
