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

boolean isAdmin;
String username;

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
    JMenu subMenu;

    JMenuItem viewMenuItem;
    JMenuItem loginMenuItem;
    JMenuItem searchMenuItem;

    menuBar = new JMenuBar();

    subMenu = new JMenu("LogIn");

    loginMenuItem = new JMenuItem("Log In" , KeyEvent.VK_L);
    loginMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
    loginMenuItem.getAccessibleContext().setAccessibleDescription("Log into your Books-R-Us account.");
    loginMenuItem.setToolTipText("Log into your Books-R-Us account.");
    loginMenuItem.setActionCommand("LOGIN");
    loginMenuItem.addActionListener(this);

    subMenu.add(loginMenuItem);
    menuBar.add(subMenu);

    subMenu = new JMenu("View");

    viewMenuItem = new JMenuItem("View Options" , KeyEvent.VK_V);
    viewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
    viewMenuItem.getAccessibleContext().setAccessibleDescription("Change the appearance.");
    viewMenuItem.setToolTipText("Change the appearance of the window.");
    viewMenuItem.setActionCommand("VIEW");
    viewMenuItem.addActionListener(this);

    subMenu.add(viewMenuItem);
    menuBar.add(subMenu);

    subMenu = new JMenu("Search");

    searchMenuItem = new JMenuItem("Search Options" , KeyEvent.VK_S);
    searchMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
    searchMenuItem.getAccessibleContext().setAccessibleDescription("Search the store.");
    searchMenuItem.setToolTipText("Search the store.");
    searchMenuItem.setActionCommand("SEARCH");
    searchMenuItem.addActionListener(this);

    subMenu.add(searchMenuItem);
    menuBar.add(subMenu);

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
    else if (e.getActionCommand().equals("SEARCH"))
    {
        //get the text from the text field and try using it as a query?

    ResultSet resultSet;
    ResultSetMetaData metaData;

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
        sqle2.printStackTrace();
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
    }
    //=====================================================
}
//#########################################################