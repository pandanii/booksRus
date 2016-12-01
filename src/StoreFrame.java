//BooksRUs Software


import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;


//#########################################################
public class StoreFrame extends JFrame
                        implements ActionListener,
                                   MouseListener
{
JTable     myTable;
JPanel     scrollPanel;
Connection connection;

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
static final String PASSWORD     = "admin";  //Adust this according to local host login or server login

    //=====================================================
    public StoreFrame()
    {
    System.out.println("StoreFrame Constructor");
    Container cp;

    isAdmin = false;
    loggedIn = false;

    JPanel mainPanel;
    JPanel buttonPanel;

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
    JMenuItem searchMenuItem;
    JMenuItem cartMenuItem;

    menuBar = new JMenuBar();

    logInMenu = new JMenu("LogIn");

    loginMenuItem = new JMenuItem("Log In" , KeyEvent.VK_L);
    loginMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
    loginMenuItem.getAccessibleContext().setAccessibleDescription("Log into your Books-R-Us account.");
    loginMenuItem.setToolTipText("Log into your Books-R-Us account.");
    loginMenuItem.setActionCommand("LOGIN");
    loginMenuItem.addActionListener(this);

    logInMenu.add(loginMenuItem);
    menuBar.add(logInMenu);

    viewMenu = new JMenu("View");

    viewMenuItem = new JMenuItem("View Options" , KeyEvent.VK_V);
    viewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
    viewMenuItem.getAccessibleContext().setAccessibleDescription("Change the appearance.");
    viewMenuItem.setToolTipText("Change the appearance of the window.");
    viewMenuItem.setActionCommand("VIEW");
    viewMenuItem.addActionListener(this);

    viewMenu.add(viewMenuItem);
    menuBar.add(viewMenu);

    cartMenu = new JMenu("Shopping Cart");

    cartMenuItem = new JMenuItem("Open Shopping Cart" , KeyEvent.VK_C);
    cartMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
    cartMenuItem.getAccessibleContext().setAccessibleDescription("View your shopping cart.");
    cartMenuItem.setToolTipText("View your shopping cart.");
    cartMenuItem.setActionCommand("OPENCART");
    cartMenuItem.addActionListener(this);

    cartMenu.add(cartMenuItem);
    menuBar.add(cartMenu);

    searchMenu = new JMenu("Search");

    searchMenuItem = new JMenuItem("Search Options" , KeyEvent.VK_S);
    searchMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
    searchMenuItem.getAccessibleContext().setAccessibleDescription("Search the store.");
    searchMenuItem.setToolTipText("Search the store.");
    searchMenuItem.setActionCommand("SEARCH");
    searchMenuItem.addActionListener(this);

    searchMenu.add(searchMenuItem);
    menuBar.add(searchMenu);

    return menuBar;
    }
    //=====================================================
    @Override
    public void actionPerformed(ActionEvent e)
    {
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
                System.out.println("No connection to database.");
                JOptionPane.showMessageDialog(null, "Not connected.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        else
            {
            //change the menu item back to Login
            logInMenu.setText("Login");
            logInMenu.getItem(0).setText("Log In");  //getting the JMenuItem
            isAdmin = false;
            loggedIn = false;
            username = null;
            this.setTitle("Books-R-Us");
            this.repaint(); //causes the whole frame to repaint with the update to its components
            }

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
    else if (e.getActionCommand().equals("ADDTOCART"))
        {
        int currentColumnIndex;
        int numberInStockColumnIndex;
        Vector<Object> cartRowList; //only ever size one in this setup
        Vector<Object> currentRow;

        currentRow = new Vector<Object>();
        cartRowList = new Vector<Object>();

        numberInStockColumnIndex = 0;
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
    }
    //=====================================================
    public void updateResultTable(ResultSet resultSet)
    {

    ResultSetMetaData resultMetaData;


//    Vector<Object> columnNames;   //declared above for use elsewhere
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
    logInMenu.setText("Logout");
    logInMenu.getItem(0).setText("Log Out");  //getting the JMenuItem
    this.setTitle("Books-R-Us" + " - Signed in as: " + username);
    this.repaint();     //causes the whole frame to repaint with the update to its components
    }
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
    public Connection copyConnection()
    {
    return connection;
    }
    //=====================================================
}
//#########################################################