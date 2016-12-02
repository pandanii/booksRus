import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddDvdDialog extends JDialog implements ActionListener
{
   private JTextField        titleTextField;
   private JTextField        priceTextField;
   private JTextField        numCopiesTextField;
   
   private JSpinner          dateJSpinner;
   private SpinnerDateModel  mySpinnerDateModel;
   private JComponent        spinEditor;

   private JTextField        castTextField;
   private JTextField        directorTextField;
   private JTextField        genreTextField;
   private JTextField        sequelTextField;

    
   private JLabel        titleJLabel;
   private JLabel        priceJLabel;
   private JLabel        numCopiesJLabel;
   private JLabel        dateSpinnerJLabel;
   private JLabel        castJLabel;
   private JLabel        directorJLabel;
   private JLabel        genreJLabel;
   private JLabel        sequelJLabel;
   
     
   private Connection connection;
   private Queries    listOfQueries;
   
   private JPanel mainPanel;
   private JPanel buttonPanel;
   
   private JButton doneButton;
   private StoreFrame pointerToStoreFrame;

public AddDvdDialog(StoreFrame pointerToStoreFrame)
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
   
    castJLabel = new JLabel("Cast: ");
    castTextField = new JTextField();
    mainPanel.add(castJLabel);
    mainPanel.add(castTextField); 
   
    directorJLabel = new JLabel("Director: ");
    directorTextField = new JTextField();
    mainPanel.add(directorJLabel);
    mainPanel.add(directorTextField);
   
    genreJLabel = new JLabel("Genre: ");
    genreTextField = new JTextField();
    mainPanel.add(genreJLabel);
    mainPanel.add(genreTextField);
   
    sequelJLabel = new JLabel("Sequel's Title: ");
    sequelTextField = new JTextField();
    mainPanel.add(sequelJLabel);
    mainPanel.add(sequelTextField);
    
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
    setTitle("Add dvd to Database");
    setVisible(true);
  }
 //----------------------------------------------------- 
    @Override
  public void actionPerformed(ActionEvent e)
{
    PreparedStatement preparedStatement;
    if(e.getActionCommand().equals("DONE"))
    {
        /*-- QUERY(s): "INSERT INTO MEDIA(Title,price,copies_In_Stock,year) values (?,?,?,?)"
             "INSERT INTO DVDS(Title,cast,director,genre) values (?,?,?,?)"
             "INSERT INTO sequel(prequel_title,sequel_title) values (?,?)"// optional only need to insert if pos*/
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
            // This section is for the 2nd insertion query on dvds.
            preparedStatement = connection.prepareStatement(listOfQueries.insertDvds);//INSERT INTO DVDS(Title,cast,director,genre) values (?,?,?,?)"
            preparedStatement.setString(1, titleTextField.getText().trim());
            preparedStatement.setString(2, castTextField.getText().trim());
            preparedStatement.setString(3, directorTextField.getText().trim());
            preparedStatement.setString(4, genreTextField.getText().trim());
            System.out.println("Attempting to execute INSERT with preparedStatement: " + preparedStatement.toString());
            preparedStatement.execute();
            preparedStatement.clearParameters();
            //---------------------------------------------------------
            // This section is for the 3rd insertion query on sequel  // IF THIS FAILS THE OTHER MIGHT NOT GET EXECUTED!!!
            if(!sequelTextField.getText().trim().isEmpty())
            {
                preparedStatement = connection.prepareStatement(listOfQueries.insertSequel);//"INSERT INTO sequel(prequel_title,sequel_title) values (?,?)"// optional only need to insert if pos*/
                preparedStatement.setString(1, titleTextField.getText().trim());
                preparedStatement.setString(2, sequelTextField.getText().trim());
                System.out.println("Attempting to execute INSERT with preparedStatement: " + preparedStatement.toString());
                preparedStatement.execute();
                preparedStatement.clearParameters();
            }
            //---------------------------------------------------------
            
        }
        catch(SQLException sqle1)
        {
            System.out.println("SQLException in addBookDialog actionPerformed");
            sqle1.printStackTrace();
        }
    }// end of done
    
}// end of Action performed  
//-----------------------------------------------------
}
