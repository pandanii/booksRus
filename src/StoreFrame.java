import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

//#########################################################
public class StoreFrame extends JFrame implements ActionListener,MouseListener
{
private static final long serialVersionUID = 1L;
JTable     myTable;
JPanel     scrollPanel;
Connection connection;
Queries    listOfQueries;

JPopupMenu rightClickMenu;
int        selectedRow;
Point      mousePoint;

Vector<Object> columnNames;
ShoppingCart   shoppingCart;

JTable cartTable;

boolean isAdmin;
boolean loggedIn;

String userID;
String password;
String phoneNumber;
String address;
String email;
String name;

JMenu logInMenu; //one dropdown part of the menu bar declared here so it can be referenced
JMenu adminMenu;

static final String JDBC_DRIVER  = "com.mysql.jdbc.Driver";
static final String DATABASE_URL = "jdbc:mysql://localhost:3306/movies&books"/*"jdbc:mysql://falcon-cs.fairmontstate.edu/DB00";SWAP THESE FOR SCHOOL EDITING.*/;
static final String USERNAME     = "root";
static final String PASSWORD     = "admin";//"password"  //Adust this according to local host login or server login

//=====================================================
@SuppressWarnings({"LeakingThisInConstructor", "OverridableMethodCallInConstructor"})
public StoreFrame()
{
    System.out.println("StoreFrame Constructor");
    Container cp;

    isAdmin = false;
    loggedIn = false;
    listOfQueries = new Queries();

    JPanel mainPanel;
    JPanel buttonPanel;

    JScrollPane myScrollPane;

    JButton closeButton;

    closeButton = new JButton("Close");
    closeButton.setActionCommand("CLOSE");
    closeButton.addActionListener(this);
    closeButton.setToolTipText("Close the program.");

    buttonPanel = new JPanel();
    buttonPanel.add(closeButton);

    scrollPanel = new JPanel();

    mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(scrollPanel, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    cp = getContentPane();
    cp.add(mainPanel, BorderLayout.CENTER);

    setupMainFrame();
    setupPopMenu();
    establishConnection();
    
    shoppingCart = new ShoppingCart(this); // MUST BE INITIALIZED AFTER THE CONNECTION IS ESTABLISHED TO THE SERVER
    logInMenu.getItem(0).doClick();// should click on DISPLAY_MEDIA button
}
//=====================================================
private void setupMainFrame()
{
    Toolkit   tk = Toolkit.getDefaultToolkit();
    Dimension d  = tk.getScreenSize();
    setSize(d.width/4, d.height/4);
    setLocation(d.width/3, d.height/3);
    setTitle("Books-R-Us");
    d.setSize(725, 550);
    setMinimumSize(d);
    setJMenuBar(createMenuBar());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
}
//=====================================================
private void setupPopMenu()
{
    Toolkit     tk;
    Dimension   d;
    JMenuItem   addToCartButton;
    JMenuItem   cancelButton;

    rightClickMenu = new JPopupMenu("Right Click Options");

    addToCartButton = new JMenuItem("Add to cart");
    addToCartButton.setActionCommand("ADDTOCART");
    addToCartButton.addActionListener(this);
    addToCartButton.setToolTipText("Add the selected item to the shopping cart.");
    addToCartButton.setPreferredSize(new Dimension(100,40));

    cancelButton = new JMenuItem("Cancel");
    cancelButton.setActionCommand("CANCELCLICK");
    cancelButton.addActionListener(this);
    cancelButton.setToolTipText("Close this popup.");
    cancelButton.setPreferredSize(new Dimension(100,40));

    rightClickMenu.add(addToCartButton);
    rightClickMenu.add(cancelButton);

    tk = Toolkit.getDefaultToolkit();
    d = tk.getScreenSize();
    rightClickMenu.setSize(d.width/4, d.height/5);
    rightClickMenu.setLocation(d.width/3, d.height/3);
    d.setSize(500, 50);
    rightClickMenu.setMinimumSize(d);
}

//=====================================================
private JMenuBar createMenuBar()
{
    JMenuBar menuBar;
    JMenu searchMenu;
    JMenu cartMenu;

    JMenuItem loginMenuItem;
    JMenuItem userInfoMenuItem;
    JMenuItem userPurchaseMenuItem;
    JMenuItem searchMenuItem;
    JMenuItem historyMenuItem;
    JMenuItem cartMenuItem;
    JMenuItem addCartMenuItem;
    JMenuItem buyCartMenuItem;

    JMenuItem adminDisplayUserMenuItem;
    JMenuItem adminAddUserMenuItem;
    JMenuItem adminRemoveUserMenuItem;
    JMenuItem adminDisplayAllMediaMenuItem;
    JMenuItem adminAddDvdMenuItem;
    JMenuItem adminAddBookMenuItem;
    JMenuItem adminRemoveMediaMenuItem;
    JMenuItem adminAdminLast24MenuItem;
    JMenuItem adminTop10MenuItem;

    menuBar = new JMenuBar();

    logInMenu = new JMenu("UserInfo/LogIn");

    loginMenuItem = new JMenuItem("Log In" , KeyEvent.VK_L);
    loginMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
    loginMenuItem.getAccessibleContext().setAccessibleDescription("Log into your Books-R-Us account.");
    loginMenuItem.setToolTipText("Log into your Books-R-Us account.");
    loginMenuItem.setActionCommand("LOGIN");
    loginMenuItem.addActionListener(this);
    logInMenu.add(loginMenuItem);

    userInfoMenuItem = new JMenuItem("User Info" , KeyEvent.VK_U);
    userInfoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK));
    userInfoMenuItem.getAccessibleContext().setAccessibleDescription("EDIT your Books-R-Us account info.");
    userInfoMenuItem.setToolTipText("EDIT your Books-R-Us account info.");
    userInfoMenuItem.setActionCommand("USERINFO");
    userInfoMenuItem.addActionListener(this);
    logInMenu.add(userInfoMenuItem);
    
