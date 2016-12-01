//BooksRUs Software


import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;


//#########################################################
public class StoreFrame extends JFrame implements ActionListener
{
JTable     myTable;
JPanel     scrollPanel;
Connection connection;
Queries    listOfQueries;

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
private JMenuBar createMenuBar()
{
    JMenuBar menuBar;
//    JMenu logInMenu; //made this have a larger scope so it can be used later to logout when user is logged in.
    JMenu viewMenu;
    JMenu searchMenu;

    JMenuItem viewMenuItem;
    JMenuItem loginMenuItem;
    JMenuItem searchMenuItem;
    JMenuItem     historyMenuItem;

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

    searchMenu = new JMenu("Search");

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
    else if (e.getActionCommand().equals("HISTORY"))
    {   
        PreparedStatement preparedStatement;
        ResultSet         resultSet = null;
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
    logInMenu.getItem(0).setText("Logout");  //getting the JMenuItem
    this.setTitle("Books-R-Us" + " - Signed in as: " + username);
    this.repaint();     //causes the whole frame to repaint with the update to its components
    }
    //=====================================================
}
//#########################################################