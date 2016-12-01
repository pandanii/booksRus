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

public AddBookDialog()//StoreFrame pointerToStoreFrame)
{
    //this.pointerToStoreFrame = pointerToStoreFrame;     //so a method of StoreFrame can be called later.
    //connection = pointerToStoreFrame.connection;
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
    if(e.getActionCommand().equals("DONE"))
    {
        
    }
    
}  
//-----------------------------------------------------




}
