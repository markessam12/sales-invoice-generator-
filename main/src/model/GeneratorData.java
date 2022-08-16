package model;

import java.util.Vector;

public abstract class GeneratorData {
    public abstract Vector<Object> toVector();

    public abstract String toCSV();

    public abstract UniqueID getInvoiceID();

    public abstract void addMultipleRows(Vector<GeneratorData> items);

    public abstract void addItem(InvoiceItem invoiceItem);
}
