package model;

import java.util.Vector;

public class Invoice extends GeneratorData{
    private final UniqueID invoiceID;
    private String invoiceDate;
    private String invoiceCustomerName;
    private Vector<InvoiceItem> invoiceItems = new Vector<>();

    public Invoice(String invoiceDate, String invoiceCustomerName){
        this.invoiceDate = invoiceDate;
        this.invoiceCustomerName = invoiceCustomerName;
        invoiceID = new UniqueID();
    }

    public Invoice(int invoiceID, String invoiceDate, String invoiceCustomerName){
        this(invoiceDate, invoiceCustomerName);
        setInvoiceID(invoiceID);
    }

    public Invoice(String [] invoiceData){
        this(Integer.parseInt(invoiceData[0]), invoiceData[1], invoiceData[2]);
    }

    public UniqueID getInvoiceID() {
        return invoiceID;
    }

    private void setInvoiceID(int invoiceID) {
        this.invoiceID.editUniqueID(invoiceID);
    }

    public void updateInvoiceID(int invoiceID){
        this.invoiceID.editUniqueID(invoiceID);
    }

    @Override
    public void addMultipleRows(Vector<GeneratorData> items) {

    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public void setInvoiceCustomerName(String invoiceCustomerName) {
        this.invoiceCustomerName = invoiceCustomerName;
    }

    public Vector<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void addItem(InvoiceItem item){
        invoiceItems.add(item);
    }

    public double getInvoiceTotalPrice() {
        double sum = 0;
        if(invoiceItems != null){
            for (InvoiceItem item : invoiceItems) {
                sum += item.getItemTotalCost();
            }
        }
        return sum;
    }

    public void deleteInvoiceItems(){
        invoiceItems.clear();
    }

    public Vector<Object> toVector(){
        Vector <Object> vector = new Vector();
        vector.add(invoiceID);
        vector.add(invoiceDate);
        vector.add(invoiceCustomerName);
        vector.add(getInvoiceTotalPrice());
        return vector;
    }

    public String toCSV(){
        return new String(invoiceID.toString() + "," +
                invoiceDate + " ," +
                invoiceCustomerName
        );
    }
}
