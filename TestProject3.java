//******************************************************************************
//
//  Developer:     Cory Munselle
//
//  Project #:     Project 3
//
//  File Name:     TestProject3.java
//
//  Course:        COSC 4301 - Modern Programming
//
//  Due Date:      2/27/2022
//
//  Instructor:    Fred Kumi
//
//  Description:   Drives the methods in CashRegister, prompting the
//                 user to add items to their cart, check out, and
//                 accepts user input. Exports the results of the purchase
//                 to a file.
//
//******************************************************************************

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Random;
import java.util.Scanner;

public class TestProject3 {

    private final Scanner userInput;
    private Scanner inventoryInput;
    private Scanner cashierInput;
    private Formatter output;
    private final ArrayList<String> linePieces = new ArrayList<>();
    private final ArrayList<String> cashiers = new ArrayList<>();
    private final ArrayList<StoreItem> inventory = new ArrayList<>();
    private final CashRegister currTransaction;

    //***************************************************************
    //
    //  Method:       Constructor
    //
    //  Description:  Defines variables for use in the program
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public TestProject3 () {
        userInput = new Scanner(System.in);
        currTransaction = new CashRegister(inventory);
    }

    //***************************************************************
    //
    //  Method:       main
    //
    //  Description:  The main method of the program
    //
    //  Parameters:   String array
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public static void main(String[] args) {

        TestProject3 testObj = new TestProject3();

        testObj.developerInfo();

        testObj.getFilename();

        testObj.readFile();

        testObj.processFile();

        testObj.menuDriver();
    }

    //***************************************************************
    //
    //  Method:       getFilename
    //
    //  Description:  Gets filenames for cashier and inventory from the user
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void getFilename () {
        System.out.println("Please give the name of the inventory file and cashier file (with extensions) that you want to open.");
        String filename;
        String cashierName;
        filename = userInput.next();
        cashierName = userInput.next();

        while (filename.isEmpty() || !this.openFile(filename, cashierName)) {
            filename = userInput.next();
            cashierName = userInput.next();
        }
    }

    //***************************************************************
    //
    //  Method:       openFile
    //
    //  Description:  Opens input and output files
    //
    //  Parameters:   String filename, String cashierName
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public boolean openFile(String filename, String cashierName)
    {
        boolean success = false;
        try {
            inventoryInput = new Scanner(Paths.get(filename));
            cashierInput = new Scanner(Paths.get(cashierName));
            output = new Formatter("../Project3-Output.txt");
        }
        catch (FileNotFoundException e) {
            System.err.println("Cannot find file. Please supply another file.");
        }
        catch (IOException ioException) {
            System.err.println("Error opening file. Please try again.");
        }
        if (inventoryInput != null && cashierInput != null) {
            success = true;
        }
        return success;
    }

    //***************************************************************
    //
    //  Method:       readFile
    //
    //  Description:  Reads the input of both files line by line
    //                and stores them in an ArrayList for later processing
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void readFile() {
        while (inventoryInput.hasNext()) {
            linePieces.add(inventoryInput.nextLine());
        }
        while (cashierInput.hasNext()) {
            cashiers.add(cashierInput.next());
        }
    }

    //***************************************************************
    //
    //  Method:       processFile
    //
    //  Description:  Processes the Inventory.txt file and creates
    //                a StoreItem object from the information
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void processFile() {
        String[] pieces;
        int itemNumber = 0;
        String itemDesc = null;
        int numUnits = 0;
        double itemPrice = 0;

        //Since the order in the file is going to be the same regardless,
        //I feel safe in just making raw assignments. Still need to check for
        //exceptions though.
        for (String linePiece : linePieces) {
            //regex: one or more spaces
            //This will split based on any amount of whitespace, regardless of amount
            pieces = linePiece.split("\\s+");

            //This isn't strictly necessary, but it's better to be safe I think
            try {
                itemNumber = Integer.parseInt(pieces[0]);
                itemDesc = pieces[1];
                numUnits = Integer.parseInt(pieces[2]);
                itemPrice = Double.parseDouble(pieces[3]);
            } catch (NumberFormatException | NullPointerException e) {
                e.printStackTrace();
                System.exit(-1);
            }
            //Because the program exits if any of the conversions fail, I don't have any checks here.
            inventory.add(new StoreItem(itemNumber, itemDesc, numUnits, itemPrice));
        }
    }