    userPurchaseMenuItem = new JMenuItem("User Purchase Info");
    userPurchaseMenuItem.getAccessibleContext().setAccessibleDescription("User Purchase Info");
    userPurchaseMenuItem.setToolTipText("User Purchase Info");
    userPurchaseMenuItem.setActionCommand("USERPURCH");
    userPurchaseMenuItem.addActionListener(this);
    logInMenu.add(userPurchaseMenuItem);
    
    

    menuBar.add(logInMenu);


    cartMenu     = new JMenu("Shopping Cart");


    cartMenuItem = new JMenuItem("Open Shopping Cart" , KeyEvent.VK_C);
    cartMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
    cartMenuItem.getAccessibleContext().setAccessibleDescription("View your shopping cart.");
    cartMenuItem.setToolTipText("View your shopping cart.");
    cartMenuItem.setActionCommand("OPENCART");
    cartMenuItem.addActionListener(this);


    buyCartMenuItem = new JMenuItem("Buy NOW!" , KeyEvent.VK_B);
    buyCartMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
    buyCartMenuItem.getAccessibleContext().setAccessibleDescription("buy the selected item");
    buyCartMenuItem.setToolTipText("buy the selected item");
    buyCartMenuItem.setActionCommand("BUY");
    buyCartMenuItem.addActionListener(this);


    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    addCartMenuItem = new JMenuItem("Add To Cart" , KeyEvent.VK_A);
    addCartMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
    addCartMenuItem.getAccessibleContext().setAccessibleDescription("Add item to cart");
    addCartMenuItem.setToolTipText("Add item to cart");
    addCartMenuItem.setActionCommand("ADDTOCART");
    addCartMenuItem.addActionListener(this);
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    cartMenu.add(buyCartMenuItem);
    cartMenu.add(addCartMenuItem);
    cartMenu.add(cartMenuItem);
    menuBar.add(cartMenu);

    searchMenu     = new JMenu("Search");
    searchMenuItem = new JMenuItem("Search Options" , KeyEvent.VK_S);
    searchMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
    searchMenuItem.getAccessibleContext().setAccessibleDescription("Search the store.");
    searchMenuItem.setToolTipText("Search the store.");
    searchMenuItem.setActionCommand("SEARCH");
    searchMenuItem.addActionListener(this);


    historyMenuItem = new JMenuItem("Purchase History" , KeyEvent.VK_H);
    historyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
    historyMenuItem.getAccessibleContext().setAccessibleDescription("Show users history");
    historyMenuItem.setToolTipText("Show users history");
    historyMenuItem.setActionCommand("HISTORY");
    historyMenuItem.addActionListener(this);

    searchMenu.add(searchMenuItem);
    searchMenu.add(historyMenuItem);
    menuBar.add(searchMenu);

