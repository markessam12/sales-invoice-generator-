package view;

import constants.Constants;
import model.GeneratorData;
import model.Invoice;
import model.InvoiceItem;
import model.UniqueID;
import utilities.DateValidator;
import utilities.Utilities;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

public class SalesInvoiceGenerator extends JFrame implements ActionListener, MouseListener, TableModelListener
{
    private static SalesInvoiceGenerator instance;
    private JTextField selectedInvoiceDate;
    private JLabel selectedInvoiceNumber;
    private JTextField selectedCustomerName;
    private JLabel selectedInvoiceTotal;
    private CSVTable invoiceTable;
    private CSVTable invoiceItemsTable;
    private Vector<GeneratorData> invoices = new Vector<>();
    private GridBagConstraints positionGBC = new GridBagConstraints();

    private Boolean invoiceEdited = Boolean.FALSE;
    private Boolean creatingInvoice = Boolean.FALSE;

    // private constructor to prevent using the word new as this is a singleton pattern implementation
    private SalesInvoiceGenerator()
    {
        super("Sales Invoice Generator");
        instance = this;

        setupFrame();
        createToolBar();

        // Create the Invoice Table and Configure it's layout
        positionGBC.insets = new Insets(0,0,0,0);
        setGridConstraint(0,0,5,1);
        JLabel tableNameLabel = new JLabel("Invoices Table");
        add(tableNameLabel, positionGBC);
        setGridConstraint(0,1,5,6);
        invoiceTable = new CSVTable(this, Constants.INVOICE_TABLE_COLS, positionGBC, false);
        invoiceTable.getTable().addMouseListener(this);
        positionGBC.insets = new Insets(10,40,10,40);

        // Create the buttons of the Invoice Table
        setGridConstraint(2,7,2,1);
        createBtn("Create New Invoice",Constants.CREATE_BTN_ACTION_COMMAND);
        setGridConstraint(4,7,1,1);
        createBtn("Delete Invoice", Constants.DELETE_BTN_ACTION_COMMAND);

        createInvoiceDataEditor();

        // Create the Invoice items Table and Configure it's layout
        positionGBC.insets = new Insets(0,20,0,0);
        setGridConstraint(6,4,5,1);
        JLabel InvoiceTableNameLabel = new JLabel("Invoice Items");
        add(InvoiceTableNameLabel, positionGBC);
        setGridConstraint(6,5,5,2);
        invoiceItemsTable = new CSVTable(this, Constants.INVOICE_ITEMS_COLS,positionGBC,true);
        invoiceItemsTable.getModel().addTableModelListener(this);
        positionGBC.insets = new Insets(10,40,10,0);

        // Create the buttons of the Invoice Items Table
        setGridConstraint(7,7,1,1);
        createBtn("Save", Constants.SAVE_UPDATE_BTN_ACTION_COMMAND);
        setGridConstraint(8,7,1,1);
        createBtn("Remove Item", Constants.REMOVE_ITEM_BTN_ACTION_COMMAND);
        setGridConstraint(9,7,1,1);
        positionGBC.insets = new Insets(10,40,10,20);
        createBtn("Add Item", Constants.ADD_ITEM_BTN_ACTION_COMMAND);
    }

    // Returns the instance of the created singleton object
    public static SalesInvoiceGenerator getInstance()
    {
        if(instance == null){
            new SalesInvoiceGenerator();
        }
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(Constants.OPEN_FILE_ACTION_COMMAND)){
            openCSV();
        }
        else if(e.getActionCommand().equals(Constants.CREATE_BTN_ACTION_COMMAND)){
            addInvoice();
        }
        else if(e.getActionCommand().equals(Constants.DELETE_BTN_ACTION_COMMAND)){
            deleteInvoice();
        }
        else if(e.getActionCommand().equals(Constants.ADD_ITEM_BTN_ACTION_COMMAND)){
            if(invoiceTable.getTable().getSelectedRow() == -1){
                JOptionPane.showMessageDialog(this,"No invoice selected.", "Item addition failed", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                invoiceItemsTable.addRow();
            }
        }
        else if(e.getActionCommand().equals(Constants.REMOVE_ITEM_BTN_ACTION_COMMAND)){
            deleteItem();
        }
        else if(e.getActionCommand().equals(Constants.INVOICE_DATA_EDITED_ACTION_COMMAND)){
            invoiceEdited = true;
        }
        else if(e.getActionCommand().equals(Constants.SAVE_UPDATE_BTN_ACTION_COMMAND)){
            saveChanges();
        }
        else if(e.getActionCommand().equals(Constants.SAVE_FILE_ACTION_COMMAND)){
            saveCSV();
        }
    }