    //***************************************************************
    //
    //  Method:       menuDriver
    //
    //  Description:  Runs the menu and calls various commands from
    //                the CashRegister object based on user input
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void menuDriver() {

        String input;
        int selection = 0;

        while (selection >= 0) {
            currTransaction.displayMenu();
            input = userInput.next();

            //regex: One or more digits with a possible - sign in front
            //This lets me check for all digits generically so I don't
            //have to create logic for each possible entry
            if (input.matches("-?\\d+")) {

                //The regex above makes this strictly unnecessary, but
                //I'm not 100% familiar with regex yet so I don't want
                //to miss any edge cases I'm not ready for
                try {
                    selection = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                //Prevents values above the inventory length or less than zero
                if (selection > inventory.size() || selection == 0) {
                    System.out.println("Invalid option. Please try again.");
                }
                else if (selection < 0) {
                    System.out.println("\nThank you for shopping at Cory's Cool Clothes!");
                }
                else {
                    //Since the selection will correspond to the position of the item in the inventory arraylist,
                    //I can use the selection value to access items in inventory and add them based on that
                    currTransaction.purchaseItem(new StoreItem(inventory.get(selection - 1).getItemNumber(), inventory.get(selection - 1).getItemDesc(), 1, inventory.get(selection - 1).getItemPrice()));
                }
            } else if (input.toUpperCase().matches("A")) {
                currTransaction.showItems();
            } else if (input.toUpperCase().matches("B")) {
                currTransaction.clearRegister();
            } else if (input.toUpperCase().matches("C")) {
                currTransaction.showInventory();
            } else if (input.toUpperCase().matches("D")) {
                System.out.println();
                System.out.print(currTransaction.checkOut());
                System.out.println("Would you like to finalize your purchase? (y/n)");
                input = userInput.next();
                if (input.toUpperCase().matches("Y")) {
                    System.out.println("\nThank you for shopping at Cory's Cool Clothes!");
                    genReceipt();
                    selection = -1;
                }
                else {
                    System.out.println("Understood. Emptying the cart...");
                    currTransaction.clearRegister();
                }
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    //***************************************************************
    //
    //  Method:       genReceipt
    //
    //  Description:  Generates the receipt and adds it to the output file
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void genReceipt() {
        DateFormat date = DateFormat.getDateInstance();
        Calendar cals = Calendar.getInstance();

        if (cals.get(Calendar.MINUTE) < 10) {
            output.format("%s%s%20s%s%s%02d", "Date: ", date.format(cals.getTime()), "Time: ", cals.get(Calendar.HOUR), ":", cals.get(Calendar.MINUTE));
        }
        else {
            output.format("%s%s%20s%s%s%s", "Date: ", date.format(cals.getTime()), "Time: ", cals.get(Calendar.HOUR), ":", cals.get(Calendar.MINUTE));
        }
        if (cals.get(Calendar.AM_PM) == Calendar.AM) {
            output.format("%s%n%n", " AM");
        }
        else {
            output.format("%s%n%n", " PM");
        }
        output.format(currTransaction.checkOut());
        output.format("%n%s%s%s", "Your cashier today was ", cashiers.get(new Random().nextInt(cashiers.size() - 1)), ".");
        output.close();
    }

    //***************************************************************
    //
    //  Method:       developerInfo (Non Static)
    //
    //  Description:  The developer information method of the program
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void developerInfo()
    {
        System.out.println("Name:    Cory Munselle");
        System.out.println("Course:  COSC 4301 Modern Programming");
        System.out.println("Project: Three\n");

    } // End of the developerInfo method
}
