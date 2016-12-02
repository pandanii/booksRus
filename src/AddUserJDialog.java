//BooksRUs Software

import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;


//#########################################################
public class AddUserJDialog extends JDialog
                            implements ActionListener,
                                       DocumentListener
{

//INSERT INTO User(userID,password,phone_number,address,email,name,is_admin) VALUES (?,?,?,?,?,?,?);

StoreFrame pointerToStoreFrame;

JTextField userIDField; //not null
JTextField passwordField;   //not null
JTextField phoneNumberField;
JTextField addressField;    //not null
JTextField emailField;  //not null
JTextField nameField;   //not null
JComboBox isAdminComboBox;

JButton createUserButton;

    //=====================================================
    public AddUserJDialog (StoreFrame pointerToStoreFrame)
    {
    this.pointerToStoreFrame = pointerToStoreFrame;

    Container cp;
    Toolkit tk;
    Dimension d;

    JButton cancelButton;

    JLabel userIDLabel;
    JLabel passwordLabel;
    JLabel phoneNumberLabel;
    JLabel addressLabel;
    JLabel emailLabel;
    JLabel nameLabel;
    JLabel isAdminLabel;

    GroupLayout groupLayout;

    JPanel mainPanel;
    JPanel buttonPanel;
    JPanel fieldPanel;

    userIDLabel = new JLabel("User ID");
    passwordLabel = new JLabel("Password");
    phoneNumberLabel = new JLabel("Phone Number");
    addressLabel = new JLabel("Address");
    emailLabel = new JLabel("Email");
    nameLabel = new JLabel("Name");
    isAdminLabel = new JLabel("Admin Status");

    userIDField = new JTextField(30);
    userIDField.getDocument().addDocumentListener(this);

    passwordField = new JTextField(30);
    passwordField.getDocument().addDocumentListener(this);

    phoneNumberField = new JTextField(30);

    addressField = new JTextField(30);
    addressField.getDocument().addDocumentListener(this);

    emailField = new JTextField(30);
    emailField.getDocument().addDocumentListener(this);

    nameField = new JTextField(30);
    nameField.getDocument().addDocumentListener(this);

    isAdminComboBox = new JComboBox();
    isAdminComboBox.addItem("Not Admin");
    isAdminComboBox.addItem("Admin");

    createUserButton = new JButton("Create User");
    createUserButton.setActionCommand("CREATE");
    createUserButton.addActionListener(this);
    createUserButton.setToolTipText("Create a new user with the information in the fields.");
    createUserButton.setEnabled(false);

    cancelButton = new JButton("Cancel");
    cancelButton.setActionCommand("CANCEL");
    cancelButton.addActionListener(this);
    cancelButton.setToolTipText("Close the Add User window.");

    buttonPanel = new JPanel(new GridLayout(2,1));
    buttonPanel.add(createUserButton);
    buttonPanel.add(cancelButton);

    fieldPanel = new JPanel();
    groupLayout = new GroupLayout(fieldPanel);
    fieldPanel.setLayout(groupLayout);

    groupLayout.setAutoCreateGaps(true);

    groupLayout.setAutoCreateContainerGaps(true);

    GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();

    hGroup.addGroup(groupLayout.createParallelGroup().addComponent(userIDField).addComponent(passwordField).addComponent(phoneNumberField).addComponent(addressField).addComponent(emailField).addComponent(nameField).addComponent(isAdminComboBox));

    hGroup.addGroup(groupLayout.createParallelGroup().addComponent(userIDLabel).addComponent(passwordLabel).addComponent(phoneNumberLabel).addComponent(addressLabel).addComponent(emailLabel).addComponent(nameLabel).addComponent(isAdminLabel));

    groupLayout.setHorizontalGroup(hGroup);

    GroupLayout.SequentialGroup vGroup = groupLayout.createSequentialGroup();

    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(userIDLabel).addComponent(userIDField));

    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(passwordLabel).addComponent(passwordField));

    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(phoneNumberLabel).addComponent(phoneNumberField));

    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(addressLabel).addComponent(addressField));

    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(emailLabel).addComponent(emailField));

    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(nameLabel).addComponent(nameField));

    vGroup.addGroup(groupLayout.createParallelGroup().addComponent(isAdminLabel).addComponent(isAdminComboBox));

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

    setTitle("Add New User");

    setVisible(true);


    }
    //=====================================================
    public void actionPerformed(ActionEvent e)
    {

    if (e.getActionCommand().equals("CLOSE"))
        {
        dispose();
        }
    else if (e.getActionCommand().equals("CREATE"))
        {
        String userID;
        String password;
        String phoneNumber;
        String address;
        String email;
        String name;
        int isAdmin;

        Object comboObject;

        comboObject = isAdminComboBox.getSelectedItem();

        userID = userIDField.getText().trim();
        password = passwordField.getText().trim();
        phoneNumber = phoneNumberField.getText().trim();
        address = addressField.getText().trim();
        email = emailField.getText().trim();
        name = nameField.getText().trim();

        isAdmin = 0;
        if (comboObject.toString().equals("Not Admin"))
            {
            isAdmin = 0;
            }
        else if (comboObject.toString().equals("Admin"))
            {
            isAdmin = 1;
            }

        pointerToStoreFrame.createNewUser(userID, password, phoneNumber, address, email, name, isAdmin);
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
    if (userIDField.getText().trim().equals("") || passwordField.getText().trim().equals("") || addressField.getText().trim().equals("") || emailField.getText().trim().equals("") || nameField.getText().trim().equals(""))
        {
        createUserButton.setEnabled(false);
        }
    else
        {
        createUserButton.setEnabled(true);
        }
    }
    //=====================================================
    public void insertUpdate(DocumentEvent e)
    {
    if (userIDField.getText().trim().equals("") || passwordField.getText().trim().equals("") || addressField.getText().trim().equals("") || emailField.getText().trim().equals("") || nameField.getText().trim().equals(""))
        {
        createUserButton.setEnabled(false);
        }
    else
        {
        createUserButton.setEnabled(true);
        }
    }
    //=====================================================



}
//#########################################################