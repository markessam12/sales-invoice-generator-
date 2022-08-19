package model;

import java.util.Vector;

//abstract class to the used data classes
public abstract class GeneratorData {
    //convert the object data to vector of objects
    public abstract Vector<Object> toVector();

    //convert the object data to comma-separated-values
    public abstract String toCSV();

    public abstract UniqueID getInvoiceID();

    public abstract void addItem(InvoiceItem invoiceItem);
}
