//******************************************************************************
//
//  Developer:     Cory Munselle
//
//  Project #:     Project 3
//
//  File Name:     CashRegister.java
//
//  Course:        COSC 4301 - Modern Programming
//
//  Due Date:      2/27/2022
//
//  Instructor:    Fred Kumi
//
//  Description:   Performs operations on the customer's cart, adding,
//                 removing, displaying, getting total price, and checking
//                 out the customer.
//
//******************************************************************************

import java.util.ArrayList;

public class CashRegister {

    ArrayList<StoreItem> storeItems;
    ArrayList<StoreItem> custItems;

    //***************************************************************
    //
    //  Method:       Constructor
    //
    //  Description:  Defines variables for use in the program
    //
    //  Parameters:   ArrayList<StoreItem> inventory
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public CashRegister(ArrayList<StoreItem> inventory) {
        this.storeItems = inventory;
        custItems = new ArrayList<>();
    }

    //***************************************************************
    //
    //  Method:       purchaseItem
    //
    //  Description:  Adds the selected StoreItem to the cart
    //
    //  Parameters:   StoreItem custItem
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void purchaseItem (StoreItem custItem) {

        for (StoreItem item : storeItems) {
            if (item.getItemDesc().matches(custItem.getItemDesc())) {
                if (item.getNumUnits() > 0) {
                    try {
                        custItems.add(custItem);
                    }
                    //This shouldn't ever happen, given the checks I've done, but
                    //it doesn't hurt to be safe. I've set it up that this specifically
                    //exits because something would seriously have to go wrong for this
                    //to happen.
                    catch (NullPointerException e) {
                        System.err.println("StoreItem should not be null, something went wrong! Printing stack trace and closing...");
                        e.printStackTrace();
                        System.exit(-1);
                    }
                    System.out.println(custItem.getItemDesc() + " has been added to the cart.");
                    item.setNumUnits(item.getNumUnits() - 1);
                }
                else {
                    System.out.println("There are no more " + custItem.getItemDesc() + " in stock.");
                }
            }
        }
    }

    //***************************************************************
    //
    //  Method:       getTotalPrice
    //
    //  Description:  Calculates the total price of the items in the cart
    //
    //  Parameters:   None
    //
    //  Returns:      double totalPrice
    //
    //**************************************************************
    public double getTotalPrice () {
        double totalPrice = 0.0;
        for (StoreItem custItem : custItems) {
            totalPrice += custItem.getItemPrice() * custItem.getNumUnits();
        }
        return totalPrice;
    }

    //***************************************************************
    //
    //  Method:       showItems
    //
    //  Description:  Displays all items currently in the cart
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void showItems () {
        if (custItems.isEmpty()) {
            System.out.println("The cart is currently empty. \n");
        }
        else {
            System.out.println("These are the current items in the cart.");
            for (StoreItem custItem : custItems) {
                System.out.println("Item " + (custItems.indexOf(custItem) + 1) + ": " + custItem.getItemDesc());
                System.out.println("Number of units: " + custItem.getNumUnits());
                System.out.printf("%s%.2f%n%n", "Item price: $", custItem.getItemPrice());
            }
        }
    }

    //***************************************************************
    //
    //  Method:       clearRegister
    //
    //  Description:  Empties the cart
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void clearRegister () {
        //Since adding an item to the cart removes that item from inventory,
        //when clearing the cart the item needs to be added back to the inventory.
        //This loop does that before clearing the arraylist.
        for (StoreItem storeItem : storeItems) {
            for (StoreItem custItem : custItems) {
                if (storeItem.getItemDesc().matches(custItem.getItemDesc())) {
                    storeItem.setNumUnits(storeItem.getNumUnits() + custItem.getNumUnits());
                }
            }
        }
        custItems.clear();
        System.out.println("Cart has been cleared. \n");
    }

    //***************************************************************
    //
    //  Method:       showInventory
    //
    //  Description:  Shows the current cart contents
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void showInventory() {
        System.out.println("This is the current store inventory. \nPlease note that items in the cart are removed from inventory.");

        //Lambda for displaying each item in the store
        storeItems.forEach((storeItem -> System.out.println(storeItem.toString())));
    }

    //***************************************************************
    //
    //  Method:       checkOut
    //
    //  Description:  Builds a string with the relevant information
    //                and returns the built string
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public String checkOut () {
        //This is the stream that's part of the assignment requirements. I was going to use
        //string.stream() but I found it cumbersome so I switched to this instead. I hope this
        //will work for the requirement.
        StringBuilder receipt = new StringBuilder();

        //IntelliJ is telling me to use Comparator.comparing with a method reference but
        //I don't fully understand method references yet so I'm not using them
        //This lambda just compares the two item descriptions and sorts based on that
        custItems.sort((item1, item2) -> item1.getItemDesc().compareTo(item2.getItemDesc()));

        //Lambda that appends each toString from each item in the cart to the receipt
        custItems.forEach((item) -> receipt.append(item.toString()).append("\n"));

        //I know the assignment says to use lambdas and streams but I couldn't really figure out
        //how to apply a lambda to this process. There doesn't seem to be any need for it regardless.
        //I hope that's okay.
        receipt.append("Subtotal: $").append(String.format("%.2f", this.getTotalPrice())).append("\n");
        receipt.append("Taxes: $").append(String.format("%.2f", this.getTotalPrice() * .0825)).append("\n");
        receipt.append("Final price: $").append(String.format("%.2f", ((this.getTotalPrice() * .0825) + this.getTotalPrice()))).append("\n");

        return receipt.toString();
    }

    //***************************************************************
    //
    //  Method:       displayMenu
    //
    //  Description:  Displays all items in inventory and options for
    //                cart modification and checkout
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void displayMenu() {
        System.out.println("Hello! Thank you for shopping at Cory's Cool Clothes!");
        System.out.println("Please make a selection to add the item to your cart: \n");

        //Lambda that replaces an enhanced for loop that prints out the values for selection
        storeItems.forEach((storeItem -> System.out.println((storeItems.indexOf(storeItem) + 1) + ") " + storeItem.getItemDesc())));
        System.out.println();
        System.out.println("Or, please choose from the options below:");
        System.out.println("A) " + "Show Cash Register");
        System.out.println("B) " + "Clear Cash Register");
        System.out.println("C) " + "Show Inventory");
        System.out.println("D) " + "Check Out");
        System.out.println("If you wish to quit without checking out, please enter a negative number.");
    }

}
