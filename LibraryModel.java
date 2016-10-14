/*
 * LibraryModel.java
 * Author:
 * Created on:
 */

import javax.swing.*;
import java.sql.*;

public class LibraryModel {

    // For use in creating dialogs and making them modal
    private JFrame dialogParent;
    private Connection conn = null;

    public LibraryModel(JFrame parent, String userid, String password) {
    	dialogParent = parent;
    	try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	String connURL = "jdbc:postgresql://db.ecs.vuw.ac.nz/bhulapriy_jdbc";

    	try {
			conn = DriverManager.getConnection(connURL,userid,password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	dialogParent = parent;

    }

    public String bookLookup(int isbn) {
    	String result = "";
    	String select;
    	Statement s;
    	ResultSet rs = null;

    	select = ("SELECT * "
    			+ " FROM book_author NATURAL JOIN book NATURAL JOIN author"
    			+ " WHERE isbn="+isbn
    			+ " ORDER BY authorseqno ");
		try {
			s = conn.createStatement();
			rs = s.executeQuery(select);
			if (rs==null){
				result = "There is no book with the isbn: "+isbn+"\n";
			}
			int bisbn = isbn;
			String bTitle = "";
			int bEd = 0;
			int bNo = 0;
			int bLeft = 0;
			String aLast = "";

			while(rs.next()){
				bTitle = rs.getString("title");
				bEd = rs.getInt("edition_no");
				bNo = rs.getInt("numofcop");
				bLeft = rs.getInt("numleft");
				aLast = rs.getString("surname");
				aLast = aLast.replaceAll(" ", "");
				if (aLast.equals(null)){
					aLast = "No Authors";
				}
			}
			result = result + isbn + ": " + bTitle +
					"\n"+ "Edition: " + bEd + " - Number of Copies: " + bNo + " - Copies Left: " + bLeft +
					"\n" + "Authors: " + aLast.replaceFirst("^", "");

			if(result==null){
				result = "No loaned books found \n";
			}
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }

    public String showCatalogue() {
    	String result = "Show Catalogue:\n";
    	String select;
    	Statement s;
    	ResultSet rs = null;

    	select = ("SELECT * "
    			+ "FROM book"
    			+ " ORDER BY isbn");
		try {
			s = conn.createStatement();
			rs = s.executeQuery(select);
			while(rs.next()){
				result = result + bookLookup(rs.getInt("isbn")) +"\n \n";
			}
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }

    public String showLoanedBooks() {
    	String result = "Show Loaned Books:\n";
    	String select;
    	Statement s;
    	ResultSet rs = null;

    	select = ("SELECT book.isbn AS isbn, book.title AS Title, book.edition_no AS Edition, customer.f_name AS Name, customer.l_name AS Surname"
    			+ " FROM book,customer,customer_book"
    			+ " WHERE cust_book.customerid = customer.customerid AND cust_book.isbn = book.isbn"
    			+ " ORDER BY isbn");
		try {
			s = conn.createStatement();
			rs = s.executeQuery(select);
			while(rs.next()){
				int bisbn = rs.getInt("isbn");
				String bTitle = rs.getString("title");
				int bEd = rs.getInt("edition_no");
				String cFirst = rs.getString("f_name");
				String cLast = rs.getString("l_name");

				result = "Loaned Book: \n" + bisbn + ": " + bTitle +
					"\n"+ "Edition: " + bEd + "\n"+
						"Borrower " + cFirst + ""+cLast;

			}
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }

    public String showAuthor(int authorID) {
    	String result = "";
    	String select;
    	Statement s;
    	ResultSet rs = null;

    	select = ("SELECT * "
    			+ " FROM author"
    			+ " WHERE authorid ="+authorID);
		try {
			s = conn.createStatement();
			rs = s.executeQuery(select);
			if(rs.next()){
				int aID = rs.getInt("authorid");
				String aName = rs.getString("name");
				aName = aName.replaceAll(" ", "");
				String aSurname = rs.getString("surname");
				aSurname = aSurname.replaceAll(" ", "");

				result = aID +": " + aName + " "+aSurname;
			}
			else{
				result = "No author with given ID found";
			}
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }

    public String showAllAuthors() {
    	String result = "Show All Authors:\n \n";
    	String select;
    	Statement s;
    	ResultSet rs = null;

    	select = ("SELECT * "
    			+ "FROM author"
    			+ " ORDER BY authorid");
		try {
			s = conn.createStatement();
			rs = s.executeQuery(select);
			while(rs.next()){
				result = result + showAuthor(rs.getInt("authorid")) +"\n \n";
			}
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }

    public String showCustomer(int customerID) {
    	String result = "";
    	String select;
    	Statement s;
    	ResultSet rs = null;

    	select = ("SELECT * "
    			+ " FROM customer"
    			+ " WHERE customerid ="+customerID);
		try {
			s = conn.createStatement();
			rs = s.executeQuery(select);
			if(rs.next()){
				int cID = rs.getInt("customerid");
				String cName = rs.getString("f_name");
				cName = cName.replaceAll(" ", "");
				String cSurname = rs.getString("l_name");
				cSurname = cSurname.replaceAll(" ", "");
				String cCity = rs.getString("city");

				result = cID +": " + cName + " "+cSurname + " - "+cCity;
			}
			else{
				result = "No customer with given ID found";
			}
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }

    public String showAllCustomers() {
    	String result = "Show All Customers:\n \n";
    	String select;
    	Statement s;
    	ResultSet rs = null;

    	select = ("SELECT * "
    			+ "FROM customer"
    			+ " ORDER BY customerid");
		try {
			s = conn.createStatement();
			rs = s.executeQuery(select);
			while(rs.next()){
				result = result + showCustomer(rs.getInt("customerid")) +"\n \n";
			}
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
    	return result;
    }

    public String borrowBook(int isbn, int customerID,
			     int day, int month, int year) {
	return "Borrow Book Stub";
    }

    public String returnBook(int isbn, int customerid) {
	return "Return Book Stub";
    }

    public void closeDBConnection() {
    	try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public String deleteCus(int customerID) {
    	return "Delete Customer";
    }

    public String deleteAuthor(int authorID) {
    	return "Delete Author";
    }

    public String deleteBook(int isbn) {
    	return "Delete Book";
    }
}