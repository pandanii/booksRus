//BooksRUs Software


import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;


//#########################################################
public class StoreFrame extends JFrame implements ActionListener,MouseListener
{
JTable     myTable;
JPanel     scrollPanel;
Connection connection;
Queries    listOfQueries;

JMenu      adminMenu;
JPopupMenu rightClickMenu;
int selectedRow;
Point mousePoint;

Vector<Object> columnNames;
ShoppingCart shoppingCart;

JTable cartTable;

boolean isAdmin;
String username;
boolean loggedIn;

JMenu logInMenu; //one dropdown part of the menu bar declared here so it can be referenced

static final String JDBC_DRIVER  = "com.mysql.jdbc.Driver";
static final String DATABASE_URL = "jdbc:mysql://localhost:3306/movies&books"/*"jdbc:mysql://falcon-cs.fairmontstate.edu/DB00";SWAP THESE FOR SCHOOL EDITING.*/;
static final String USERNAME     = "root";
static final String PASSWORD     = "password";  //Adust this according to local host login or server login

//=====================================================
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

    shoppingCart = new ShoppingCart(this);

    setupMainFrame();
    setupPopMenu();

    establishConnection();
}
//=====================================================
private void setupMainFrame()
{
    Toolkit tk;
    Dimension d;
    tk = Toolkit.getDefaultToolkit();
    d = tk.getScreenSize();
    setSize(d.width/4, d.height/4);
    setLocation(d.width/3, d.height/3);
    setTitle("Books-R-Us");
    d.setSize(550, 550);
    setMinimumSize(d);
    setJMenuBar(createMenuBar());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
}
//=====================================================
private void setupPopMenu()
{
    Toolkit         tk;
    Dimension        d;
    JMenuItem addToCartButton;
    JMenuItem editRowButton;

    rightClickMenu = new JPopupMenu("Right Click Options");

    if (loggedIn)
    {
        addToCartButton = new JMenuItem("Add to Cart");
        addToCartButton.setActionCommand("ADDTOCART");
        addToCartButton.addActionListener(this);
        addToCartButton.setToolTipText("Add this item to your checkout cart.");
        addToCartButton.setPreferredSize(new Dimension(100,40));
        rightClickMenu.add(addToCartButton);
        rightClickMenu.addSeparator();
    }
    if (isAdmin)
    {
        editRowButton = new JMenuItem("Edit");
        editRowButton.setActionCommand("EDITROW");
        editRowButton.addActionListener(this);
        editRowButton.setToolTipText("Edit this row.");
        editRowButton.setPreferredSize(new Dimension(100,40));
        rightClickMenu.add(editRowButton);
    }

    tk = Toolkit.getDefaultToolkit();
    d = tk.getScreenSize();
    rightClickMenu.setSize(d.width/4, d.height/5);
    rightClickMenu.setLocation(MouseInfo.getPointerInfo().getLocation());  //gets the mouse location and turns it into a point
    d.setSize(500, 250);
    rightClickMenu.setMinimumSize(d);
}