    private void openCSV(){
        //reset data
        invoiceEdited = Boolean.FALSE;
        creatingInvoice = Boolean.FALSE;
        invoiceItemsTable.clearTable();
        setInvoiceData("-","","","-");

        //open invoice header
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String filename = f.getName().toLowerCase();
                    return filename.endsWith(".csv") || filename.endsWith(".txt") ;
                }
            }

            @Override
            public String getDescription() {
                return "CSV Files (*.csv)";
            }
        });
        fileChooser.setDialogTitle("Open invoice header file");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);

        //open invoice line
        JFileChooser fileChooser2 = new JFileChooser();
        fileChooser2.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String filename = f.getName().toLowerCase();
                    return filename.endsWith(".csv") || filename.endsWith(".txt") ;
                }
            }

            @Override
            public String getDescription() {
                return "CSV Files (*.csv)";
            }
        });
        fileChooser2.setDialogTitle("Open invoice line file");
        fileChooser2.setCurrentDirectory(fileChooser.getCurrentDirectory());
        int result2 = fileChooser2.showOpenDialog(this);

        //check if the two files opened successfully to continue
        if (result == JFileChooser.APPROVE_OPTION && result2 == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            File selectedFile2 = fileChooser2.getSelectedFile();
            try {
                invoices = Utilities.parseCSVInvoices(selectedFile);
                Utilities.parseCSVItems(selectedFile2,invoices);
                setGridConstraint(0,1,5,6);
                invoiceTable.clearTable();
                invoiceTable.addMultipleRows(invoices);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,e.getMessage(),"Import failed",JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void saveCSV(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String filename = f.getName().toLowerCase();
                    return filename.endsWith(".csv") || filename.endsWith(".txt") ;
                }
            }

            @Override
            public String getDescription() {
                return "CSV Files (*.csv)";
            }
        });
        fileChooser.setDialogTitle("Open invoice header file");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File path = fileChooser.getSelectedFile();
            try{
                Utilities.saveFiles(path, invoices);
                JOptionPane.showMessageDialog(this,"Files saved successfully!");
            }catch (IOException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,"Failed to save the files", "Save failed", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            System.out.println("No Selection");
        }
    }

    private void addInvoice(){
        if(creatingInvoice){
            int confirm = JOptionPane.showConfirmDialog(this,"Previous created invoice wasn't saved. Discard it?",
                    "Invoice creation error", JOptionPane.YES_NO_OPTION);
            invoiceTable.getTable().setRowSelectionInterval(invoices.size(), invoices.size());
            if(confirm == JOptionPane.YES_OPTION){
                invoiceTable.removeRow(invoices.size());
                invoiceItemsTable.clearTable();
                setInvoiceData("-","","","-");
                invoiceEdited = false;
                creatingInvoice = false;
            }
            return;
        }
        creatingInvoice = true;
        int id = UniqueID.getUniqueID();
        invoiceItemsTable.clearTable();
        invoiceTable.addRow();
        invoiceTable.getTable().setRowSelectionInterval(invoices.size(), invoices.size());
        setInvoiceData(Integer.toString(id),"","","-");
    }

    private void deleteInvoice() {
        if(creatingInvoice){
            int confirm = JOptionPane.showConfirmDialog(this,"Cancel the new invoice creation?",
                    "confirm invoice delete",JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION){
                creatingInvoice = false;
                invoiceEdited = false;
                invoiceTable.removeRow(Integer.parseInt(selectedInvoiceNumber.getText()) - 1);
                invoiceItemsTable.clearTable();
                setInvoiceData("-","","","-");
            }
            return;
        }
        int index = invoiceTable.getTable().getSelectedRow();
        if(index == -1){
            JOptionPane.showMessageDialog(this,"Please select an invoice to delete.","Invalid selection",JOptionPane.ERROR_MESSAGE);
        }
        else{
            int confirm = JOptionPane.showConfirmDialog(this,"Are you sure you want to delete this invoice?.",
                    "confirm invoice delete",JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION){
                invoices.remove(index);
                invoiceTable.removeRow(index);
                for (int i = index; i < invoices.size(); i++) {
                    ((Invoice)invoices.get(i)).updateInvoiceID(i+1);
                }
                invoiceTable.clearTable();
                invoiceTable.addMultipleRows(invoices);
                invoiceItemsTable.clearTable();
                UniqueID.revertID();
                setInvoiceData("-","","","-");
            }
        }

    }

    private void deleteItem(){
        int invoiceIndex = invoiceTable.getTable().getSelectedRow();
        int itemIndex = invoiceItemsTable.getTable().getSelectedRow();
        if(itemIndex == -1 || invoiceIndex == -1){
            JOptionPane.showMessageDialog(this,"No item selected.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,"Delete the selected item?",
                "Confirm Deletion",JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION){
            try{
                ((Invoice)invoices.get(invoiceIndex)).getInvoiceItems().remove(itemIndex);
            }catch (ArrayIndexOutOfBoundsException ae){
                ae.printStackTrace();
            }
            invoiceItemsTable.removeRow(itemIndex);
        }
    }

    private void saveChanges(){
        if(selectedInvoiceDate.getText().equals("") || selectedCustomerName.getText().equals("") || !validCheck(invoiceItemsTable.getTable())){
            JOptionPane.showMessageDialog(this,"Complete invoice missing data", "Failed to save",JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(!DateValidator.validateJavaDate(selectedInvoiceDate.getText())){
            JOptionPane.showMessageDialog(this,"Invalid date! Please enter a valid date", "Failed to save",JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,"Save the new data?","Save confirmation",JOptionPane.YES_NO_CANCEL_OPTION);
        if(confirm == JOptionPane.NO_OPTION){
            if(creatingInvoice) {
                addInvoice();
            }
            else{
                invoiceItemsTable.clearTable();
                setInvoiceData("-","","","-");
                invoiceEdited = false;
                creatingInvoice = false;
            }
        }
        else if(confirm == JOptionPane.YES_OPTION){
            //fire table changed to update the table if items exists before saving it
            if(invoiceItemsTable.getTable().getRowCount() > 0){
                invoiceItemsTable.getModel().fireTableDataChanged();
            }

            int index = Integer.parseInt(selectedInvoiceNumber.getText()) - 1;
            //Procedure in case the user creates new invoice
            if(creatingInvoice){
                Invoice invoice = new Invoice(selectedInvoiceDate.getText(),selectedCustomerName.getText());
                invoices.add(invoice);
            }
            //Procedure in case the user update already existing invoice
            else{
                ((Invoice)invoices.get(index)).setInvoiceDate(selectedInvoiceDate.getText());
                ((Invoice)invoices.get(index)).setInvoiceCustomerName(selectedCustomerName.getText());
            }
            ////Procedure in both old or new invoices where we edit items data
            ((Invoice)invoices.get(index)).deleteInvoiceItems();
            for (int i = 0; i < invoiceItemsTable.getTable().getRowCount(); i++) {
                invoices.get(index).addItem(new InvoiceItem(invoices.get(index).getInvoiceID(),
                        invoiceItemsTable.getTable().getValueAt(i,0).toString(),
                        Utilities.doubleConverter(invoiceItemsTable.getTable().getValueAt(i,1)),
                        Utilities.integerConverter(invoiceItemsTable.getTable().getValueAt(i,2)))
                );
            }
            invoiceEdited = false;
            creatingInvoice = false;
            invoiceTable.clearTable();
            invoiceTable.addMultipleRows(invoices);
            invoiceItemsTable.clearTable();
            setInvoiceData("-","","","-");
        }
        else{
            return;
        }
    }

    //method fired when an invoice row is selected
    private void tableMouseClicked(java.awt.event.MouseEvent evt){
        if(creatingInvoice){
            addInvoice();
            return;
        }
        if(invoiceEdited){
            int confirm = JOptionPane.showConfirmDialog(this,"Selecting different invoice will cancel the current edits. \nDiscard changes?",
                    "Unsaved changes",JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.NO_OPTION){
                int currentlySelected = Integer.parseInt(selectedInvoiceNumber.getText()) - 1;
                invoiceTable.getTable().setRowSelectionInterval(currentlySelected,currentlySelected);
                return;
            }
        }
        DefaultTableModel tb1Model = (DefaultTableModel) invoiceTable.getTable().getModel();
        invoiceItemsTable.clearTable();
        try{
            int row = invoiceTable.getTable().getSelectedRow();
            String invoiceNumber = tb1Model.getValueAt(row,0).toString();
            String invoiceDate = tb1Model.getValueAt(row,1).toString();
            String invoiceCustomerName = tb1Model.getValueAt(row,2).toString();
            String invoiceTotalPrice = tb1Model.getValueAt(row,3).toString();
            setInvoiceData(invoiceNumber,invoiceDate,invoiceCustomerName,invoiceTotalPrice);

            Vector<InvoiceItem> items = ((Invoice) invoices.get(row)).getInvoiceItems();
            for (InvoiceItem item : items)
            {
                invoiceItemsTable.addRow(item);
            }
        }catch (Exception e){
            setInvoiceData("-","","","-");
            invoiceItemsTable.clearTable();
        }
        invoiceEdited = false;
    }

    private void setInvoiceData(int invoiceNumber, String invoiceDate, String customerName, int invoiceTotal){
        this.selectedInvoiceNumber.setText(Integer.toString(invoiceNumber));
        this.selectedInvoiceDate.setText(invoiceDate);
        this.selectedCustomerName.setText(customerName);
        this.selectedInvoiceTotal.setText(Integer.toString(invoiceTotal));
    }

    private void setInvoiceData(String invoiceNumber, String invoiceDate, String customerName, String invoiceTotal){
        this.selectedInvoiceNumber.setText(invoiceNumber);
        this.selectedInvoiceDate.setText(invoiceDate);
        this.selectedCustomerName.setText(customerName);
        this.selectedInvoiceTotal.setText(invoiceTotal);
    }

    // Create the components for editing Invoices data
    private void createInvoiceDataEditor()
    {
        JLabel invoiceNumberLabel = new JLabel("Invoice Number");
        selectedInvoiceNumber = new JLabel("-");
        setGridConstraint(6,0,2,1);
        add(invoiceNumberLabel,positionGBC);
        setGridConstraint(8,0,3,1);
        add(selectedInvoiceNumber,positionGBC);

        JLabel invoiceDateLabel = new JLabel("Invoice Date");
        selectedInvoiceDate = new JTextField(20);
        selectedInvoiceDate.setActionCommand(Constants.INVOICE_DATA_EDITED_ACTION_COMMAND);
        selectedInvoiceDate.addActionListener(this);
        setGridConstraint(6,1,2,1);
        add(invoiceDateLabel,positionGBC);
        setGridConstraint(8,1,3,1);
        add(selectedInvoiceDate,positionGBC);

        JLabel customerNameLabel = new JLabel("Customer Name");
        selectedCustomerName = new JTextField(25);
        selectedCustomerName.setActionCommand(Constants.INVOICE_DATA_EDITED_ACTION_COMMAND);
        selectedCustomerName.addActionListener(this);
        setGridConstraint(6,2,2,1);
        add(customerNameLabel,positionGBC);
        setGridConstraint(8,2,2,1);
        add(selectedCustomerName,positionGBC);

        JLabel invoiceTotalLabel = new JLabel("Invoice Total");
        selectedInvoiceTotal = new JLabel("-");
        setGridConstraint(6,3,2,1);
        add(invoiceTotalLabel,positionGBC);
        setGridConstraint(8,3,2,1);
        add(selectedInvoiceTotal,positionGBC);
    }

    // Configure the main frame settings
    private void setupFrame()
    {
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setLocation((Utilities.getScreenWidth() - Constants.WINDOW_WIDTH)/2,
                (Utilities.getScreenHeight() - Constants.WINDOW_HEIGHT)/2);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        positionGBC.fill = GridBagConstraints.BOTH;
        setResizable(false);
    }

    // Create the program's toolbar
    private void createToolBar()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem openItem = new JMenuItem("Open",Constants.OPEN_FILE_KEY_CODE);
        openItem.setAccelerator(KeyStroke.getKeyStroke(Constants.OPEN_FILE_KEY_CODE, InputEvent.CTRL_DOWN_MASK));
        openItem.addActionListener(this);
        openItem.setActionCommand(Constants.OPEN_FILE_ACTION_COMMAND);
        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem("Save",Constants.SAVE_FILE_KEY_CODE);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(Constants.SAVE_FILE_KEY_CODE,InputEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(this);
        saveItem.setActionCommand(Constants.SAVE_FILE_ACTION_COMMAND);
        fileMenu.add(saveItem);

        this.setJMenuBar(menuBar);
        menuBar.add(fileMenu);
    }

    // Method for creating buttons
    private void createBtn(String btnName, String actionCommand)
    {
        JButton button = new JButton(btnName);
        button.setActionCommand(actionCommand);
        button.addActionListener(this);
        add(button,positionGBC);
    }

    // Method for changing current grid position and size for the GridBagLayout
    private void setGridConstraint(int x, int y, int xSize, int ySize)
    {
        positionGBC.gridx = x;
        positionGBC.gridy = y;
        positionGBC.gridwidth = xSize;
        positionGBC.gridheight = ySize;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        tableMouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    //fired when items table change to calculate the new total price automatically
    @Override
    public void tableChanged(TableModelEvent e) {
        if(e.getType() == 0){
            dataChanged();
            int editedRow = e.getFirstRow();
            double itemPrice = 0;
            int count = 0;
            //check for validity of inputs in the item price and count fields
            try{
                itemPrice = Utilities.doubleConverter(invoiceItemsTable.getModel().getValueAt(editedRow,1));
                count = Utilities.integerConverter(invoiceItemsTable.getModel().getValueAt(editedRow,2));
            }catch (NumberFormatException exception){
                exception.printStackTrace();
                invoiceItemsTable.getTable().setValueAt(0, editedRow, invoiceItemsTable.getTable().getSelectedColumn());
                invoiceItemsTable.getTable().setValueAt(0, editedRow, 3);
                JOptionPane.showMessageDialog(this,"Please make sure you entered valid data in item price and count fields.","Invalid data",JOptionPane.ERROR_MESSAGE);
                return;
            }
            //condition to break from infinitely editing the cell and firing the change action
            if(invoiceItemsTable.getModel().getValueAt(editedRow, 3) != null &&
                    invoiceItemsTable.getModel().getValueAt(editedRow, 3).equals(Math.abs(itemPrice*count))){
                return;
            }
            //calculate the new total price
            invoiceItemsTable.getTable().setValueAt(Math.abs(itemPrice*count), editedRow, 3);
            selectedInvoiceTotal.setText(Double.toString(calculateCurrentTotalPrice()));

            //confirm that prices and count are positive numbers
            invoiceItemsTable.getTable().setValueAt(Math.abs(itemPrice), invoiceItemsTable.getTable().getSelectedRow(), 1);
            invoiceItemsTable.getTable().setValueAt(Math.abs(count), invoiceItemsTable.getTable().getSelectedRow(), 2);
        }
    }

    private void dataChanged(){
        this.invoiceEdited = true;
    }

    public boolean validCheck(JTable table) {
        if(table.getCellEditor()!= null) {
            table.getCellEditor().stopCellEditing();
        }
        for(int i=0; i < table.getRowCount(); i++) {
            for(int j=0; j < table.getColumnCount(); j++) {
                if(table.getValueAt(i,j) == null){
                    return false;
                }
                String value = table.getValueAt(i,j).toString();
                if(value.trim().length() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public double calculateCurrentTotalPrice(){
        double result = 0;
        for (int i = 0; i < invoiceItemsTable.getTable().getRowCount(); i++) {
            if(invoiceItemsTable.getTable().getValueAt(i,3) == null)
                result += 0;
            else{
                result += (Double) invoiceItemsTable.getTable().getValueAt(i,3);
            }
        }
        return result;
    }
}
