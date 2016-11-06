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

    //=====================================================
    public SearchJDialog(StoreFrame pointerToStoreFrame)
    {
    this.pointerToStoreFrame = pointerToStoreFrame;

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
    searchButton.setEnabled(false);

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
    searchComboBox.addItem("DVD Title");
    searchComboBox.addItem("Director Name");
    searchComboBox.addItem("Cast Member Name");
    searchComboBox.addItem("Genre");
    searchComboBox.addItem("DVD Keyword");
    searchComboBox.addItem("Book Title");

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

    d.setSize(500, 100);
    setMinimumSize(d);

    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    getRootPane().setDefaultButton(searchButton);
    searchTextField.requestFocus();

    setVisible(true);

    }
    //=====================================================
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
        String queryString;
        String searchString;

        ResultSet resultSet;

        searchString = searchTextField.getText().trim();    //get what the user wants to search for

        comboObject = searchComboBox.getSelectedItem();     //get what they want to search in from the combo box
                                                            //and relate it to the Queries data members
        queryString = "";
        if (comboObject.toString().equals("DVD Title"))
            {
            queryString = "title_DVDs_Search";
            }
        else if (comboObject.toString().equals("Director Name"))
            {
            queryString = "director_Search";
            }
        else if (comboObject.toString().equals("Cast Member Name"))
            {
            queryString = "cast_Search";
            }
        else if (comboObject.toString().equals("Genre"))
            {
            queryString = "genre_Search";
            }
        else if (comboObject.toString().equals("DVD Keyword"))
            {
            queryString = "keyword_DVDs_Search";
            }
        else if (comboObject.toString().equals("Book Title"))
            {
            queryString = "title_Books_Search";
            }


        try
            {
            Connection connection;
            connection = pointerToStoreFrame.connection;    //just to shorten the next line of code up.
            preparedStatement = connection.prepareStatement(listOfQueries.getQuery(queryString));
            //to avoid having duplicate code inside multiple if's, I added a method to Queries
            preparedStatement.clearParameters();
            preparedStatement.setString(1, searchString);   //this format will have to change should we ever need to run queries with
                                                            //multiple '?' in them

            resultSet = preparedStatement.executeQuery();

            pointerToStoreFrame.updateResultTable(resultSet);
            }
        catch (SQLException sqle)
            {
            System.out.println("SQLException in SearchJDialog actionPerformed");

            sqle.printStackTrace();
            }
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
    if (searchTextField.getText().trim().equals(""))
        {
        searchButton.setEnabled(false);
        }
    else
        {
        searchButton.setEnabled(true);
        }
    }
    //=====================================================
    public void insertUpdate(DocumentEvent e)
    {
    if (searchTextField.getText().trim().equals(""))
        {
        searchButton.setEnabled(false);
        }
    else
        {
        searchButton.setEnabled(true);
        }
    }
    //=====================================================

}
//#########################################################