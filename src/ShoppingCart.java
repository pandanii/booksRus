//BooksRUs Software

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

//#########################################################
public class ShoppingCart extends JDialog implements ActionListener, MouseListener
{

    private static final long serialVersionUID = 1L;
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
        listOfQueries = new Queries();
        this.connection = pointerToStoreFrame.connection;

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

        buttonPanel = new JPanel(new GridLayout(1, 4));
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
        setSize(d.width / 4, d.height / 4);
        setLocation(d.width / 3 + 50, d.height / 3 + 50);

        d.setSize(550, 550);
        setMinimumSize(d);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
//=====================================================

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("CLOSE"))
        {
            this.setVisible(false);
        }
        else if (e.getActionCommand().equals("PURCHASE"))
        {
            //use a loop to send sql commands to database to subtract stock
            PreparedStatement preparedStatement;
            ResultSet resultSet = null;
            boolean isAdvd = false;
            boolean isAbook = false;
            int numberOfCopiesToBuy;
            int costTotal;
            int shippingTotal;
            if (connection == null)
            {
                JOptionPane.showMessageDialog(null, "The connection is null!!!!");
            }
            else if (cartTable == null || cartTable.getRowCount() == 0)
            {
                JOptionPane.showMessageDialog(null, "There is nothing in your cart!");
            }
            else
            {
                int maxTransactionId;
                int cost = 0;
                int shippingCost = 0;
                costTotal = 0;
                shippingTotal = 0;
                for (int i = 0; i < cartTable.getRowCount(); i++)
                {
                    try
                    {
                        // Checks if it is a dvd so that we can update the pricing
                        preparedStatement = connection.prepareStatement(listOfQueries.checkIfDvd);
                        preparedStatement.clearParameters();
                        preparedStatement.setString(1, (String) cartTable.getValueAt(i, 0));
                        System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                        resultSet = preparedStatement.executeQuery();
                        if (!resultSet.next())
                        {
                            System.out.println("No records found in DVDs!");
                        }
                        else
                        {
                            isAdvd = true;
                            System.out.println("ISADVD!");
                        }
                        preparedStatement.close();

                        if (!isAdvd)
                        {
                            // Checks if it is a book so that we can update the pricing
                            preparedStatement = connection.prepareStatement(listOfQueries.checkIfBook);
                            preparedStatement.clearParameters();
                            preparedStatement.setString(1, (String) cartTable.getValueAt(i, 0));
                            System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                            resultSet = preparedStatement.executeQuery();
                            if (!resultSet.next())
                            {
                                JOptionPane.showMessageDialog(null, "No records found!");
                                return;
                            }
                            else
                            {
                                isAbook = true;
                                System.out.println("ISABOOK!");
                            }
                        }
                        preparedStatement.close();

                        if (isAdvd || isAbook)
                        {
                            // GET MAX TRANSID
                            System.out.println("GET MAX TRANSID!");
                            preparedStatement = connection.prepareStatement(listOfQueries.maxTransactionID);
                            preparedStatement.clearParameters();
                            System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                            resultSet = preparedStatement.executeQuery();
                            if (!resultSet.next())
                            {
                                JOptionPane.showMessageDialog(null, "No records found!");
                                return;
                            }
                            else
                            {
                                maxTransactionId = resultSet.getInt(1) + 1;
                            }
                            preparedStatement.close();

                            // GET PRICE
                            System.out.println("GET PRICE!");
                            preparedStatement = connection.prepareStatement(listOfQueries.getMediaCost);
                            preparedStatement.clearParameters();
                            preparedStatement.setString(1, (String) cartTable.getValueAt(i, 0));
                            System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                            resultSet = preparedStatement.executeQuery();
                            if (!resultSet.next())
                            {
                                JOptionPane.showMessageDialog(null, "No records found!");
                                return;
                            }
                            else
                            {
                                System.out.println("geting cost!!!!");
                                cost = resultSet.getInt(1);
                                if (isAdvd)
                                {
                                    //cost = resultSet.getInt(1) + 1;
                                    shippingCost = 1;
                                    System.out.println("Cost: " + cost);
                                }
                                if (isAbook)
                                {
                                    //cost = resultSet.getInt(1) + 2;
                                    shippingCost = 2;
                                    System.out.println("Cost: " + cost);
                                }
                            }
                            preparedStatement.close();
                            numberOfCopiesToBuy = (int) cartTable.getValueAt(i, 2);
                            cost = cost * numberOfCopiesToBuy;
                            System.out.println("Cost:" + cost);
                            shippingCost = shippingCost * numberOfCopiesToBuy;
                            System.out.println("shippingCost!:" + shippingCost);
                            shippingTotal = shippingTotal + shippingCost;
                            System.out.println("shippingTotal!:" + shippingTotal);
                            costTotal = costTotal + cost;
                            System.out.println("costTotal!:" + costTotal);
                            if (costTotal > 50)
                            {
                                shippingTotal = 0;
                                System.out.println("Free Shipping!");
                            }
                            costTotal = costTotal + shippingTotal;// sets the shipping cost

                            // inserts purchase history
                            preparedStatement = connection.prepareStatement(listOfQueries.insertPurchase_History);//"INSERT INTO 'purchase_History' (transationID,date_of_purchase, number_of_copies, total_cost) VALUES(?,CURDATE(),?,?);";
                            preparedStatement.clearParameters();
                            preparedStatement.setInt(1, maxTransactionId);
                            preparedStatement.setInt(2, numberOfCopiesToBuy);//# copy purchased
                            preparedStatement.setInt(3, cost);
                            System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                            preparedStatement.execute();
                            preparedStatement.close();

                            // insert purchase
                            preparedStatement = connection.prepareStatement(listOfQueries.insertPurchase);//"INSERT INTO 'purchase' (usersID, title, transationID) VALUES (?,?,?);";
                            preparedStatement.clearParameters();
                            preparedStatement.setString(1, pointerToStoreFrame.userID);
                            preparedStatement.setString(2, (String) cartTable.getValueAt(i, 0));
                            preparedStatement.setInt(3, maxTransactionId);
                            System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                            preparedStatement.execute();
                            preparedStatement.close();
                        }
                    }
                    catch (SQLException sqle)
                    {
                        System.out.println("SQLException in ShoppingCart actionPerformed");
                        sqle.printStackTrace();
                    }
                }
                JOptionPane.showMessageDialog(null, "Transaction completed for $" + costTotal + " total [shipping was: $" + shippingTotal + "] ,we will ship it to you soon.");

                cartTableModel.cartTableDefaultListModel.clear();
                cartTableModel.fireTableDataChanged();
                removeItemButton.setEnabled(false);
                clearButton.setEnabled(false);
            }
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

    @Override
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

    @Override
    public void mousePressed(MouseEvent e)
    {
    }
//=====================================================

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }
//=====================================================

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }
//=====================================================

    @Override
    public void mouseExited(MouseEvent e)
    {
    }
//=====================================================
}
//#########################################################
