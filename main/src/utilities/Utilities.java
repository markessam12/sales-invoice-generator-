package utilities;

import constants.Constants;
import model.GeneratorData;
import model.Invoice;
import model.InvoiceItem;
import model.UniqueID;

import java.awt.*;
import java.io.*;
import java.util.Vector;

public class Utilities
{
    public static int getScreenWidth()
    {
        return (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    }

    public static int getScreenHeight()
    {
        return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    }

    public static Vector<GeneratorData> parseCSVInvoices(File csv) throws Exception {
        UniqueID.resetID();
        BufferedReader br = new BufferedReader(new FileReader(csv));
        Object[] tableLines = br.lines().toArray();
        Vector<GeneratorData> invoices = new Vector<>(tableLines.length);
        for (int i = 0; i < tableLines.length; i++) {
            String line = tableLines[i].toString().trim();
            String [] data = line.split(",");
            if(data.length == Constants.INVOICE_TABLE_COLS.length - 1){
                if(Integer.parseInt(data[0]) != UniqueID.getUniqueID()){
                    throw new Exception("Invalid ID at row " + i + 1 + " in invoice header file");
                }
                if(DateValidator.validateJavaDate(data[1])){
                    throw new Exception("Invalid date at row " + i + 1 + " in invoice header file");
                }
                invoices.add(new Invoice(data));
            }
            else{
                throw new Exception("Invalid Data, different number of columns found.");
            }
        }
        return invoices;
    }

    public static void parseCSVItems(File CSV, Vector<GeneratorData> invoices) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(CSV));
        Object[] tableLines = br.lines().toArray();
        for (int i = 0; i < tableLines.length; i++) {
            String line = tableLines[i].toString().trim();
            String [] data = line.split(",");
            if(data.length == Constants.INVOICE_ITEMS_COLS.length){
                if(Integer.parseInt(data[0]) >= UniqueID.getUniqueID()){
                    throw new Exception("Invalid ID at row " + i + 1 + " in invoice line file");
                }
                Invoice parentInvoice = (Invoice) invoices.get(Integer.parseInt(data[0])-1);
                parentInvoice.addItem(new InvoiceItem(parentInvoice.getInvoiceID(),data[1],data[2],data[3]));
            }
            else{
                throw new Exception("Invalid Data, different number of columns found.");
            }
        }
    }

    public static int integerConverter(Object input) throws NumberFormatException{
        return (input == null)? 0 : Math.abs(Integer.parseInt(input.toString()));
    }

    public static Double doubleConverter(Object input) throws NumberFormatException{
        return (input == null)? 0 : Math.abs(Double.parseDouble(input.toString()));
    }

    public static void saveFiles(File CSV, Vector<GeneratorData> invoices) throws IOException{
        PrintWriter invoiceHeader = new PrintWriter(new BufferedWriter(new FileWriter(CSV + "\\InvoiceHeader.csv",false)));
        PrintWriter invoiceLine = new PrintWriter(new BufferedWriter(new FileWriter(CSV + "\\InvoiceLine.csv",false)));
        for (GeneratorData invoice : invoices) {
            invoiceHeader.println(invoice.toCSV());
            for (InvoiceItem item : ((Invoice)invoice).getInvoiceItems()) {
                invoiceLine.println(item.toCSV());
            }
        }
        invoiceHeader.flush();
        invoiceLine.flush();
        invoiceHeader.close();
        invoiceLine.close();
    }
}
