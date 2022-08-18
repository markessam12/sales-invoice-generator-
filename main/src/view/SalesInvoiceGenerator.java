package view;

import constants.Constants;
import model.GeneratorData;
import utilities.Utilities;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class SalesInvoiceGenerator extends JFrame implements ActionListener, MouseListener, TableModelListener {
    private static SalesInvoiceGenerator instance;
    private JTextField selectedInvoiceDate;
    private JLabel selectedInvoiceNumber;
    private JTextField selectedCustomerName;
    private JLabel selectedInvoiceTotal;
    private CSVTable invoiceTable;
    private CSVTable invoiceItemsTable;
    private Vector<GeneratorData> invoices = new Vector<>();
    private GridBagConstraints positionGBC = new GridBagConstraints();

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

    // Method for changing current grid position and size for the GridBagLayout
    private void setGridConstraint(int x, int y, int xSize, int ySize)
    {
        positionGBC.gridx = x;
        positionGBC.gridy = y;
        positionGBC.gridwidth = xSize;
        positionGBC.gridheight = ySize;
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

    // Create the components for editing Invoices data
    private void createInvoiceDataEditor()
    {
        JLabel invoiceNumberLabel = new JLabel("Invoice Number");
        selectedInvoiceNumber = new JLabel();
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

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

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

    @Override
    public void tableChanged(TableModelEvent e) {

    }
}
