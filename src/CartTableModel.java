import java.awt.*;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.table.*;
import javax.swing.event.*;
import java.lang.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.*;

//#########################################################
public class CartTableModel extends AbstractTableModel
{

CartTableDefaultListModel cartTableDefaultListModel;


	//=====================================================
	public CartTableModel() //constructor
	{
	System.out.println("CartTableModel constructor");

	cartTableDefaultListModel = new CartTableDefaultListModel();

	}
	//=====================================================
	public int getRowCount()
	{
//	System.out.println("CartTableModel getRowCount");

	return cartTableDefaultListModel.size();

	}
	//=====================================================
	public int getColumnCount()
	{
//	System.out.println("CartTableModel getColumnCount");

	return 3;

	}
	//=====================================================
	public Object getValueAt(int row, int col)
	{
//	System.out.println("CartTableModel getValueAt");

	Vector<Object> cartRow;

	cartRow = (Vector<Object>)cartTableDefaultListModel.elementAt(row);


	if (col == 0)
		{
		return cartRow.elementAt(0);
		}
	else if (col == 1)
		{
		return cartRow.elementAt(1);
		}
	else if (col == 2)
		{
		return cartRow.elementAt(2);
		}

	return null;

	}
	//=====================================================


}
//#########################################################