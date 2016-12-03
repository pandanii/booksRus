
import javax.swing.*;
import java.util.*;


//#########################################################
public class CartTableDefaultListModel extends DefaultListModel
									   implements CartDataManagerInterface
{
Vector<Object> cartRow;


	//=====================================================
	public CartTableDefaultListModel() //constructor
	{
	cartRow = new Vector<Object>();
	}
	//=====================================================
	public void addRow(Vector<Object> cartRow)
	{
//	System.out.println("CartTableDefaultListModel addRow");
	//add the cartRow to the DefaultListModel

	addElement(cartRow);

	}
	//=====================================================
	public void deleteRow(int rowToRemoveIndex)
	{
//	System.out.println("CartTableDefaultListModel deleteRow");
	//delete a selected row

	removeElementAt(rowToRemoveIndex);


	}
	//=====================================================

}
//#########################################################