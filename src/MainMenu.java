import api.*;
import java.util.*;
import model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainMenu {
    final static AdminResource resource = new AdminResource();
    public static void mainMenu() {
        preloadRooms();
        int menuOption = 0;
        while (true) {
            System.out.println("""
                Main Menu
                ------------------------------
                1. Find and Reserve a Room
                2. See my Reservations
                3. Create an Account
                4. Admin
                5. Exit
                -------------------------------
                Please select a number for the menu options: """);
            while (true) {
                Scanner scanner = new Scanner(System.in);
                try {
                    menuOption = scanner.nextInt();
                    if (1 <= menuOption && menuOption <= 5) {
                        System.out.println("You selected menu option " + menuOption);
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Please enter a valid number (1-5) from the Menu options");
                }
            }
            switch (menuOption) {
                case 1 -> findAndReserveARoom();
                case 2 -> seeMyReservations();
                case 3 -> createAnAccount();
                case 4 -> AdminMenu.admin();
                case 5 -> {
                    System.out.println("Thank you for using our application!");
                    return;
                }
                default -> System.out.println("Please enter a valid number (1-5) from the Menu options");
            }
        }
    }
    public static void preloadRooms() {
        List<IRoom> predefinedRooms = new ArrayList<>();

        predefinedRooms.add(new FreeRoom("101",0.0, RoomType.SINGLE));
        predefinedRooms.add(new Room("102", 100.0, RoomType.SINGLE));
        predefinedRooms.add(new Room("103", 150.0, RoomType.SINGLE));

        predefinedRooms.add(new FreeRoom("201",0.0, RoomType.DOUBLE));
        predefinedRooms.add(new Room("202", 200.0, RoomType.DOUBLE));
        predefinedRooms.add(new Room("203", 250.0, RoomType.DOUBLE));

        AdminResource.addRoom(predefinedRooms, false);
    }


    private static String emailInput() {
        System.out.println("Please enter your email address (Format: name@domain.com):");
        String email = "";
        while (true) {
            Scanner scanner = new Scanner(System.in);
            email = scanner.nextLine();
            if (email.matches("^(.+)@(.+).(.+)$")) {
                break;
            } else {
                System.out.println("Please enter a valid email address (Format: name@domain.com)");
            }
        }
        return email;
    }

    private static Date addDays(Date checkInDate, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);
        calendar.add(Calendar.DATE, i);
        return calendar.getTime();
    }

    private static void findAndReserveARoom() {
        System.out.println("Please enter your email address (Format: name@domain.com):");
        Scanner scanner = new Scanner(System.in);
        String email = scanner.nextLine().trim().toLowerCase();

// simple RegEx validation
        if (!email.matches("^(.+)@(.+)\\.com$")) {
            System.out.println("Invalid email format.");
            System.out.println("Returning to Main Menu.");
            return;
        }

        Customer customer = resource.getCustomer(email);
        if (customer == null) {
            System.out.println("Customer does not exist. Please create an account from the Main Menu.");
            System.out.println("Returning to Main Menu.");
            return;
        }

// Only continue if customer is valid
        else {
            System.out.println("Please enter your check in date (yyyy-mm-dd):");
            Date checkIn;
            while (true) {
                String checkInDate = scanner.nextLine();

                checkIn = parseStrictDate(checkInDate, "yyyy-MM-dd");
                if (checkIn != null) {
                    // To check if check in date is in the future
                    if (checkIn.after(new Date())) {
                        System.out.println("You entered check in date: " + checkIn);
                        break;
                    } else {
                        System.out.println("Please enter a valid check in date (yyyy-mm-dd)");
                    }
                } else {
                    System.out.println("Please enter a valid date (Format: yyyy-mm-dd)");
                }
            }
            System.out.println("Please enter your check out date (yyyy-mm-dd):");
            Date checkOut;
            while (true) {
                String checkOutDate = scanner.nextLine();
                // to date
                checkOut = parseStrictDate(checkOutDate, "yyyy-MM-dd");
                if (checkOut != null) {
                    // To check if check out date is after check in date
                    if (checkOut.after(checkIn)) {
                        System.out.println("You entered check out date: " + checkOut);
                        break;
                    } else {
                        System.out.println("Please enter a valid check out date (yyyy-mm-dd) after check in date");
                    }
                } else {
                    System.out.println("Please enter a valid date (Format: yyyy-mm-dd)");
                }
            }
            boolean ifRecommendRoom = false;
            // find available rooms
            Collection<IRoom> rooms = resource.findARoom(checkIn, checkOut);
            int numberOfRooms = rooms.size();
            // display all found rooms
            if (numberOfRooms > 0) {
                System.out.println("Found " + numberOfRooms + " available rooms:");
                for (IRoom room : rooms) {
                    System.out.println(room.toString());
                }
            } else {
                System.out.println("No rooms available");
                rooms = resource.recommendRooms(checkIn, checkOut);
                if (rooms.size() > 0) {
                    System.out.println("We recommend you to add your check in and check out dates by 7 days:");
                    System.out.println("Checkin date will be: " + addDays(checkIn, 7));
                    System.out.println("Checkout date will be: " + addDays(checkOut, 7));
                    System.out.println("After you change your dates, these rooms will be available:");
                    for (IRoom room : rooms) {
                        System.out.println(room.toString());
                    }
                    System.out.println("Do you want to change your check in and out dates (y/n)?");
                    while (true) {
                        String answer = scanner.nextLine().trim();
                        if (answer.equalsIgnoreCase("y")) {
                            ifRecommendRoom = true;
                            break;
                        } else if (answer.equalsIgnoreCase("n")) {
                            System.out.println("No reservation made. Returning to main menu.\n");
                            return;
                        } else {
                            System.out.println("Please enter y or n");
                        }
                    }

                } else {
                    System.out.println("No rooms to recommend.");
                    return;
                }
            }
            if (ifRecommendRoom) {
                checkIn = addDays(checkIn, 7);
                checkOut = addDays(checkOut, 7);
            }
            // select a room
            System.out.println("Please enter a Room Number to Reserve:");
            String roomNumber = "";
            while (true) {
                roomNumber = scanner.nextLine();
                String finalRoomNumber = roomNumber;
                if (rooms.stream().anyMatch(room -> room.getRoomNumber().equals(finalRoomNumber))) {
                    break;
                } else {
                    System.out.println("Please enter a valid room number");
                }
            }
            // book a room
            Reservation reservation = resource.bookARoom(email, resource.getRoom(roomNumber), checkIn, checkOut);
            System.out.println("Your reservation is: " + reservation.toString());
        }
    }

    private static void createAnAccount() {
        System.out.println("Please enter your first name");
        String firstName = "";
        while (true) {
            Scanner scanner = new Scanner(System.in);
            firstName = scanner.nextLine();
            if (firstName.matches("^[a-zA-Z]+$")) {
                break;
            } else {
                System.out.println("Please enter a valid first name");
            }
        }
        System.out.println("Please enter your last name");
        String lastName = "";
        while (true) {
            Scanner scanner = new Scanner(System.in);
            lastName = scanner.nextLine();
            if (lastName.matches("^[a-zA-Z]+$")) {
                break;
            } else {
                System.out.println("Please enter a valid last name");
            }
        }
        String email = emailInput();
        // email to lowercase
        email = email.toLowerCase();
        // check if customer exists
        Customer customer = resource.getCustomer(email);
        if (customer == null) {
            resource.createACustomer(email, firstName, lastName);
            System.out.println("Customer created successfully");
        } else {
            System.out.println("Customer already exists");
        }
    }

    private static void seeMyReservations() {
        System.out.println("Please enter your email address (Format: name@domain.com):");
        Scanner scanner = new Scanner(System.in);
        String email = scanner.nextLine().trim().toLowerCase();

        if (!email.matches("^(.+)@(.+)\\.com$")) {
            System.out.println("Invalid email format.");
            System.out.println("Returning to Main Menu.");
            return;
        }

        Customer customer = resource.getCustomer(email);
        if (customer == null) {
            System.out.println("Customer does not exist. Please create an account from the Main Menu.");
            System.out.println("Returning to Main Menu.");
            return;
        }

        Collection<Reservation> reservations = resource.getCustomerReservations(email);
        if (reservations.size() > 0) {
            System.out.println("Your reservations:");
            for (Reservation reservation : reservations) {
                System.out.println(reservation.toString());
            }
        } else {
            System.out.println("No reservations found.");
        }

    }
    public static Date parseStrictDate(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;  // invalid date
        }
    }
}