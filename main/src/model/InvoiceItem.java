package model;

import java.util.Vector;

public class InvoiceItem extends GeneratorData{
    private UniqueID invoiceID;
    private String itemName;
    private double itemPrice;
    private int itemCount;

    public InvoiceItem(String itemName, double itemPrice, int itemCount) {
        this.itemName = itemName;
        this.itemPrice = Math.abs(itemPrice);
        this.itemCount = Math.abs(itemCount);
    }

    public InvoiceItem(UniqueID invoiceID ,String itemName, double itemPrice, int itemCount) {
        this(itemName, itemPrice, itemCount);
        this.invoiceID = invoiceID;
    }

    public InvoiceItem(UniqueID invoiceID, String itemName, String itemPrice, String itemCount){
        this.invoiceID = invoiceID;
        this.itemName = itemName;
        this.itemPrice = Math.abs(Double.parseDouble(itemPrice));
        this.itemCount = Math.abs(Integer.parseInt(itemCount));
    }

    @Override
    public void addMultipleRows(Vector<GeneratorData> items) {

    }

    @Override
    public void addItem(InvoiceItem invoiceItem) {

    }

    public double getItemTotalCost() {
        return itemPrice * itemCount;
    }

    public Vector<Object> toVector(){
        Vector<Object> vector = new Vector();
        vector.add(itemName);
        vector.add(itemPrice);
        vector.add(itemCount);
        vector.add(getItemTotalCost());
        return vector;
    }

    @Override
    public UniqueID getInvoiceID() {
        return null;
    }

    public String toCSV(){
        return new String(invoiceID.toString() + "," +
                itemName + " ," +
                itemPrice + "," +
                itemCount
        );
    }
}

