//BooksRUs Software
//Version 1.0

import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

//#########################################################
public class LoginJDialog extends JDialog implements ActionListener,DocumentListener
{
StoreFrame pointerToStoreFrame;

Connection connection;

JButton loginButton;
JButton cancelButton;

JLabel usernameLabel;
JLabel passwordLabel;

JTextField usernameTextField;
JPasswordField passwordTextField;

JPanel mainPanel;
JPanel buttonPanel;
JPanel labelTextFieldPanel;

static final String JDBC_DRIVER  = "com.mysql.jdbc.Driver";
static final String DATABASE_URL = "jdbc:mysql://localhost:3306/movies&books"/*"jdbc:mysql://falcon-cs.fairmontstate.edu/DB00";SWAP THESE FOR SCHOOL EDITING.*/;
static final String USERNAME     = "root";
static final String PASSWORD     = "admin";  //Adust this according to local host login or server login

//=====================================================
public LoginJDialog(StoreFrame pointerToStoreFrame)
{
    this.pointerToStoreFrame = pointerToStoreFrame;     //so a method of StoreFrame can be called later.
    
    connection = establishConnection();
    if(connection != null)
    {
        pointerToStoreFrame.passConnection(connection);     //passing the connection established to the StoreFrame
    }
    Container cp;
    Toolkit   tk;
    Dimension d;

    GroupLayout groupLayout;

    loginButton = new JButton("Login");
    loginButton.setActionCommand("LOGIN");
    loginButton.addActionListener(this);
    loginButton.setToolTipText("Send your credentials in the fields above to log in to your account.");
    loginButton.setEnabled(false);

    cancelButton = new JButton("Cancel");
    cancelButton.setActionCommand("CANCEL");
    cancelButton.addActionListener(this);
    cancelButton.setToolTipText("Close the login panel.");

    buttonPanel = new JPanel();
    buttonPanel.add(loginButton);
    buttonPanel.add(cancelButton);

    usernameLabel = new JLabel("Username: ");
    passwordLabel = new JLabel("Password: ");

    usernameTextField = new JTextField(30);
    usernameTextField.getDocument().addDocumentListener(this);

    passwordTextField = new JPasswordField(30);
    passwordTextField.getDocument().addDocumentListener(this);

    labelTextFieldPanel = new JPanel();
    groupLayout = new GroupLayout(labelTextFieldPanel);
    labelTextFieldPanel.setLayout(groupLayout);

    groupLayout.setAutoCreateGaps(true);

    groupLayout.setAutoCreateContainerGaps(true);

    GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();

    hGroup.addGroup(groupLayout.createParallelGroup().addComponent(usernameLabel).addComponent(passwordLabel));
    hGroup.addGroup(groupLayout.createParallelGroup().addComponent(usernameTextField).addComponent(passwordTextField));

    groupLayout.setHorizontalGroup(hGroup);

    GroupLayout.SequentialGroup vGroup = groupLayout.createSequentialGroup();

    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(usernameLabel).addComponent(usernameTextField));
    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(passwordLabel).addComponent(passwordTextField));

    groupLayout.setVerticalGroup(vGroup);

    mainPanel = new JPanel();
    mainPanel.add(labelTextFieldPanel);
    mainPanel.add(buttonPanel);

    cp = getContentPane();
    cp.add(mainPanel, BorderLayout.CENTER);

    setTitle("Login Panel");
    setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

    tk = Toolkit.getDefaultToolkit();
    d = tk.getScreenSize();
    setSize(d.width/4, d.height/8);
    setLocation(d.width/3, d.height/3);

    d.setSize(450, 180);
    setMinimumSize(d);

    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    getRootPane().setDefaultButton(loginButton);
    usernameTextField.requestFocus();

    setVisible(true);
}

//=====================================================
public void actionPerformed(ActionEvent e)
{
    //Connection connection;
    if (e.getActionCommand().equals("CANCEL"))
    {
        dispose();
    }
    else if (e.getActionCommand().equals("LOGIN"))
    {
        // I think this should just call the query for the database to retrieve the login info for the user.
        
        /*
        //do login stuff.
        System.out.println("Trying to establish a connection");
        connection = getConnection();   //local method using input of fields to connect to the database
        if (connection != null)
        {
            pointerToStoreFrame.passConnection(connection);     //passing the connection established to the StoreFrame
        }*/
        dispose();
    }
}
//=====================================================
public Connection establishConnection() 
{
    try
    {
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
    }
    catch (ClassNotFoundException cnfe)
    {
        System.out.println("ClassNotFoundException in LoginJDialog establishConnection");
        JOptionPane.showMessageDialog(null, "Failed to load driver.", "Failed to connect", JOptionPane.ERROR_MESSAGE);
//        cnfe.printStackTrace();
        return null;
    }
    catch (SQLException sqle)
    {
        System.out.println("SQLException in LoginJDialog establishConnection");
        JOptionPane.showMessageDialog(null, "Bad Credentials.", "Failed to connect", JOptionPane.ERROR_MESSAGE);
//        sqle.printStackTrace();
        return null;
    }
    
}
//=====================================================
public Connection getConnection()
{
// I think the connection should be established before we actually login. The login would be something seperate. Otherwise we would have to add a lot of other accounts to the DB
    /*Connection connection;
    String username;
    char[] password;
    try
    {
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(DATABASE_URL, usernameTextField.getText().trim(), new String(passwordTextField.getPassword()).trim());
        return connection;
        //return DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
    }
    catch (ClassNotFoundException cnfe)
    {
        System.out.println("ClassNotFoundException in LoginJDialog getConnection");
        JOptionPane.showMessageDialog(null, "Failed to load driver.", "Failed to connect", JOptionPane.ERROR_MESSAGE);
//        cnfe.printStackTrace();
        return null;
    }
    catch (SQLException sqle)
    {
        System.out.println("SQLException in LoginJDialog getConnection");
        JOptionPane.showMessageDialog(null, "Bad Credentials.", "Failed to connect", JOptionPane.ERROR_MESSAGE);
//        sqle.printStackTrace();
        return null;
    }*/
    return null; // NEED TO TAKE OUT IF USING
}
//=====================================================
public void changedUpdate(DocumentEvent e)
{
    //do nothing
}
//=====================================================
public void removeUpdate(DocumentEvent e)
{
    if (usernameTextField.getText().trim().equals("") || passwordTextField.getText().trim().equals(""))
    {
        loginButton.setEnabled(false);
    }
    else
    {
        loginButton.setEnabled(true);
    }
}
//=====================================================
public void insertUpdate(DocumentEvent e)
{
    if (usernameTextField.getText().trim().equals("") || passwordTextField.getText().trim().equals(""))
    {
        loginButton.setEnabled(false);
    }
    else
    {
        loginButton.setEnabled(true);
    }
}
//=====================================================
}
//#########################################################