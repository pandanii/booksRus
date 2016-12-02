import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.List;

public class AddBookDialog extends JDialog implements ActionListener
{
   private JTextField        titleTextField;
   private JTextField        priceTextField;
   private JTextField        numCopiesTextField;
   
   private JSpinner          dateJSpinner;
   private SpinnerDateModel  mySpinnerDateModel;
   private JComponent        spinEditor;

   private JTextField        isbnTextField;
   private JTextField        subjectTextField;
   private JTextField        publisherAddressTextField;
   private JTextField        publisherNameTextField;
   private JTextField        authorAddressTextField;
   private JTextField        authorNameTextField;
    
   private JLabel        titleJLabel;
   private JLabel        priceJLabel;
   private JLabel        numCopiesJLabel;
   private JLabel        dateSpinnerJLabel;
   private JLabel        isbnJLabel;
   private JLabel        subjectJLabel;
   private JLabel        publisherAddressJLabel;
   private JLabel        publisherNameJLabel;
   private JLabel        authorAddressJLabel;
   private JLabel        authorNameJLabel;
   
     
   private Connection connection;
   private Queries    listOfQueries;
   
   private JPanel mainPanel;
   private JPanel buttonPanel;
   
   private JButton doneButton;
   private StoreFrame pointerToStoreFrame;

public AddBookDialog(StoreFrame pointerToStoreFrame)
{
    this.pointerToStoreFrame = pointerToStoreFrame;     //so a method of StoreFrame can be called later.
    connection = pointerToStoreFrame.connection;
    listOfQueries = new Queries();
    
    mainPanel =  new JPanel();
    mainPanel.setLayout(new GridLayout(14,2,0,3));
    buttonPanel  =  new JPanel();
    
    mySpinnerDateModel = new SpinnerDateModel();
    dateJSpinner       = new JSpinner(mySpinnerDateModel);
    spinEditor         = new JSpinner.DateEditor(dateJSpinner,"yyyy-MM-dd");
    dateJSpinner.setEditor(spinEditor);
    
    titleJLabel    = new JLabel("Title: "); 
    titleTextField = new JTextField();
    mainPanel.add(titleJLabel);
    mainPanel.add(titleTextField);
           
    priceJLabel    = new JLabel("Cost: "); 
    priceTextField  = new JTextField();
    mainPanel.add(priceJLabel);
    mainPanel.add(priceTextField); 
           
    numCopiesJLabel = new JLabel("#inStock: ");
    numCopiesTextField = new JTextField();
    mainPanel.add(numCopiesJLabel);
    mainPanel.add(numCopiesTextField); 

    dateSpinnerJLabel = new JLabel("DATE RELEASED: ");
    mainPanel.add(dateSpinnerJLabel);
    mainPanel.add(dateJSpinner); 
   
    isbnJLabel = new JLabel("ISBN: ");
    isbnTextField = new JTextField();
    mainPanel.add(isbnJLabel);
    mainPanel.add(isbnTextField); 
   
    subjectJLabel = new JLabel("Subject: ");
    subjectTextField = new JTextField();
    mainPanel.add(subjectJLabel);
    mainPanel.add(subjectTextField);
   
    publisherAddressJLabel = new JLabel("Publisher's Address: ");
    publisherAddressTextField = new JTextField();
    mainPanel.add(publisherAddressJLabel);
    mainPanel.add(publisherAddressTextField);
   
    publisherNameJLabel = new JLabel("Publisher's Name: ");
    publisherNameTextField = new JTextField();
    mainPanel.add(publisherNameJLabel);
    mainPanel.add(publisherNameTextField);
   
    authorAddressJLabel = new JLabel("Author's Address: ");
    authorAddressTextField = new JTextField();
    mainPanel.add(authorAddressJLabel);
    mainPanel.add(authorAddressTextField);
   
    authorNameJLabel   = new JLabel("Author's Name: ");
    authorNameTextField = new JTextField();
    mainPanel.add(authorNameJLabel);
    mainPanel.add(authorNameTextField);
    
    this.add(mainPanel,BorderLayout.CENTER);
   
    doneButton = new JButton("Done");
    doneButton.setActionCommand("DONE");
    doneButton.addActionListener(this);
    getRootPane().setDefaultButton(doneButton);
    
    buttonPanel.add(doneButton);
    this.add(buttonPanel,BorderLayout.SOUTH);
    
    this.setupMainFrame();
}
//-----------------------------------------------------
  public void setupMainFrame()
  {
    Toolkit   tk = Toolkit.getDefaultToolkit();
    Dimension d  = tk.getScreenSize();
    this.setSize(300,350);
    this.setMinimumSize(new Dimension(300,350));
    this.setLocation(d.width/4, d.height/8);
    setTitle("Add Book to Database");
    setVisible(true);
  }
 //----------------------------------------------------- 
    @Override
  public void actionPerformed(ActionEvent e)
{
    PreparedStatement preparedStatement;
    ResultSet resultSet = null;
    if(e.getActionCommand().equals("DONE"))
    {
        /*-- QUERY(s): "INSERT INTO MEDIA(Title,price,copies_In_Stock,year) values (?,?,?,?)"
                       "INSERT INTO books(Title,ISBN,subject_category,Address,name) values (?,?,?,?,?)"
                       try insert " INSERT INTO AUTHORS(Address,Name) values (?,?)" // will be blocked if found
                       "INSERT INTO written_by(Title,Address,name) values (?,?,?)" */
        try
        {
            // This section is for the 1st insertion query on media.
            preparedStatement = connection.prepareStatement(listOfQueries.insertMedia);//"INSERT INTO MEDIA(Title,price,copies_In_Stock,year) values (?,?,?,?)"
            preparedStatement.setString(1, titleTextField.getText().trim());
            preparedStatement.setInt(2, Integer.parseInt(priceTextField.getText().trim()));
            preparedStatement.setInt(3, Integer.parseInt(numCopiesTextField.getText().trim()));
            preparedStatement.setDate(4, new java.sql.Date(mySpinnerDateModel.getDate().getTime()));
            System.out.println("Attempting to execute INSERT with preparedStatement: " + preparedStatement.toString());
            preparedStatement.execute();
            preparedStatement.clearParameters();
            //---------------------------------------------------------
            // This section is for the 2nd insertion query on books.
            preparedStatement = connection.prepareStatement(listOfQueries.insertBooks);//"INSERT INTO books(Title,ISBN,subject_category,Address,name) values (?,?,?,?,?)"
            preparedStatement.setString(1, titleTextField.getText().trim());
            preparedStatement.setString(2, isbnTextField.getText().trim());
            preparedStatement.setString(3, subjectTextField.getText().trim());
            preparedStatement.setString(4, publisherAddressTextField.getText().trim());
            preparedStatement.setString(5, publisherNameTextField.getText().trim());
            System.out.println("Attempting to execute INSERT with preparedStatement: " + preparedStatement.toString());
            preparedStatement.execute();
            preparedStatement.clearParameters();
            //---------------------------------------------------------
            // This section is for the 3rd insertion query on Authors  // IF THIS FAILS THE OTHER MIGHT NOT GET EXECUTED!!! 
            preparedStatement = connection.prepareStatement(listOfQueries.insertAuthors);//try insert " INSERT INTO AUTHORS(Address,Name) values (?,?)" // will be blocked if found
            preparedStatement.setString(1, authorAddressTextField.getText().trim());
            preparedStatement.setString(2, authorNameTextField.getText().trim());
            System.out.println("Attempting to execute INSERT with preparedStatement: " + preparedStatement.toString());
            preparedStatement.execute();
            preparedStatement.clearParameters();
            //---------------------------------------------------------
            // This section is for the 4th insertion query on written_by
            preparedStatement = connection.prepareStatement(listOfQueries.insertWrittenBy);//"INSERT INTO written_by(Title,Address,name) values (?,?,?)"
            preparedStatement.setString(1, titleTextField.getText().trim());
            preparedStatement.setString(2, authorAddressTextField.getText().trim());
            preparedStatement.setString(3, authorNameTextField.getText().trim());
            System.out.println("Attempting to execute INSERT with preparedStatement: " + preparedStatement.toString());
            preparedStatement.execute();
            preparedStatement.clearParameters();
            //---------------------------------------------------------
            
        }
        catch(SQLException sqle1)
        {
            System.out.println("SQLException in addBookDialog actionPerformed");
            sqle1.printStackTrace();
            
            try// extra incase the author fails above.
            {
                // This section is for the 4th insertion query on written_by
                preparedStatement = connection.prepareStatement(listOfQueries.insertWrittenBy);//"INSERT INTO written_by(Title,Address,name) values (?,?,?)"
                preparedStatement.setString(1, titleTextField.getText().trim());
                preparedStatement.setString(2, authorAddressTextField.getText().trim());
                preparedStatement.setString(3, authorNameTextField.getText().trim());
                System.out.println("Attempting to execute INSERT with preparedStatement: " + preparedStatement.toString());
                preparedStatement.execute();
                preparedStatement.clearParameters();
            }
            catch(SQLException sqle2)
            {
                System.out.println("SQLException in addBookDialog actionPerformed,  tryCatch!!!! ");
                sqle1.printStackTrace();
            }
        }
        catch (NumberFormatException nfe) 
        {
            JOptionPane.showMessageDialog(this, "Number Format exception occured, please make sure that you entered a number in for price or number of copies", "ERROR.", JOptionPane.WARNING_MESSAGE);
            nfe.printStackTrace();
        }  
    }// end of done
    
}// end of Action performed  
//-----------------------------------------------------
}