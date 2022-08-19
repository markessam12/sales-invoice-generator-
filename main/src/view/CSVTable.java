package view;

import model.GeneratorData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class CSVTable {
    private final JTable CSVTable;
    private final DefaultTableModel table_data;

    public CSVTable(Container container, String[] cols, GridBagConstraints pos, Boolean editable)
    {
        //Disable editing of the total price cell (fourth cells)
        table_data = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                //Only the third column
                return column != 3;
            }
        };
        //Initialize columns headers
        for (String s : cols){
            table_data.addColumn(s);
        }
        if(editable){
            CSVTable = new JTable();
        }
        else{
            CSVTable = new JTable() {
                public boolean editCellAt(int row, int column, java.util.EventObject e) {
                    return false;
                }
            };
        }
        CSVTable.setModel(table_data);
        JScrollPane jsp = new JScrollPane(CSVTable);
        jsp.setBorder(BorderFactory.createLineBorder(Color.black,1));
        container.add(jsp,pos);
    }

    public JTable getTable() {
        return CSVTable;
    }

    public DefaultTableModel getModel() {
        return table_data;
    }

    public void clearTable(){
        table_data.setRowCount(0);
    }

    public void addRow(GeneratorData invoice){
        table_data.addRow(invoice.toVector());
    }

    public void addRow(){
        table_data.addRow(new Vector<>(4));
    }

    public void addMultipleRows(Vector<GeneratorData> items){
        for (GeneratorData item : items) {
            addRow(item);
        }
    }

    public void removeRow(int index){
        table_data.removeRow(index);
    }
}