//BooksRUs Software

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

JLabel userIDLabel;
JLabel passwordLabel;

JTextField userIDTextField;
JPasswordField passwordTextField;

JPanel mainPanel;
JPanel buttonPanel;
JPanel labelTextFieldPanel;

//=====================================================
public LoginJDialog(StoreFrame pointerToStoreFrame)
{
    this.pointerToStoreFrame = pointerToStoreFrame;     //so a method of StoreFrame can be called later.
    this.connection = pointerToStoreFrame.connection;

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

    userIDLabel = new JLabel("User ID: ");
    passwordLabel = new JLabel("Password: ");

    userIDTextField = new JTextField(30);
    userIDTextField.getDocument().addDocumentListener(this);

    passwordTextField = new JPasswordField(30);
    passwordTextField.getDocument().addDocumentListener(this);

    labelTextFieldPanel = new JPanel();
    groupLayout = new GroupLayout(labelTextFieldPanel);
    labelTextFieldPanel.setLayout(groupLayout);

    groupLayout.setAutoCreateGaps(true);

    groupLayout.setAutoCreateContainerGaps(true);

    GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();

    hGroup.addGroup(groupLayout.createParallelGroup().addComponent(userIDLabel).addComponent(passwordLabel));
    hGroup.addGroup(groupLayout.createParallelGroup().addComponent(userIDTextField).addComponent(passwordTextField));

    groupLayout.setHorizontalGroup(hGroup);

    GroupLayout.SequentialGroup vGroup = groupLayout.createSequentialGroup();

    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(userIDLabel).addComponent(userIDTextField));
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
    userIDTextField.requestFocus();

    setVisible(true);

}

//=====================================================
@Override
public void actionPerformed(ActionEvent e)
{
    //Connection connection;
    if (e.getActionCommand().equals("LOGIN"))
    {
        String userID = userIDTextField.getText().trim();
        String password = new String(passwordTextField.getPassword()).trim();
        String phoneNumber;
        String address;
        String email;
        String name;

        phoneNumber = "";
        address = "";
        email = "";
        name = "";

        try
        {
            ResultSetMetaData metaData;
            PreparedStatement preparedStatement;
            ResultSet resultSet;


            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE users.userID = ? AND users.password = ?;");
            preparedStatement.clearParameters();
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, password);
            System.out.println("preparedStatement: "+preparedStatement);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next())//Couldn't login or find info
            {
                JOptionPane.showMessageDialog(null, "Login unsucessful, try again!");
            }
            else// did login, will display the login data
            {
                metaData = resultSet.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); ++i)
                {
                    System.out.println(metaData.getColumnLabel(i) + ": " + resultSet.getObject(i));

                    if (metaData.getColumnLabel(i).equals("userID"))
                        {
                        userID = resultSet.getObject(i).toString();
                        }
                    else if (metaData.getColumnLabel(i).equals("password"))
                        {
                        password = resultSet.getObject(i).toString();
                        }
                    else if (metaData.getColumnLabel(i).equals("phone_number"))
                        {
                        phoneNumber = resultSet.getObject(i).toString();
                        }
                    else if (metaData.getColumnLabel(i).equals("address"))
                        {
                        address = resultSet.getObject(i).toString();
                        }
                    else if (metaData.getColumnLabel(i).equals("email"))
                        {
                        email = resultSet.getObject(i).toString();
                        }
                    else if (metaData.getColumnLabel(i).equals("name"))
                        {
                        name = resultSet.getObject(i).toString();
                        }
                }

                // removed second query. No need to requery.
                if (resultSet.getInt(7) == 1)
                    {
                    System.out.println("Admin loged in");
                    pointerToStoreFrame.setUserInfo(userID, password, phoneNumber, address, email, name, true);
                    JOptionPane.showMessageDialog(null, "Welcome Admin " + userID + ".");
                    this.dispose();
                    }
                else
                    {
                    System.out.println("NONE Admin loged in");
                    pointerToStoreFrame.setUserInfo(userID, password, phoneNumber, address, email, name, false);
                    JOptionPane.showMessageDialog(null, "Welcome " + userID + ".");
                    this.dispose();
                    }
            }
        }
        catch (SQLException sqle)
        {
            System.out.println("SQLException in LoginJDialog actionPerformed");
            JOptionPane.showMessageDialog(null, "Bad Query.", "Failed to query", JOptionPane.ERROR_MESSAGE);
            sqle.printStackTrace();
        }

    }
    else if (e.getActionCommand().equals("CANCEL"))
    {
        dispose();
    }
}
//=====================================================
@Override
public void changedUpdate(DocumentEvent e)
{
    //do nothing
}
//=====================================================
@Override
public void removeUpdate(DocumentEvent e)
{
    if (userIDTextField.getText().trim().equals("") || passwordTextField.getText().trim().equals(""))
    {
        loginButton.setEnabled(false);
    }
    else
    {
        loginButton.setEnabled(true);
    }
}
//=====================================================
@Override
public void insertUpdate(DocumentEvent e)
{
    if (userIDTextField.getText().trim().equals("") || passwordTextField.getText().trim().equals(""))
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