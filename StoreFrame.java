//BooksRUs Software
//Version 1.1
//
//
//

//=========================================================

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
                                   DocumentListener
{

JTable myTable;

JTextField queryTextField;
JButton submitButton;
JPanel scrollPanel;

Connection connection;

    //=====================================================
    public StoreFrame()
    {
    System.out.println("StoreFrame Constructor");

    Container cp;

    JPanel mainPanel;
    JPanel buttonPanel;
    JPanel queryPanel;
    JPanel inputPanel;

    JLabel queryLabel;

    JScrollPane myScrollPane;

    JButton closeButton;

    closeButton = new JButton("Close");
    closeButton.setActionCommand("CLOSE");
    closeButton.addActionListener(this);
    closeButton.setToolTipText("Close the program.");

    submitButton = new JButton("Submit");
    submitButton.setActionCommand("SUBMIT");
    submitButton.addActionListener(this);
    submitButton.setToolTipText("Submit the query in the text field.");
    submitButton.setEnabled(false);

    buttonPanel = new JPanel(new GridLayout(2,1));
    buttonPanel.add(submitButton);
    buttonPanel.add(closeButton);

    queryLabel = new JLabel("Query Input");

    queryTextField = new JTextField(30);
    queryTextField.getDocument().addDocumentListener(this);

    queryPanel = new JPanel(new GridLayout(2,1));
    queryPanel.add(queryLabel);
    queryPanel.add(queryTextField);

    inputPanel = new JPanel();
    inputPanel.add(queryPanel);
    inputPanel.add(buttonPanel);

    scrollPanel = new JPanel();

    mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(scrollPanel, BorderLayout.CENTER);
    mainPanel.add(inputPanel, BorderLayout.SOUTH);

    cp = getContentPane();
    cp.add(mainPanel, BorderLayout.CENTER);

    getRootPane().setDefaultButton(submitButton);

    setupMainFrame();
    queryTextField.requestFocus();
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

    menuBar = new JMenuBar();

    subMenu = new JMenu("LogIn");

    loginMenuItem = new JMenuItem("Log In" , KeyEvent.VK_L);
    loginMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
    loginMenuItem.getAccessibleContext().setAccessibleDescription("Log into the database.");
    loginMenuItem.setToolTipText("Log into the database.");
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

    return menuBar;
    }
    //=====================================================
    public void actionPerformed(ActionEvent e)
    {
//    System.out.println("StoreFrame ActionPerformed");


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
        //new JDialog to accept the login credentials
        //then try to establish a Connection with the database

        new LoginJDialog(this);     //sending the JDialog a pointer to this instance of
                                    //StoreFrame so it can all one of its methods
        if (connection == null)
            {
            System.out.println("Null connection");
            }
        else
            {
            System.out.println("Good connection");
            }

        }
    else if (e.getActionCommand().equals("SUBMIT"))
        {
        //get the text from the text field and try using it as a query?

        String query;
        Statement statement;
        ResultSet resultSet;
        ResultSetMetaData resultMetaData;

        Vector<Object> columnNames;
        Vector<Object> currentRow;
        Vector<Object> rowList;

		JScrollPane myScrollPane;

        if (connection != null)
            {
            System.out.println("Query submitted");
            //execute query

            query = queryTextField.getText().trim();

            try
                {
                statement = connection.createStatement();

                resultSet = statement.executeQuery(query);


                if (!resultSet.first())
                    {
                    System.out.println("No records");
                    JOptionPane.showMessageDialog(null, "No results found.", "No results", JOptionPane.ERROR_MESSAGE);
                    }
                else
                    {

					columnNames = new Vector<Object>();
					currentRow = new Vector<Object>();
					rowList = new Vector<Object>();

                    resultMetaData = resultSet.getMetaData();


                    for (int i=0; i < resultMetaData.getColumnCount(); i++)
                        {
						columnNames.addElement(resultMetaData.getColumnName(i+1));
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
                statement.close();

                }
            catch (SQLException sqle2)
                {
                System.out.println("SQLException2 in StoreFrame actionPerformed");

                JOptionPane.showMessageDialog(null, "Query Error.", "Query Error", JOptionPane.ERROR_MESSAGE);
                sqle2.printStackTrace();
                }

            }
        else
            {
            System.out.println("No connection");
            JOptionPane.showMessageDialog(null, "Must be connected to the database first.", "No Connection", JOptionPane.ERROR_MESSAGE);
            }


        queryTextField.setText("");
        queryTextField.requestFocus();
        }


    }
    //=====================================================
    public void changedUpdate(DocumentEvent e)
    {
    //do nothing
    }
    //=====================================================
    public void removeUpdate(DocumentEvent e)
    {

    if (queryTextField.getText().trim().equals(""))
        {
        submitButton.setEnabled(false);
        }
    else
        {
        submitButton.setEnabled(true);
        }

    }
    //=====================================================
    public void insertUpdate(DocumentEvent e)
    {

    if (queryTextField.getText().trim().equals(""))
        {
        submitButton.setEnabled(false);
        }
    else
        {
        submitButton.setEnabled(true);
        }

    }
    //=====================================================
    public void passConnection(Connection connection)
    {
    this.connection = connection;

    System.out.println("Retrieved a connection.");

    }
    //=====================================================
















}
//#########################################################