    adminMenu = new JMenu("Admin");
    adminDisplayUserMenuItem = new JMenuItem("Display Users" , KeyEvent.VK_D);
    adminDisplayUserMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
    adminDisplayUserMenuItem.getAccessibleContext().setAccessibleDescription("Display Users");
    adminDisplayUserMenuItem.setToolTipText("Display Users");
    adminDisplayUserMenuItem.setActionCommand("DISPLAY_USERS");
    adminDisplayUserMenuItem.addActionListener(this);
    adminMenu.add(adminDisplayUserMenuItem);

    adminDisplayAllMediaMenuItem = new JMenuItem("Display Media" , KeyEvent.VK_M);
    adminDisplayAllMediaMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.ALT_MASK));
    adminDisplayAllMediaMenuItem.getAccessibleContext().setAccessibleDescription("Display Media");
    adminDisplayAllMediaMenuItem.setToolTipText("Display Media");
    adminDisplayAllMediaMenuItem.setActionCommand("DISPLAY_MEDIA");
    adminDisplayAllMediaMenuItem.addActionListener(this);
    adminMenu.add(adminDisplayAllMediaMenuItem);

    adminRemoveUserMenuItem = new JMenuItem("Remove User" , KeyEvent.VK_R);
    adminRemoveUserMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
    adminRemoveUserMenuItem.getAccessibleContext().setAccessibleDescription("Remove User");
    adminRemoveUserMenuItem.setToolTipText("Remove User");
    adminRemoveUserMenuItem.setActionCommand("REMOVE_USER");
    adminRemoveUserMenuItem.addActionListener(this);
    adminMenu.add(adminRemoveUserMenuItem);

    adminRemoveMediaMenuItem = new JMenuItem("Remove Media" , KeyEvent.VK_E);
    adminRemoveMediaMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
    adminRemoveMediaMenuItem.getAccessibleContext().setAccessibleDescription("Remove Media");
    adminRemoveMediaMenuItem.setToolTipText("Remove Media");
    adminRemoveMediaMenuItem.setActionCommand("REMOVE_MEDIA");
    adminRemoveMediaMenuItem.addActionListener(this);
    adminMenu.add(adminRemoveMediaMenuItem);

    adminAddUserMenuItem = new JMenuItem("Add New User" , KeyEvent.VK_A);
    adminAddUserMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
    adminAddUserMenuItem.getAccessibleContext().setAccessibleDescription("Add New User");
    adminAddUserMenuItem.setToolTipText("Add New User");
    adminAddUserMenuItem.setActionCommand("ADD_USER");
    adminAddUserMenuItem.addActionListener(this);
    adminMenu.add(adminAddUserMenuItem);

    adminAddBookMenuItem = new JMenuItem("Add Book" , KeyEvent.VK_B);
    adminAddBookMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
    adminAddBookMenuItem.getAccessibleContext().setAccessibleDescription("Add Book");
    adminAddBookMenuItem.setToolTipText("Add Book");
    adminAddBookMenuItem.setActionCommand("ADD_BOOK");
    adminAddBookMenuItem.addActionListener(this);
    adminMenu.add(adminAddBookMenuItem);

    adminAddDvdMenuItem = new JMenuItem("Add DVD" , KeyEvent.VK_I);
    adminAddDvdMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK));
    adminAddDvdMenuItem.getAccessibleContext().setAccessibleDescription("Add DVD");
    adminAddDvdMenuItem.setToolTipText("Add DVD");
    adminAddDvdMenuItem.setActionCommand("ADD_DVD");
    adminAddDvdMenuItem.addActionListener(this);
    adminMenu.add(adminAddDvdMenuItem);
    
    adminAdminLast24MenuItem = new JMenuItem("Orders in last 24 hrs");
    adminAdminLast24MenuItem.getAccessibleContext().setAccessibleDescription("Orders in last 24 hrs");
    adminAdminLast24MenuItem.setToolTipText("Orders in last 24 hrs");
    adminAdminLast24MenuItem.setActionCommand("AD_LAST_24");
    adminAdminLast24MenuItem.addActionListener(this);
    adminMenu.add(adminAdminLast24MenuItem);
    
    adminTop10MenuItem = new JMenuItem("Admin Top Ten");
    adminTop10MenuItem.getAccessibleContext().setAccessibleDescription("Admin Top Ten");
    adminTop10MenuItem.setToolTipText("Admin Top Ten");
    adminTop10MenuItem.setActionCommand("ADMIN_TOP10");
    adminTop10MenuItem.addActionListener(this);
    adminMenu.add(adminTop10MenuItem);
    
    menuBar.add(adminMenu);
    adminMenu.setEnabled(false);

    return menuBar;
}
//=====================================================
@Override
public void actionPerformed(ActionEvent e)
{
    PreparedStatement preparedStatement;
    ResultSet         resultSet = null;
    boolean           isAdvd    = false;
    boolean           isAbook   = false;
    rightClickMenu.setVisible(false);

    if (e.getActionCommand().equals("CLOSE"))
    {
        try
        {
            if (connection != null)
            {
                connection.close();
            }
        }
        catch (SQLException sqle1)
        {
            System.out.println("SQLException in StoreFrame actionPerformed");
            JOptionPane.showMessageDialog(null, "Unable to sever connection.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            sqle1.printStackTrace();
        }
        System.exit(0);
    }
    else if (e.getActionCommand().equals("CANCELCLICK"))
    {
    rightClickMenu.setVisible(false);
    }
    else if (e.getActionCommand().equals("USERINFO"))
    {
        if (connection != null && loggedIn == true)
        {
            ModifyUserJDialog modifyUserJDialog = new ModifyUserJDialog(this);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "No connection to database, or you are not logged in.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    else if (e.getActionCommand().equals("LOGIN"))
    {
        System.out.println("Creating LoginJDialog");
        if (!loggedIn)
        {
            if (connection != null)
            {
                LoginJDialog loginJDialog = new LoginJDialog(this); //sending the JDialog a pointer to this instance of StoreFrame so it can all one of its methods
                System.out.println("Good connection");
            }
            else
            {
                System.out.println("Null connection");
                JOptionPane.showMessageDialog(null, "Not connected.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            //change the menu item back to Login
            logInMenu.setText("User Info/Login");
            logInMenu.getItem(0).setText("LogIn");  //getting the JMenuItem
            isAdmin  = false;
            loggedIn = false;
            userID = null;
            adminMenu.setEnabled(false);
            scrollPanel.removeAll();// REMOVES THE OLD TABLE FROM THE PANEL
            this.setTitle("Books-R-Us");
            this.repaint(); //causes the whole frame to repaint with the update to its components
        }
    }
    else if (e.getActionCommand().equals("BUY"))
    {
        if(myTable == null)
        {
            JOptionPane.showMessageDialog(null,"There is nothing displayed!");
        }
        else if(myTable.getSelectedRow() == -1)
        {
            JOptionPane.showMessageDialog(null,"Nothing seems to be selected!");
        }
        else
        {
            int option = JOptionPane.showConfirmDialog(null,"Attempting to Buy "+myTable.getValueAt(myTable.getSelectedRow(),0)+ " would you like to continue?", "choose one", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION && loggedIn)
            {
                int maxTransactionId;
                int cost = 0;
                try
                {
                    // Checks if it is a dvd so that we can update the pricing
                    preparedStatement = connection.prepareStatement(listOfQueries.checkIfDvd);
                    preparedStatement.clearParameters();
                    preparedStatement.setString(1, (String)myTable.getValueAt(myTable.getSelectedRow(),0));
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

                    if(!isAdvd)
                    {
                        // Checks if it is a book so that we can update the pricing
                        preparedStatement = connection.prepareStatement(listOfQueries.checkIfBook);
                        preparedStatement.clearParameters();
                        preparedStatement.setString(1, (String)myTable.getValueAt(myTable.getSelectedRow(),0));
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

                    if(isAdvd || isAbook)
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
                        preparedStatement.setString(1, (String)myTable.getValueAt(myTable.getSelectedRow(),0));
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
                            if(isAdvd)
                            {
                                if(cost > 50)
                                {
                                  System.out.println("Free shipping!!!!!");   
                                }
                                else
                                {
                                    cost = resultSet.getInt(1) + 1;
                                }
                            }
                            if(isAbook)
                            {
                                if(cost > 50)
                                {
                                  System.out.println("Free shipping!!!!!");   
                                }
                                else
                                {
                                    cost = resultSet.getInt(1) + 1;
                                }
                            }
                            System.out.println("Cost: " + cost);
                        }
                        preparedStatement.close();


                        // inserts purchase history
                        preparedStatement = connection.prepareStatement(listOfQueries.insertPurchase_History);//"INSERT INTO 'purchase_History' (transationID,date_of_purchase, number_of_copies, total_cost) VALUES(?,CURDATE(),?,?);";
                        preparedStatement.clearParameters();
                        preparedStatement.setInt(1, maxTransactionId);
                        preparedStatement.setInt(2, 1);// only 1 copy purchased
                        preparedStatement.setInt(3, cost);
                        System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                        preparedStatement.execute();
                        preparedStatement.close();

                        // insert purchase
                        preparedStatement = connection.prepareStatement(listOfQueries.insertPurchase);//"INSERT INTO 'purchase' (usersID, title, transationID) VALUES (?,?,?);";
                        preparedStatement.clearParameters();
                        preparedStatement.setString(1, userID);
                        preparedStatement.setString(2, (String)myTable.getValueAt(myTable.getSelectedRow(),0));
                        preparedStatement.setInt(3, maxTransactionId);
                        System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                        preparedStatement.execute();
                        preparedStatement.close();
                        JOptionPane.showMessageDialog(null, "You just bought "+myTable.getValueAt(myTable.getSelectedRow(),0)+" for $"+ cost+",  we will ship it to you soon.");
                    }
                }
                catch(SQLException sqle)
                {
                    System.out.println("SQLException in StoreFrame actionPerformed");
                    sqle.printStackTrace();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "You must be logged in first.");
            }
        }
    }
    else if (e.getActionCommand().equals("ADDTOCART"))
    {
    try
        {
            System.out.println("Attempting to ADDTOCART");
            String titleToAdd = null;
            String response;
            int priceToAdd = 0;
            int numberInStock = 0;
            int quantityToPurchase;
            Vector<Object> cartRow = new Vector<>();
            if (myTable != null)
            {
                for (int i=0; i < myTable.getColumnCount(); i++)
                {
                    if (myTable.getColumnName(i).equals("Amount in Stock"))
                    {
                        numberInStock = (int)(myTable.getValueAt(myTable.getSelectedRow(), i));
                    }
                    if (myTable.getColumnName(i).equals("DVD Title") || myTable.getColumnName(i).equals("Book Title"))
                    {
                        titleToAdd = (String)myTable.getValueAt(myTable.getSelectedRow(), i);
                    }
                    if (myTable.getColumnName(i).equals("Price"))
                    {
                        priceToAdd = (int)myTable.getValueAt(myTable.getSelectedRow(), i);
                    }
                }
            }
            System.out.println("titleToAdd " + titleToAdd);
            System.out.println("numberInStock " + numberInStock);
            System.out.println("priceToAdd " + priceToAdd);
            quantityToPurchase = 0;
            if (numberInStock > 0 && titleToAdd != null)
            {
                response = JOptionPane.showInputDialog("Enter the quantity of " + titleToAdd + " you'd like to purchase.");
                quantityToPurchase = Integer.parseInt(response);
                System.out.println("quantityToPurchase " + quantityToPurchase);
                if (quantityToPurchase > numberInStock)
                {
                    JOptionPane.showMessageDialog(null, "Too few items in stock for that transaction.", "Failed", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    cartRow.add(titleToAdd);
                    cartRow.add(priceToAdd);
                    cartRow.add(quantityToPurchase);
                    shoppingCart.addItemToCart(cartRow);    //should take a rowList to add it to the JTable for each purchased item//that table has media title, media cost, and quantity as columns already
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Couldn't add item to cart.", "Failed", JOptionPane.ERROR_MESSAGE);
            }
    }
    catch(NumberFormatException nfe)
    {
            System.out.println("NumberFormatException in StoreFrame actionPerformed");
            JOptionPane.showMessageDialog(null, "Enter a number for the quantity.", "Failed", JOptionPane.ERROR_MESSAGE);
            nfe.printStackTrace();
    }

    }
    else if (e.getActionCommand().equals("OPENCART"))
    {
        shoppingCart.setVisible(true);
    }
    else if (e.getActionCommand().equals("SEARCH"))
    {
        if(connection != null)
        {
            SearchJDialog searchJDialog = new SearchJDialog(this); //sending it 'this' so it can call a method of StoreFrame later on.
        }
        else
        {
            System.out.println("No connection to database.");
            JOptionPane.showMessageDialog(null, "Not connected.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    else if (e.getActionCommand().equals("HISTORY"))
    {
        if(connection != null && userID != null)
        {
            try
            {
                preparedStatement = connection.prepareStatement(listOfQueries.purchase_History);
                preparedStatement.clearParameters();
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                preparedStatement.setString(1, userID);   //only the logged in user can see their history
                resultSet = preparedStatement.executeQuery();
            }
            catch (SQLException sqle)
            {
                System.out.println("SQLException in StoreFrame actionPerformed");
                sqle.printStackTrace();
            }
            if (resultSet != null)
            {
                this.updateResultTable(resultSet);   //sending the resultSet to StoreFrame to be displayed
            }
        }
        else
        {
            System.out.println("No connection to database, or you are not logged in.");
            JOptionPane.showMessageDialog(null, "No connection to database, or you are not logged in.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    else if (e.getActionCommand().equals("DISPLAY_USERS"))
    {
        try
        {
            preparedStatement = connection.prepareStatement(listOfQueries.displayUsers);
            preparedStatement.clearParameters();
            System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
        }
        catch (SQLException sqle)
        {
            System.out.println("SQLException in StoreFrame actionPerformed");
            sqle.printStackTrace();
        }
        if (resultSet != null)
        {
            this.updateResultTable(resultSet);   //sending the resultSet to StoreFrame to be displayed
        }
    }
    else if (e.getActionCommand().equals("DISPLAY_MEDIA"))
    {
        try
        {
            preparedStatement = connection.prepareStatement(listOfQueries.displayMedia);
            preparedStatement.clearParameters();
            System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
        }
        catch (SQLException sqle)
        {
            System.out.println("SQLException in StoreFrame actionPerformed");
            sqle.printStackTrace();
        }
        if (resultSet != null)
        {
            this.updateResultTable(resultSet);   //sending the resultSet to StoreFrame to be displayed
        }
    }
    else if (e.getActionCommand().equals("REMOVE_MEDIA"))
    {
        if(myTable.getSelectedRow() == -1)
        {
            JOptionPane.showMessageDialog(null,"Nothing seems to be selected!");
        }
        else
        {
            int option = JOptionPane.showConfirmDialog(null,"Attempting to delete "+myTable.getValueAt(myTable.getSelectedRow(),0)+ " would you like to continue?", "choose one", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION)
            {
                try
                {

                    preparedStatement = connection.prepareStatement(listOfQueries.deletePurchase);
                    preparedStatement.clearParameters();
                    preparedStatement.setString(1, (String)myTable.getValueAt(myTable.getSelectedRow(),0));
                    System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                    preparedStatement.execute();
                    preparedStatement.close();

                    preparedStatement = connection.prepareStatement(listOfQueries.deleteWrittenBy);
                    preparedStatement.clearParameters();
                    preparedStatement.setString(1, (String)myTable.getValueAt(myTable.getSelectedRow(),0));
                    System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                    preparedStatement.execute();
                    preparedStatement.close();

                    preparedStatement = connection.prepareStatement(listOfQueries.deleteMedia);
                    preparedStatement.clearParameters();
                    preparedStatement.setString(1, (String)myTable.getValueAt(myTable.getSelectedRow(),0));
                    System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                    preparedStatement.execute();
                    preparedStatement.close();
                    adminMenu.getItem(1).doClick();// should click on DISPLAY_MEDIA button
                }
                catch (SQLException sqle)
                {
                    System.out.println("SQLException in StoreFrame actionPerformed");
                    sqle.printStackTrace();
                }
            }
        }
    }
    else if (e.getActionCommand().equals("REMOVE_USER"))
    {
        if(myTable.getSelectedRow() == -1)
        {
            JOptionPane.showMessageDialog(null,"Nothing seems to be selected!");
        }
        else
        {
            int option = JOptionPane.showConfirmDialog(null,"Attempting to delete "+myTable.getValueAt(myTable.getSelectedRow(),0)+ " would you like to continue?", "choose one", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION)
            {
                try
                {
                    preparedStatement = connection.prepareStatement(listOfQueries.deleteUser);
                    preparedStatement.clearParameters();
                    preparedStatement.setString(1, (String)myTable.getValueAt(myTable.getSelectedRow(),0));
                    System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                    preparedStatement.execute();
                    preparedStatement.close();
                    adminMenu.getItem(0).doClick();// should click on REMOVE_USER button
                }
                catch (SQLException sqle)
                {
                    System.out.println("SQLException in StoreFrame actionPerformed");
                    sqle.printStackTrace();
                }
            }
        }
    }
    else if (e.getActionCommand().equals("ADD_USER"))
    {
        if(connection != null)
        {
            AddUserJDialog addUserJDialog = new AddUserJDialog(this);
        }
        else
        {
            System.out.println("No connection to database.");
            JOptionPane.showMessageDialog(null, "Not connected.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    else if(e.getActionCommand().equals("ADD_BOOK"))
    {
        if(connection != null)
        {
            AddBookDialog addBookDialog = new AddBookDialog(this);
        }
        else
        {
            System.out.println("No connection to database.");
            JOptionPane.showMessageDialog(null, "Not connected.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    else if(e.getActionCommand().equals("ADD_DVD"))
    {
        if(connection != null)
        {
            AddDvdDialog addDvdDialog = new AddDvdDialog(this);
        }
        else
        {
            System.out.println("No connection to database.");
            JOptionPane.showMessageDialog(null, "Not connected.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    else if(e.getActionCommand().equals("AD_LAST_24"))
    {
        try
        {
            System.out.println("comboObject.toString().equals(\"Admin Last 24 hours\")");//DEBUG
            preparedStatement = connection.prepareStatement(listOfQueries.admin_In_Last_24h);
            preparedStatement.clearParameters();
            System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null)
            {
                this.updateResultTable(resultSet);   //sending the resultSet to StoreFrame to be displayed
            }
        }
        catch (SQLException sqle) 
        {
            System.out.println("SQLException in StoreFrame actionPerformed");
            sqle.printStackTrace();
        }
    }
    else if(e.getActionCommand().equals("ADMIN_TOP10"))
    {
        try
        {
            System.out.println("!!ADMIN_TOP10!!");//DEBUG
            preparedStatement = connection.prepareStatement(listOfQueries.admin_top_10);
            preparedStatement.clearParameters();
            System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null)
            {
                this.updateResultTable(resultSet);   //sending the resultSet to StoreFrame to be displayed
            }
        }
        catch (SQLException sqle) 
        {
            System.out.println("SQLException in StoreFrame actionPerformed");
            sqle.printStackTrace();
        }
    }
    else if(e.getActionCommand().equals("USERPURCH"))
    {
        try
        {
            System.out.println("Purchase History");//DEBUG
            preparedStatement = connection.prepareStatement(listOfQueries.purchase_History);
            preparedStatement.clearParameters();
            System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
            preparedStatement.setString(1, userID);   //only the logged in user can see their history
            resultSet = preparedStatement.executeQuery();
            if (resultSet != null)
            {
                this.updateResultTable(resultSet);   //sending the resultSet to StoreFrame to be displayed
            }
        }
        catch (SQLException sqle) 
        {
            System.out.println("SQLException in StoreFrame actionPerformed");
            sqle.printStackTrace();
        }
    }
}
//=====================================================
public void updateResultTable(ResultSet resultSet)
{
    ResultSetMetaData resultMetaData;
    Vector<Object> columnNames;
    Vector<Object> currentRow;
    Vector<Object> rowList;
    JScrollPane myScrollPane;
    scrollPanel.removeAll();// REMOVES THE OLD TABLE FROM THE PANEL
    try
    {
        if (!resultSet.first())
        {
            System.out.println("No records");
            JOptionPane.showMessageDialog(null, "No results found.", "No results", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            columnNames = new Vector<>();
            currentRow  = new Vector<>();
            rowList     = new Vector<>();
            resultMetaData = resultSet.getMetaData();

            for (int i=0; i < resultMetaData.getColumnCount(); i++)
            {
                columnNames.addElement(resultMetaData.getColumnLabel(i+1));// USING getColumnLabel() INSTEAD OF getColumnName() ALLOWS FOR ALIASING!
            }
            do
            {
                currentRow = new Vector<>();
                for (int j=0; j < resultMetaData.getColumnCount(); j++)
                {
                    currentRow.addElement(resultSet.getObject(j+1));
                }
                rowList.addElement(currentRow);
            }
            while (resultSet.next());

            myTable = new JTable(rowList, columnNames);
            myTable.addMouseListener(this);
            myScrollPane = new JScrollPane(myTable);
            myScrollPane.setPreferredSize(new Dimension(700, 400));
            scrollPanel.add(myScrollPane);
//            myJTable.scrollRectToVisible(myJTable.getCellRect(myJTable.getRowCount() - 1, 0, true));
            validate();
        }
        resultSet.close();
    }
    catch (SQLException sqle2)
    {
        System.out.println("SQLException2 in StoreFrame actionPerformed");
        JOptionPane.showMessageDialog(null, "Query Error.", "Query Error", JOptionPane.ERROR_MESSAGE);
    }
}
//=====================================================
public void establishConnection()
{
    try
    {
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
    }
    catch (ClassNotFoundException cnfe)
    {
        System.out.println("ClassNotFoundException in LoginJDialog establishConnection");
        JOptionPane.showMessageDialog(null, "Failed to load driver.", "Failed to connect", JOptionPane.ERROR_MESSAGE);
        cnfe.printStackTrace();
    }
    catch (SQLException sqle)
    {
        System.out.println("SQLException in LoginJDialog establishConnection");
        JOptionPane.showMessageDialog(null, "Failed to create a connection with the database.", "Failed to connect", JOptionPane.ERROR_MESSAGE);
        sqle.printStackTrace();
    }
}
//=====================================================
public void setUserInfo(String userID, String password, String phoneNumber, String address, String email, String name, boolean isAdmin)
{
    this.userID = userID;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.email = email;
    this.name = name;
    this.isAdmin = isAdmin;

    loggedIn = true;

    if(isAdmin)
    {
        adminMenu.setEnabled(true);
    }

    logInMenu.setText("User Info/Logout");
    logInMenu.getItem(0).setText("Logout");  //getting the JMenuItem
    this.setTitle("Books-R-Us" + " - Signed in as: " + userID);
    this.repaint();     //causes the whole frame to repaint with the update to its components
}
    //=====================================================
@Override
public void mouseClicked(MouseEvent e)
{
    System.out.println("StoreFrame mouseClicked");
    if(e.getButton() == MouseEvent.BUTTON3)
    {
        mousePoint = e.getPoint();
        selectedRow = myTable.rowAtPoint(mousePoint);
        System.out.println(selectedRow + " SELECTED");
        myTable.changeSelection(selectedRow, 0, false, false);
        rightClickMenu.setVisible(true);
    }
    else
    {
    rightClickMenu.setVisible(false);
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
    public Connection copyConnection()
    {
        return connection;
    }
    //=====================================================
public void createNewUser(String userID, String password, String phoneNumber, String address, String email, String name, int isAdmin)
{
/*
System.out.println("UserID" + userID);
System.out.println("password" + password);
System.out.println("phoneNumber" + phoneNumber);
System.out.println("address" + address);
System.out.println("email" + email);
System.out.println("name" + name);
System.out.println("isAdmin" + isAdmin);
*/
    PreparedStatement preparedStatement;
    try
    {
        preparedStatement = connection.prepareStatement(listOfQueries.newUser);
        preparedStatement.clearParameters();
        preparedStatement.setString(1, userID);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, phoneNumber);
        preparedStatement.setString(4, address);
        preparedStatement.setString(5, email);
        preparedStatement.setString(6, name);
        preparedStatement.setInt(7, isAdmin);

        System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
        preparedStatement.execute();
        preparedStatement.close();
        adminMenu.getItem(0).doClick();// should click on REMOVE_USER button
    }
    catch (SQLException sqle)
    {
        System.out.println("SQLException in StoreFrame createNewUser");
        sqle.printStackTrace();
    }
}
    //=====================================================
public void modifyUserInfo(String userID, String password, String phoneNumber, String address, String email, String name, boolean isAdmin)
{
//UPDATE Users SET password = ?, phone_number = ?, address = ?, email = ?, name = ? WHERE userID = ?
/*
System.out.println("UserID" + userID);
System.out.println("password" + password);
System.out.println("phoneNumber" + phoneNumber);
System.out.println("address" + address);
System.out.println("email" + email);
System.out.println("name" + name);
System.out.println("isAdmin" + isAdmin);
*/
    PreparedStatement preparedStatement;
    try
    {
        preparedStatement = connection.prepareStatement(listOfQueries.updateUser);
        preparedStatement.clearParameters();
        preparedStatement.setString(1, password);
        preparedStatement.setString(2, phoneNumber);
        preparedStatement.setString(3, address);
        preparedStatement.setString(4, email);
        preparedStatement.setString(5, name);
        preparedStatement.setString(6, userID);
        System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
        preparedStatement.execute();
        preparedStatement.close();
        this.setUserInfo(userID, password, phoneNumber, address, email, name, isAdmin);
    }
    catch (SQLException sqle)
    {
        System.out.println("SQLException in StoreFrame createNewUser");
        sqle.printStackTrace();
    }

}
//=====================================================
}
//#########################################################