//=====================================================
private JMenuBar createMenuBar()
{
    JMenuBar menuBar;
//    JMenu logInMenu; //made this have a larger scope so it can be used later to logout when user is logged in.
    JMenu viewMenu;
    JMenu searchMenu;
    JMenu cartMenu;

    JMenuItem viewMenuItem;
    JMenuItem loginMenuItem;
    JMenuItem userInfoMenuItem;
    JMenuItem searchMenuItem;
    JMenuItem historyMenuItem;
    JMenuItem addcartMenuItem;
    JMenuItem cartMenuItem;

    JMenuItem adminDisplayUserMenuItem;
    JMenuItem adminAddUserMenuItem;
    JMenuItem adminRemoveUserMenuItem;
    JMenuItem adminDisplayAllMediaMenuItem;
    JMenuItem adminAddDvdMenuItem;
    JMenuItem adminAddBookMenuItem;
    JMenuItem adminRemoveMediaMenuItem;

    menuBar = new JMenuBar();

    logInMenu = new JMenu("UserInfo/LogIn");

    loginMenuItem = new JMenuItem("Log In" , KeyEvent.VK_L);
    loginMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
    loginMenuItem.getAccessibleContext().setAccessibleDescription("Log into your Books-R-Us account.");
    loginMenuItem.setToolTipText("Log into your Books-R-Us account.");
    loginMenuItem.setActionCommand("LOGIN");
    loginMenuItem.addActionListener(this);

    userInfoMenuItem = new JMenuItem("User Info" , KeyEvent.VK_U);
    userInfoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK));
    userInfoMenuItem.getAccessibleContext().setAccessibleDescription("EDIT your Books-R-Us account info.");
    userInfoMenuItem.setToolTipText("EDIT your Books-R-Us account info.");
    userInfoMenuItem.setActionCommand("USERINFO");
    userInfoMenuItem.addActionListener(this);

    logInMenu.add(loginMenuItem);
    logInMenu.add(userInfoMenuItem);
    menuBar.add(logInMenu);

    viewMenu     = new JMenu("View");
    viewMenuItem = new JMenuItem("View Options" , KeyEvent.VK_V);
    viewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
    viewMenuItem.getAccessibleContext().setAccessibleDescription("Change the appearance.");
    viewMenuItem.setToolTipText("Change the appearance of the window.");
    viewMenuItem.setActionCommand("VIEW");
    viewMenuItem.addActionListener(this);

    viewMenu.add(viewMenuItem);
    menuBar.add(viewMenu);
    cartMenu     = new JMenu("Shopping Cart");
    cartMenuItem = new JMenuItem("Open Shopping Cart" , KeyEvent.VK_C);
    cartMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
    cartMenuItem.getAccessibleContext().setAccessibleDescription("View your shopping cart.");
    cartMenuItem.setToolTipText("View your shopping cart.");
    cartMenuItem.setActionCommand("OPENCART");
    cartMenuItem.addActionListener(this);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

	    addcartMenuItem = new JMenuItem("Add" , KeyEvent.VK_A);
	    addcartMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
	    addcartMenuItem.getAccessibleContext().setAccessibleDescription("Add item to cart");
	    addcartMenuItem.setToolTipText("Add item to cart");
	    addcartMenuItem.setActionCommand("ADDTOCART");
    addcartMenuItem.addActionListener(this);
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    cartMenu.add(addcartMenuItem);
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

    adminRemoveMediaMenuItem = new JMenuItem("Remove Media" , KeyEvent.VK_R);
    adminRemoveMediaMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
    adminRemoveMediaMenuItem.getAccessibleContext().setAccessibleDescription("Remove Media");
    adminRemoveMediaMenuItem.setToolTipText("Remove Media");
    adminRemoveMediaMenuItem.setActionCommand("REMOVE_MEDIA");
    adminRemoveMediaMenuItem.addActionListener(this);
    adminMenu.add(adminRemoveMediaMenuItem);

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
            //sqle1.printStackTrace();
        }
        System.exit(0);
    }
    else if (e.getActionCommand().equals("USERINFO"))
    {
        // HERE WE WILL OPEN THE DIALOG FOR THE USERS INFO
    }
    else if (e.getActionCommand().equals("VIEW"))
    {
        //new JDialog to change appearance of the frame
    }
    else if (e.getActionCommand().equals("LOGIN"))
    {
    System.out.println("Creating LoginJDialog");
    if (!loggedIn)
        {
        if (connection != null)
            {
            new LoginJDialog(this);     //sending the JDialog a pointer to this instance of
                                        //StoreFrame so it can all one of its methods
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
        logInMenu.setText("Login");
        logInMenu.getItem(0).setText("LogIn");  //getting the JMenuItem
        isAdmin  = false;
        loggedIn = false;
        username = null;
        adminMenu.setEnabled(false);
        this.setTitle("Books-R-Us");
        this.repaint(); //causes the whole frame to repaint with the update to its components
        }

    }
    else if (e.getActionCommand().equals("ADDTOCART"))
    {
        int currentColumnIndex;
        int numberInStockColumnIndex = 0;
        Vector<Object> cartRowList = new Vector<Object>(); //only ever size one in this setup
        Vector<Object> currentRow = new Vector<Object>();

        for (int i=0; i < myTable.getColumnCount(); i++)
            {
                if (myTable.getColumnName(i).equals("Amount in Stock"))
                {
                    numberInStockColumnIndex = i;
                }
            }


        if ((int)myTable.getValueAt(selectedRow, numberInStockColumnIndex) > 0)
            {

            currentColumnIndex = 0;
            while (currentColumnIndex < myTable.getColumnCount())
                {
                currentRow.add(myTable.getValueAt(selectedRow, currentColumnIndex));  //selectedRow comes from mouselistener click
                                                                                      //do JTables index at 0? This assumes so.
                currentColumnIndex = currentColumnIndex + 1;
                }                                                                     //current column index to walk through the rows columns

            cartRowList.add(currentRow);

            shoppingCart.addItemToCart(cartRowList, columnNames);    //should take a list of rows and column names to make a new JTable for each purchased item
                                                                    //easiest way to handle that book's columns != DVD's columns
            }
        else
            {
            JOptionPane.showMessageDialog(null, "Couldn't add item to cart.", "Out of Stock", JOptionPane.ERROR_MESSAGE);
            }

    }
    else if (e.getActionCommand().equals("EDITROW"))
    {

    }
    else if (e.getActionCommand().equals("OPENCART"))
    {
        shoppingCart.setVisible(true);
    }
    else if (e.getActionCommand().equals("SEARCH"))
    {
    if(connection != null)
        {
        new SearchJDialog(this);    //sending it 'this' so it can call a method of StoreFrame later on.
        }
    else
        {
        System.out.println("No connection to database.");
        JOptionPane.showMessageDialog(null, "Not connected.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    else if (e.getActionCommand().equals("HISTORY"))
    {
        if(connection != null && username != null)
        {
            try
            {
                preparedStatement = connection.prepareStatement(listOfQueries.purchase_History);
                preparedStatement.clearParameters();
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                preparedStatement.setString(1, username);   //only the logged in user can see their history
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
        } else
        {
            int option = JOptionPane.showConfirmDialog(null,"Attempting to delete "+myTable.getValueAt(myTable.getSelectedRow(),0)+ " would you like to continue?", "choose one", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION)
            {
                try
                {
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
                    adminMenu.getItem(0).doClick();// should click on DISPLAY_MEDIA button
                }
                catch (SQLException sqle)
                {
                    System.out.println("SQLException in StoreFrame actionPerformed");
                    sqle.printStackTrace();
                }
            }
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
            columnNames = new Vector<Object>();
            currentRow  = new Vector<Object>();
            rowList     = new Vector<Object>();
            resultMetaData = resultSet.getMetaData();

            for (int i=0; i < resultMetaData.getColumnCount(); i++)
                {
                columnNames.addElement(resultMetaData.getColumnLabel(i+1));// USING getColumnLabel() INSTEAD OF getColumnName() ALLOWS FOR ALIASING!
                }

            do
                {
                currentRow = new Vector<Object>();
                for (int j=0; j < resultMetaData.getColumnCount(); j++)
                    {
                    currentRow.addElement(resultSet.getObject(j+1));
                    }
                rowList.addElement(currentRow);
                }
            while (resultSet.next());

            myTable = new JTable(rowList, columnNames);
            myScrollPane = new JScrollPane(myTable);
            myScrollPane.setPreferredSize(new Dimension(500, 400));
            myTable.addMouseListener(this);
            scrollPanel.add(myScrollPane);
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
    //        cnfe.printStackTrace();
        }
        catch (SQLException sqle)
        {
            System.out.println("SQLException in LoginJDialog establishConnection");
            JOptionPane.showMessageDialog(null, "Failed to create a connection with the database.", "Failed to connect", JOptionPane.ERROR_MESSAGE);
    //        sqle.printStackTrace();
        }
    }
    //=====================================================
    public void setUserInfo(boolean isAdmin, String username)
    {
    this.isAdmin = isAdmin;
    this.username = username;
    loggedIn = true;

    if(isAdmin)
        adminMenu.setEnabled(true);

    logInMenu.setText("Logout");
    logInMenu.getItem(0).setText("Logout");  //getting the JMenuItem
    this.setTitle("Books-R-Us" + " - Signed in as: " + username);
    this.repaint();     //causes the whole frame to repaint with the update to its components
    }
    //=====================================================
        //=====================================================
    public void mouseClicked(MouseEvent e)
    {
    System.out.println("StoreFrame mouseClicked");

    if (e.getButton() == MouseEvent.BUTTON3)
        {

        mousePoint = e.getPoint();
        selectedRow = myTable.rowAtPoint(mousePoint);
        System.out.println(selectedRow + " SELECTED");

        myTable.changeSelection(selectedRow, 0, false, false);
        rightClickMenu.setVisible(true);
        }
    }

    //=====================================================
    public void mousePressed(MouseEvent e)
    {
    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	rightClickMenu.setVisible(false);// to get the menu to go away whe you click somewhere else
    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    }
    //=====================================================
    public void mouseReleased(MouseEvent e)
    {
    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	setupPopMenu();//to show the popup when you release the mouse button
    //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
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
    public Connection copyConnection()
    {
        return connection;
    }
//=====================================================
}
//#########################################################
