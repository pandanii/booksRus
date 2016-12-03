//BooksRUs Software

import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.table.*;

//#########################################################
public class ShoppingCart extends JDialog
                          implements ActionListener,
                          			 MouseListener

{
StoreFrame pointerToStoreFrame;
Connection connection;

JTable cartTable;
CartTableModel cartTableModel;
DefaultTableColumnModel colModel;
TableColumn col;
JScrollPane scrollPane;

int selectedRow;
Point mousePoint;

Queries listOfQueries;

JPanel scrollPanel;
JButton removeItemButton;
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

	colModel = new DefaultTableColumnModel();
	cartTableModel = new CartTableModel();
	cartTable = new JTable(cartTableModel);
	cartTable.addMouseListener(this);
	cartTable.setColumnModel(colModel);

	col = new TableColumn(0);
	col.setPreferredWidth(120);
	col.setMinWidth(50);
	col.setHeaderValue("Title");
	cartTable.addColumn(col);

	col = new TableColumn(1);
	col.setPreferredWidth(120);
	col.setMinWidth(50);
	col.setHeaderValue("Price");
	cartTable.addColumn(col);

	col = new TableColumn(2);
	col.setPreferredWidth(120);
	col.setMinWidth(50);
	col.setHeaderValue("Quantity");
	cartTable.addColumn(col);

	scrollPane = new JScrollPane(cartTable);
	scrollPane.setPreferredSize(new Dimension(450, 400));

    scrollPanel = new JPanel();
    scrollPanel.add(scrollPane);

    purchaseButton = new JButton("Purchase Cart");
    purchaseButton.setActionCommand("PURCHASE");
    purchaseButton.addActionListener(this);
    purchaseButton.setToolTipText("Purchase your cart's items.");

    removeItemButton = new JButton("Remove Item");
    removeItemButton.setActionCommand("REMOVEITEM");
    removeItemButton.addActionListener(this);
    removeItemButton.setToolTipText("Remove selected item from the cart.");
    removeItemButton.setEnabled(false);

    closeButton = new JButton("Close");
    closeButton.setActionCommand("CLOSE");
    closeButton.addActionListener(this);
    closeButton.setToolTipText("Close the shopping cart.");

    clearButton = new JButton("Clear Cart");
    clearButton.setActionCommand("CLEARCART");
    clearButton.addActionListener(this);
    clearButton.setToolTipText("Clear your cart of all items.");
    clearButton.setEnabled(false);

    buttonPanel = new JPanel(new GridLayout(1,4));
    buttonPanel.add(purchaseButton);
    buttonPanel.add(removeItemButton);
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

		cartTableModel.cartTableDefaultListModel.clear();
		cartTableModel.fireTableDataChanged();

		removeItemButton.setEnabled(false);
        clearButton.setEnabled(false);
        }
    else if (e.getActionCommand().equals("REMOVEITEM"))
    	{
		cartTableModel.cartTableDefaultListModel.deleteRow(selectedRow);
		cartTableModel.fireTableDataChanged();
		removeItemButton.setEnabled(false);
		}

    }
    //=====================================================
    public void addItemToCart(Vector<Object> cartRow)
    {


	cartTableModel.cartTableDefaultListModel.addRow(cartRow);
	cartTableModel.fireTableDataChanged();


    clearButton.setEnabled(true);

    }
    //=====================================================
    public void mouseClicked(MouseEvent e)
    {
    System.out.println("ShoppingCart mouseClicked");

    if (e.getButton() == MouseEvent.BUTTON1)
        {

        mousePoint = e.getPoint();
        selectedRow = cartTable.rowAtPoint(mousePoint);
        System.out.println(selectedRow + " SELECTED");

        cartTable.changeSelection(selectedRow, 0, false, false);
        removeItemButton.setEnabled(true);
        }
    else
    	{
	    removeItemButton.setEnabled(false);
		}
	}
	//=====================================================
    public void mousePressed(MouseEvent e)
    {

	}
	//=====================================================
    public void mouseReleased(MouseEvent e)
    {

	}
	//=====================================================
    public void mouseEntered(MouseEvent e)
    {

	}
	//=====================================================
    public void mouseExited(MouseEvent e)
    {

	}
	//=====================================================



}
//#########################################################
