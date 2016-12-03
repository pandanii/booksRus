//BooksRUs Software

import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;


//#########################################################
public class SearchJDialog extends JDialog
                           implements ActionListener,
                                      DocumentListener
{

Queries listOfQueries;

StoreFrame pointerToStoreFrame;

JButton searchButton;
JTextField searchTextField;
JComboBox searchComboBox;

boolean isAdmin;
boolean loggedIn;

    //=====================================================
    public SearchJDialog(StoreFrame pointerToStoreFrame)
    {
    this.pointerToStoreFrame = pointerToStoreFrame;
    this.isAdmin = pointerToStoreFrame.isAdmin;
    this.loggedIn = pointerToStoreFrame.loggedIn;

    Container cp;
    Toolkit tk;
    Dimension d;

    listOfQueries = new Queries();

    JButton cancelButton;

    JLabel searchComboBoxLabel;
    JLabel searchTextFieldLabel;

    JPanel mainPanel;
    JPanel searchComboBoxAndLabelPanel;
    JPanel searchTextFieldAndLabelPanel;
    JPanel buttonPanel;

    searchButton = new JButton("Search");
    searchButton.setActionCommand("SEARCH");
    searchButton.addActionListener(this);
    searchButton.setToolTipText("Begin the search.");
    //searchButton.setEnabled(false);

    cancelButton = new JButton("Cancel");
    cancelButton.setActionCommand("CANCEL");
    cancelButton.addActionListener(this);
    cancelButton.setToolTipText("Close the search window.");

    buttonPanel = new JPanel();
    buttonPanel.add(searchButton);
    buttonPanel.add(cancelButton);

    searchComboBoxLabel = new JLabel("Search By:");

    searchTextFieldLabel = new JLabel("Keywords:");

    searchComboBox = new JComboBox();
/*
    if (isAdmin)
        {
        searchComboBox.addItem("Admin Book Info");
        searchComboBox.addItem("Admin Last 24 hours");
        searchComboBox.addItem("Admin Top Ten");
        }
    if (loggedIn)
        {
        searchComboBox.addItem("Purchase History");
        }
*/
    searchComboBox.addItem("DVD Title");
    searchComboBox.addItem("Director Name");
    searchComboBox.addItem("Cast Member Name");
    searchComboBox.addItem("Genre");
    searchComboBox.addItem("Sequel");
    searchComboBox.addItem("DVD Keyword");
    searchComboBox.addItem("Book Title");
    searchComboBox.addItem("Author Name");
    searchComboBox.addItem("Publisher Name");
    searchComboBox.addItem("Book Category");
    searchComboBox.addItem("Book Keyword");
    searchComboBox.addActionListener(this);

    searchTextField = new JTextField(30);
    searchTextField.getDocument().addDocumentListener(this);

    searchComboBoxAndLabelPanel = new JPanel(new BorderLayout());
    searchComboBoxAndLabelPanel.add(searchComboBoxLabel, BorderLayout.CENTER);
    searchComboBoxAndLabelPanel.add(searchComboBox, BorderLayout.SOUTH);

    searchTextFieldAndLabelPanel = new JPanel(new BorderLayout());
    searchTextFieldAndLabelPanel.add(searchTextFieldLabel, BorderLayout.CENTER);
    searchTextFieldAndLabelPanel.add(searchTextField, BorderLayout.SOUTH);

    mainPanel = new JPanel();
    mainPanel.add(searchTextFieldAndLabelPanel);
    mainPanel.add(searchComboBoxAndLabelPanel);

    cp = getContentPane();
    cp.add(mainPanel, BorderLayout.CENTER);
    cp.add(buttonPanel, BorderLayout.SOUTH);

    setTitle("Search Panel");
    setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

    tk = Toolkit.getDefaultToolkit();
    d = tk.getScreenSize();
    setSize(d.width/4, d.height/8);
    setLocation(d.width/3, d.height/3);

    d.setSize(550, 100);
    setMinimumSize(d);

    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    searchComboBox.setSelectedIndex(0); //select the first Item in the combobox to determine text field status

    getRootPane().setDefaultButton(searchButton);
    searchTextField.requestFocus();

    setVisible(true);

    }
    //=====================================================
@SuppressWarnings("CallToPrintStackTrace")
@Override
    public void actionPerformed(ActionEvent e)
    {

    if (e.getActionCommand().equals("CANCEL"))
        {
        dispose();
        }
    else if (e.getActionCommand().equals("SEARCH"))
        {
        PreparedStatement preparedStatement;
        Object comboObject;
        String searchString;
        Connection connection;
        ResultSet resultSet;

        connection = pointerToStoreFrame.connection;

        searchString = searchTextField.getText().trim();    //get what the user wants to search for

        comboObject = searchComboBox.getSelectedItem();     //get what they want to search in from the combo box
                                                            //and relate it to the Queries data members
        resultSet = null;
        try
            {
            if (comboObject.toString().equals("Purchase History"))
                {
                System.out.println("comboObject.toString().equals(\"Purchase History\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.purchase_History);
                preparedStatement.clearParameters();
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                preparedStatement.setString(1, pointerToStoreFrame.userID);   //only the logged in user can see their history
                resultSet = preparedStatement.executeQuery();
                }
            else if (comboObject.toString().equals("Admin Book Info"))
                {
                System.out.println("comboObject.toString().equals(\"Admin Book Info\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.admin_Book_Info);
                preparedStatement.clearParameters();
                preparedStatement.setString(1, searchString);
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                resultSet = preparedStatement.executeQuery();
                }
            else if (comboObject.toString().equals("Admin Last 24 hours"))
                {
                System.out.println("comboObject.toString().equals(\"Admin Last 24 hours\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.admin_In_Last_24h);
                preparedStatement.clearParameters();
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
//                preparedStatement.setString(1, '%'+searchString+'%'); //not needed in this query
                resultSet = preparedStatement.executeQuery();
                }
            else if (comboObject.toString().equals("Admin Top Ten"))
                {
                System.out.println("comboObject.toString().equals(\"Admin Top Ten\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.admin_top_10);
                preparedStatement.clearParameters();
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
//                preparedStatement.setString(1, '%'+searchString+'%'); //not needed in this query
                resultSet = preparedStatement.executeQuery();
                }
            else if (comboObject.toString().equals("DVD Title"))
                {
                System.out.println("comboObject.toString().equals(\"DVD Title\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.displayResultsDVDs +listOfQueries.title_DVDs_Search +" );");
                preparedStatement.clearParameters();
                preparedStatement.setString(1, '%'+searchString+'%');
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                resultSet = preparedStatement.executeQuery();
                }
            else if (comboObject.toString().equals("Director Name"))
                {
                System.out.println("comboObject.toString().equals(\"Director Name\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.displayResultsDVDs + listOfQueries.director_Search+" );");
                preparedStatement.clearParameters();
                preparedStatement.setString(1, '%'+searchString+'%');
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                resultSet = preparedStatement.executeQuery();
                }
            else if (comboObject.toString().equals("Cast Member Name"))
                {
                System.out.println("comboObject.toString().equals(\"Cast Member Name\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.displayResultsDVDs + listOfQueries.cast_Search+" );");
                preparedStatement.clearParameters();
                preparedStatement.setString(1, '%'+searchString+'%');
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                resultSet = preparedStatement.executeQuery();
                }
            else if (comboObject.toString().equals("Genre"))
                {
                System.out.println("comboObject.toString().equals(\"Genre\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.displayResultsDVDs + listOfQueries.genre_Search+" );");
                preparedStatement.clearParameters();
                preparedStatement.setString(1, '%'+searchString+'%');
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                resultSet = preparedStatement.executeQuery();
                }
            else if (comboObject.toString().equals("DVD Keyword"))
                {
                System.out.println("comboObject.toString().equals(\"DVD Keyword\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.displayResultsDVDs + listOfQueries.keyword_DVDs_Search+" );");
                preparedStatement.clearParameters();
                preparedStatement.setString(1, '%'+searchString+'%');
                preparedStatement.setString(2, '%'+searchString+'%');
                preparedStatement.setString(3, '%'+searchString+'%');
                preparedStatement.setString(4, '%'+searchString+'%');
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                resultSet = preparedStatement.executeQuery();
                }
            else if (comboObject.toString().equals("Sequel"))
                {
                System.out.println("comboObject.toString().equals(\"Sequel\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.displayResultsDVDs + listOfQueries.sequel_Search+" );");
                preparedStatement.clearParameters();
                preparedStatement.setString(1, searchString);
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                resultSet = preparedStatement.executeQuery();
                }
            else if (comboObject.toString().equals("Book Title"))
                {
                System.out.println("comboObject.toString().equals(\"Book Title\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.displayResultsBooks + listOfQueries.title_Books_Search+" );");
                preparedStatement.clearParameters();
                preparedStatement.setString(1, '%'+searchString+'%');
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                resultSet = preparedStatement.executeQuery();
                }
            else if (comboObject.toString().equals("Author Name"))
                {
                System.out.println("comboObject.toString().equals(\"Author Name\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.displayResultsBooks + listOfQueries.author_Search+" );");
                preparedStatement.clearParameters();
                preparedStatement.setString(1, '%'+searchString+'%');
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                resultSet = preparedStatement.executeQuery();
                }
            else if (comboObject.toString().equals("Publisher Name"))
                {
                System.out.println("comboObject.toString().equals(\"Publisher Name\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.displayResultsBooks + listOfQueries.publisher_Search+" );");
                preparedStatement.clearParameters();
                preparedStatement.setString(1, '%'+searchString+'%');
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                resultSet = preparedStatement.executeQuery();
                }
            else if (comboObject.toString().equals("Book Category"))
                {
                System.out.println("comboObject.toString().equals(\"Book Category\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.displayResultsBooks + listOfQueries.subject_Cate_Search+" );");
                preparedStatement.clearParameters();
                preparedStatement.setString(1, '%'+searchString+'%');
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                resultSet = preparedStatement.executeQuery();
                }
            else if (comboObject.toString().equals("Book Keyword"))
                {
                System.out.println("comboObject.toString().equals(\"Book Keyword\")");//DEBUG
                preparedStatement = connection.prepareStatement(listOfQueries.displayResultsBooks + listOfQueries.keyword_Books_Search+" );");
                preparedStatement.clearParameters();
                preparedStatement.setString(1, '%'+searchString+'%');
                preparedStatement.setString(2, '%'+searchString+'%');
                preparedStatement.setString(3, '%'+searchString+'%');
                preparedStatement.setString(4, '%'+searchString+'%');
                System.out.println("ATTEMPTING TO CALL SQL QUERY: " + preparedStatement);
                resultSet = preparedStatement.executeQuery();
                }

            if (resultSet != null)
                {
                pointerToStoreFrame.updateResultTable(resultSet);   //sending the resultSet to StoreFrame to be displayed
                }
            }
        catch (SQLException sqle)
            {
            System.out.println("SQLException in SearchJDialog actionPerformed");
            sqle.printStackTrace();
            }
        dispose();
        }
    else
        {
        reactToComboChanges();  //if selected Combo Item doesn't allow use of text field, disable field
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
    /*if (searchTextField.getText().trim().equals(""))
        {
        searchButton.setEnabled(false);
        }
    else
        {
        searchButton.setEnabled(true);
        }*/
    }
    //=====================================================
@Override
    public void insertUpdate(DocumentEvent e)
    {
   /* if (searchTextField.getText().trim().equals(""))
        {
        searchButton.setEnabled(false);
        }
    else
        {
        searchButton.setEnabled(true);
        }*/
    }
    //=====================================================
    public void reactToComboChanges()
    {
//    System.out.println("reacting");
    String selectedItem;

    selectedItem = searchComboBox.getSelectedItem().toString();

    if (selectedItem.equals("Purchase History"))    //for any search option that cannot use the text field, disable it.
        {
        searchTextField.setText("");
        searchTextField.setEnabled(false);
        }
    else if (selectedItem.equals("Admin Last 24 hours"))
        {
        searchTextField.setText("");
        searchTextField.setEnabled(false);
        }
    else if (selectedItem.equals("Admin Top Ten"))
        {
        searchTextField.setText("");
        searchTextField.setEnabled(false);
        }
    else
        {
        searchTextField.setEnabled(true);
        searchTextField.requestFocus();
        }
    }
    //=====================================================


}
//#########################################################
