//******************************************************************************
//
//  Developer:     Cory Munselle
//
//  Project #:     Project 3
//
//  File Name:     StoreItem.java
//
//  Course:        COSC 4301 - Modern Programming
//
//  Due Date:      2/27/2022
//
//  Instructor:    Fred Kumi
//
//  Description:   Class that contains information about a product in the store.
//                 Designed to be loaded in an array (sort of like a struct in C++)
//                 and used by other classes.
//
//******************************************************************************

public class StoreItem {

    private final int itemNumber;
    private final String itemDesc;
    private int numUnits;
    private final double itemPrice;

    //***************************************************************
    //
    //  Method:       Constructor
    //
    //  Description:  Defines variables if they're within acceptable bounds
    //
    //  Parameters:   int itemNumber, String itemDesc, int numUnits, double itemPrice
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public StoreItem (int itemNumber, String itemDesc, int numUnits, double itemPrice) {

        if (itemNumber > 0)
            this.itemNumber = itemNumber;
        else
            throw new IllegalArgumentException("Item number must be positive.");

        if (itemDesc.length() > 0)
            this.itemDesc = itemDesc;
        else
            throw new IllegalArgumentException("Please provide a description.");

        if (numUnits >= 0)
            this.numUnits = numUnits;
        else
            throw new IllegalArgumentException("Number of units must be positive.");

        if (itemPrice >= 0.0)
            this.itemPrice = itemPrice;
        else
            throw new IllegalArgumentException("Item price must be at least zero.");
    }

    //***************************************************************
    //
    //  Method:       Getters and Setters
    //
    //  Description:  Gets and sets class variables
    //
    //  Parameters:   int numUnits
    //
    //  Returns:      int itemNumber, String itemDesc, int numUnits, double itemPrice
    //
    //**************************************************************
    public int getItemNumber() {
        return itemNumber;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public int getNumUnits() {
        return numUnits;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setNumUnits(int numUnits) {
        this.numUnits = numUnits;
    }

    //***************************************************************
    //
    //  Method:       toString
    //
    //  Description:  Returns a formatted string representation of the class
    //
    //  Parameters:   None
    //
    //  Returns:      String
    //
    //**************************************************************
    @Override
    public String toString() {
        return String.format("%s%s%n%s%d%n%s%.2f%n", "Item description: ",
                getItemDesc(), "Units: ", getNumUnits(), "Item price: $", getItemPrice());
    }

}
