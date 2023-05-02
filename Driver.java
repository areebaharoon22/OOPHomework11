/**
 *This program creates 4 main methods that perform serialization. Each method is either reading or writing to a binary file or both 
 *and making sure it is being serialized properly
 *Name: Areeba Haroon
 *Instructor: Dr. Sam Shamsuddin
 *Assignment: homework 11
 */

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
        int choice;
        do {
            printMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character after nextInt()

            switch (choice) {
                case 1:
                    addInformation();
                    break;
                case 2:
                    retrieveInformation();
                    break;
                case 3:
                    deleteInformation();
                    break;
                case 4:
                	updateInformation();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
        } while (choice != 5);
    }

    private static void printMenu() {
        System.out.println("Select an option from the Menu:");
        System.out.println("1. Add information into a file");
        System.out.println("2. Retrieve information from a file and display them");
        System.out.println("3. Delete Information");
        System.out.println("4. Update Information");
        System.out.println("5. Exit");
    }

    private static void addInformation() {
        File file = new File("Person.bin");
        ArrayList<Person> persons = readPersonsFromFile(file);

        Person obj = createPersonFromUserInput();
        persons.add(obj);

        writePersonsToFile(file, persons);

        System.out.print("Information added successfully!\n");
    }

    private static ArrayList<Person> readPersonsFromFile(File file) {
        ArrayList<Person> persons = new ArrayList<>();
        if (file.exists()) {
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                while (true) {
                    Person person = (Person) input.readObject();
                    persons.add(person);
                }
            } catch (EOFException e) {
                // reached end of file, do nothing
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        return persons;
    }

    private static Person createPersonFromUserInput() {
        System.out.print("Enter the Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter the Date of Birth: ");
        String dateOfBirth = scanner.nextLine();
        System.out.print("Enter the Email Address: ");
        String emailAddress = scanner.nextLine();
        return new Person(name.toLowerCase(), phoneNumber, dateOfBirth, emailAddress);
    }

    private static void writePersonsToFile(File file, ArrayList<Person> persons) {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            for (Person person : persons) {
                output.writeObject(person);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void retrieveInformation() {
        File file = new File("Person.bin");
        ArrayList<Person> persons = readPersonsFromFile(file);

        for (Person obj : persons) {
            System.out.println(obj);
            System.out.println();
        }
    }

    private static void deleteInformation() {
        File file = new File("Person.bin");
        ArrayList<Person> persons = readPersonsFromFile(file);

        System.out.print("Enter the name of the person you want to delete: ");
        String deletedName = scanner.nextLine();

        persons.removeIf(person -> person.getName().equalsIgnoreCase(deletedName));
        writePersonsToFile(file, persons);
        
    }
    
    private static void updateInformation() throws FileNotFoundException, IOException, ClassNotFoundException {
        // Ask user to enter the person name they want to update
        System.out.print("Enter the name of the person you want to update: ");
        String nameToUpdate = scanner.nextLine().toLowerCase();

        // Create a File object for the binary file
        File file = new File("Person.bin");

        // If the file exists, read the existing data to the arraylist from the file
        ArrayList<Person> persons = new ArrayList<>();
        if (file.exists()) {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            try {
                while (true) {
                    Person person = (Person) input.readObject();
                    if (person.getName().equals(nameToUpdate)) {
                        // Person found, update the information
                        System.out.print("Enter the new Phone Number: ");
                        String phoneNumber = scanner.nextLine();
                        System.out.print("Enter the new Date of Birth: ");
                        String dateOfBirth = scanner.nextLine();
                        System.out.print("Enter the new Email Address: ");
                        String emailAddress = scanner.nextLine();
                        person.setPhoneNumber(phoneNumber);
                        person.setDateOfBirth(dateOfBirth);
                        person.setEmail(emailAddress);
                    }
                    persons.add(person);
                }
            } catch (EOFException e) {
                // Reached end of file, do nothing
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
            input.close();

            // Write the updated ArrayList to the file
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
            for (Person person : persons) {
                output.writeObject(person);
            }
            output.close();

            System.out.print("Information updated successfully! \n");
        } else {
            System.out.println("File not found.");
        }
    }

}

