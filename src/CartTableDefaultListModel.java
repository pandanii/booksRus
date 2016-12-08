
import java.util.*;
import javax.swing.*;

//#########################################################
public class CartTableDefaultListModel extends DefaultListModel implements CartDataManagerInterface
{

    private static final long serialVersionUID = 1L;

    public Vector<Object> cartRow;

    //=====================================================
    public CartTableDefaultListModel() //constructor
    {
        cartRow = new Vector<>();
    }

    //=====================================================
    public void addRow(Vector<Object> cartRow)//add the cartRow to the DefaultListModel
    {
        this.addElement(cartRow);
    }

    //=====================================================
    public void deleteRow(int rowToRemoveIndex)//delete a selected row
    {
        removeElementAt(rowToRemoveIndex);
    }
    //=====================================================
}
//#########################################################
