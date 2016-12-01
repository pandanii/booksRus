//BooksRUs Software

import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;

//#########################################################
public class ShoppingCart extends JDialog
                          implements ActionListener

{
StoreFrame pointerToStoreFrame;
Connection connection;

DefaultListModel<JTable> listOfTables;
Queries listOfQueries;

JPanel scrollPanel;
JButton clearButton;

    //=====================================================
    public ShoppingCart(StoreFrame pointerToStoreFrame)
    {
    this.pointerToStoreFrame = pointerToStoreFrame;

    Container cp;
    Toolkit tk;
    Dimension d;

    JPanel mainPanel;
    JPanel buttonPanel;

    JButton purchaseButton;
    JButton closeButton;

    listOfTables = new DefaultListModel<JTable>();

    scrollPanel = new JPanel();

    purchaseButton = new JButton("Purchase");
    purchaseButton.setActionCommand("PURCHASE");
    purchaseButton.addActionListener(this);
    purchaseButton.setToolTipText("Purchase your cart's items.");

    closeButton = new JButton("Close");
    closeButton.setActionCommand("CLOSE");
    closeButton.addActionListener(this);
    closeButton.setToolTipText("Close the shopping cart.");

    clearButton = new JButton("Clear Cart");
    clearButton.setActionCommand("CLEARCART");
    clearButton.addActionListener(this);
    clearButton.setToolTipText("Clear your cart of all items.");
    clearButton.setEnabled(false);

    buttonPanel = new JPanel(new GridLayout(1,3));
    buttonPanel.add(purchaseButton);
    buttonPanel.add(clearButton);
    buttonPanel.add(closeButton);

    mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(scrollPanel, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    cp = getContentPane();
    cp.add(mainPanel, BorderLayout.CENTER);

    setTitle("Shopping Cart");
    setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

    tk = Toolkit.getDefaultToolkit();
    d = tk.getScreenSize();
    setSize(d.width/4, d.height/4);
    setLocation(d.width/3 + 50, d.height/3 + 50);

    d.setSize(550, 550);
    setMinimumSize(d);

    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    connection = pointerToStoreFrame.copyConnection();
    }
    //=====================================================
    public void actionPerformed(ActionEvent e)
    {

    if (e.getActionCommand().equals("CLOSE"))
        {
        this.setVisible(false);
        }
    else if (e.getActionCommand().equals("PURCHASE"))
        {
        //use a loop to send sql commands to database to subtract stock


        }
    else if (e.getActionCommand().equals("CLEARCART"))
        {
        //get selected table and delete it from defaultListModel, then reform the scrollpane
        //add this as a listener to each table when they are created to know when they are selected.

        //Tried the above, but can't figure out the best way to get the exact table selected.

        listOfTables.clear();

        scrollPanel.removeAll();
        scrollPanel.repaint();

        clearButton.setEnabled(false);
        }

    }
    //=====================================================
    public void addItemToCart(Vector<Object> cartRowList, Vector<Object> columnNames)
    {
    JTable cartTable;
    JScrollPane scrollPane;

    scrollPanel.removeAll();// REMOVES THE OLD TABLE FROM THE PANEL

    cartTable = new JTable(cartRowList, columnNames);

    listOfTables.addElement(cartTable);

    scrollPane = new JScrollPane();
    scrollPane.setPreferredSize(new Dimension(500, 400));

    for (int i=0; i < listOfTables.size(); i++)
        {
        scrollPane.add(listOfTables.elementAt(i));
        }

    scrollPanel.add(scrollPane);

    scrollPane.repaint();

    clearButton.setEnabled(true);

    }
    //=====================================================







}
//#########################################################
