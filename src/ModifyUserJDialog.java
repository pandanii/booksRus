//BooksRUs Software

import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;


//#########################################################
public class ModifyUserJDialog extends JDialog
                            implements ActionListener,
                                       DocumentListener
{

StoreFrame pointerToStoreFrame;

JTextField passwordField;   //not null
JTextField phoneNumberField;
JTextField addressField;    //not null
JTextField emailField;  //not null
JTextField nameField;   //not null

String userID;
String password;
String phoneNumber;
String address;
String email;
String name;
boolean isAdmin;

JButton modifyUserButton;

    //=====================================================
    public ModifyUserJDialog (StoreFrame pointerToStoreFrame)
    {
    this.pointerToStoreFrame = pointerToStoreFrame;
    this.userID = pointerToStoreFrame.userID;
    this.password = pointerToStoreFrame.password;
    this.phoneNumber = pointerToStoreFrame.phoneNumber;
    this.address = pointerToStoreFrame.address;
    this.email = pointerToStoreFrame.email;
    this.name = pointerToStoreFrame.name;
    this.isAdmin = pointerToStoreFrame.isAdmin;

    Container cp;
    Toolkit tk;
    Dimension d;

    JButton cancelButton;

    JLabel passwordLabel;
    JLabel phoneNumberLabel;
    JLabel addressLabel;
    JLabel emailLabel;
    JLabel nameLabel;

    GroupLayout groupLayout;

    JPanel mainPanel;
    JPanel buttonPanel;
    JPanel fieldPanel;

    passwordLabel = new JLabel("Password");
    phoneNumberLabel = new JLabel("Phone Number");
    addressLabel = new JLabel("Address");
    emailLabel = new JLabel("Email");
    nameLabel = new JLabel("Name");


    passwordField = new JTextField(30);
    passwordField.getDocument().addDocumentListener(this);

    phoneNumberField = new JTextField(30);

    addressField = new JTextField(30);
    addressField.getDocument().addDocumentListener(this);

    emailField = new JTextField(30);
    emailField.getDocument().addDocumentListener(this);

    nameField = new JTextField(30);
    nameField.getDocument().addDocumentListener(this);


    modifyUserButton = new JButton("Modify User");
    modifyUserButton.setActionCommand("MODIFY");
    modifyUserButton.addActionListener(this);
    modifyUserButton.setToolTipText("Modify your user account with the information in the fields.");
    modifyUserButton.setEnabled(false);

    cancelButton = new JButton("Cancel");
    cancelButton.setActionCommand("CANCEL");
    cancelButton.addActionListener(this);
    cancelButton.setToolTipText("Close the Modify User window.");

    buttonPanel = new JPanel(new GridLayout(2,1));
    buttonPanel.add(modifyUserButton);
    buttonPanel.add(cancelButton);

    fieldPanel = new JPanel();
    groupLayout = new GroupLayout(fieldPanel);
    fieldPanel.setLayout(groupLayout);

    groupLayout.setAutoCreateGaps(true);

    groupLayout.setAutoCreateContainerGaps(true);

    GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();

    hGroup.addGroup(groupLayout.createParallelGroup().addComponent(passwordLabel).addComponent(phoneNumberLabel).addComponent(addressLabel).addComponent(emailLabel).addComponent(nameLabel));

    hGroup.addGroup(groupLayout.createParallelGroup().addComponent(passwordField).addComponent(phoneNumberField).addComponent(addressField).addComponent(emailField).addComponent(nameField));

    groupLayout.setHorizontalGroup(hGroup);

    GroupLayout.SequentialGroup vGroup = groupLayout.createSequentialGroup();

    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(passwordLabel).addComponent(passwordField));

    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(phoneNumberLabel).addComponent(phoneNumberField));

    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(addressLabel).addComponent(addressField));

    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(emailLabel).addComponent(emailField));

    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(nameLabel).addComponent(nameField));

    groupLayout.setVerticalGroup(vGroup);


    mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(fieldPanel, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    cp = getContentPane();
    cp.add(mainPanel, BorderLayout.CENTER);


    tk = Toolkit.getDefaultToolkit();
    d = tk.getScreenSize();
    setSize(d.width/4, d.height/6);
    setLocation(d.width/3, d.height/3);

    d.setSize(400, 300);
    setMinimumSize(d);
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

    setTitle("Modify User");

    passwordField.setText(password);
    phoneNumberField.setText(phoneNumber);
    addressField.setText(address);
    emailField.setText(email);
    nameField.setText(name);

    setVisible(true);

    }
    //=====================================================
    public void actionPerformed(ActionEvent e)
    {

    if (e.getActionCommand().equals("CANCEL"))
        {
        dispose();
        }
    else if (e.getActionCommand().equals("MODIFY"))
        {

        password = passwordField.getText().trim();
        phoneNumber = phoneNumberField.getText().trim();
        address = addressField.getText().trim();
        email = emailField.getText().trim();
        name = nameField.getText().trim();

        pointerToStoreFrame.modifyUserInfo(userID, password, phoneNumber, address, email, name, isAdmin);

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
    if (passwordField.getText().trim().equals("") || addressField.getText().trim().equals("") || emailField.getText().trim().equals("") || nameField.getText().trim().equals(""))
        {
        modifyUserButton.setEnabled(false);
        }
    else
        {
        modifyUserButton.setEnabled(true);
        }
    }
    //=====================================================
    public void insertUpdate(DocumentEvent e)
    {
    if (passwordField.getText().trim().equals("") || addressField.getText().trim().equals("") || emailField.getText().trim().equals("") || nameField.getText().trim().equals(""))
        {
        modifyUserButton.setEnabled(false);
        }
    else
        {
        modifyUserButton.setEnabled(true);
        }
    }
    //=====================================================



}
//#########################################################