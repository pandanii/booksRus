/*
 *This class is designed to hold the queries for the different actions performed by the database.
 *Storing them this way allows for cleaner looking code.
 */
public class Queries
{

    public final String displayResultsDVDs;
    public final String displayResultsBooks;
    public final String purchase_History;

    public final String top_10;

    public final String title_DVDs_Search;
    public final String director_Search;
    public final String cast_Search;
    public final String genre_Search;
    public final String sequel_Search;
    public final String keyword_DVDs_Search;

    public final String title_Books_Search;
    public final String author_Search ;
    public final String publisher_Search;
    public final String subject_Cate_Search;
    public final String keyword_Books_Search;
    
  
    public final String admin_Book_Info;
    public final String admin_In_Last_24h;
    public final String admin_top_10;
    

    public Queries()
    {
        
        displayResultsDVDs = " SELECT DISTINCT media.title AS 'DVD Title', media.price AS 'Price', media.copies_In_Stock AS 'Amount in Stock', media.year as 'Year Released', dvds.cast AS 'CAST', dvds.director as Director, dvds.genre as Genre"
                           + " FROM media, dvds "
                           + " WHERE media.title = dvds.title AND media.title IN ( ";
        
       displayResultsBooks = " SELECT DISTINCT media.title as 'Book Title', media.price AS 'Price', media.copies_In_Stock AS 'Amount in Stock', media.year as 'Year Released', written_by.name as 'Author Name', books.ISBN, books.subject_category AS 'Subject Category', books.name as Publisher"
                           + " FROM media, books, written_by"
                           + " WHERE media.title = books.title AND books.title = written_by.title AND books.title IN ( ";
        
        purchase_History   = " SELECT DISTINCT purchase_history.date_of_purchase, purchase_history.total_cost, purchase_history.transactionID, media.title as \'book/dvd title\', media.price, media.year"
                           + " FROM  media, purchase_history, purchase, users"
                           + " WHERE  purchase_history.transactionID = purchase.transactionID AND users.userID = purchase.userID AND users.userID = ? AND  purchase.title = media.title";
        
        
        top_10             = " SELECT media.title"
                           + " FROM  media, purchase, purchase_history"
                           + " WHERE  purchase.transactionID = purchase_history.transactionID AND purchase.title = media.title GROUP BY purchase.title ORDER BY COUNT(*) DESC LIMIT 10;";
        
        /*This query is for *ADMINS ONLY* to geting the publisher info and the author info */
        admin_Book_Info    = " SELECT DISTINCT books.title as 'Book title', books.ISBN,books.subject_category, "
                           + "                publishers.address as 'Publishers address',publishers.name as 'Publishers name', publishers.URL as 'Publishers site', publishers.phone_number, "
                           + "                authors.name as 'authors name', authors.address as 'authors address'"
                           + " FROM books, publishers, authors, written_by, purchase_history" 
                           + " WHERE books.address = publishers.address AND books.name = publishers.name AND " 
                           +"       books.title = written_by.title AND written_by.address = authors.address AND written_by.name = authors.name AND books.title = ?";
        
       admin_In_Last_24h   = " SELECT DISTINCT purchase_history.date_of_purchase, purchase_history.total_cost, purchase_history.transactionID, purchase.title as 'Media Title',  users.userID as 'Purchasing User', users.address as 'Shipping Address', users.name"
                           + " FROM purchase_history, purchase, users"
                           +  " WHERE purchase_history.date_of_purchase between (now() - INTERVAL 1 DAY) AND NOW() AND purchase_history.transactionID = purchase.transactionID AND users.userID = purchase.userID";
        
       admin_top_10        = " SELECT media.title, media.price, media.copies_In_Stock, media.year"
                           + " FROM  media, purchase, purchase_history"
                           + " WHERE (purchase_history.date_of_purchase BETWEEN ( CURDATE() - INTERVAL 7 DAY) AND CURDATE() ) AND purchase.transactionID = purchase_history.transactionID AND" 
                           + "      purchase.title = media.title GROUP BY purchase.title ORDER BY COUNT(*) DESC LIMIT 10";
        
     
      /*
       *!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
       *Theses queries are setup to just give us the media.title as the return.
       *An alternative query will display the results in the method we want.
       *!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
       */
       /* DVDS SEARCH STUFF */
       title_DVDs_Search   = " SELECT DISTINCT media.title"
                           + " FROM dvds, media"
                           + " WHERE media.title = dvds.title AND media.title like ?";

       director_Search     = " SELECT DISTINCT media.title"
                           + " FROM dvds, media"
                           + " WHERE media.title = dvds.title AND  dvds.director like ?";

       cast_Search         = " SELECT DISTINCT media.title"
                           + " FROM dvds, media"
                           + " WHERE media.title = dvds.title AND  dvds.cast like ?";

       genre_Search        = " SELECT DISTINCT media.title"
                           + " FROM dvds, media"
                           + " WHERE media.title = dvds.title AND  dvds.genre like ?";

       sequel_Search       = " SELECT DISTINCT sequel_to.sequel_title"
                           + " FROM dvds, media, sequel_to"
                           + " WHERE media.title = ? AND media.title = dvds.title AND  dvds.title = sequel_to.prequel_title";

       keyword_DVDs_Search = title_DVDs_Search
                           + " UNION "
                           + director_Search
                           + " UNION "
                           + cast_Search
                           + " UNION "
                           + genre_Search;


       /* BOOKS SEARCH STUFF */
       title_Books_Search  = " SELECT DISTINCT media.title "
                           + " FROM books, media"
                           + " WHERE media.title = books.title  AND books.title like ?";

       author_Search       = " SELECT DISTINCT media.title"
                           + " FROM books, written_by, authors, media"
                           + " WHERE media.title = books.title AND books.title = written_by.title AND authors.name = written_by.name AND authors.address = written_by.address AND authors.name like ?";

       publisher_Search    = " SELECT DISTINCT media.title"
                           + " FROM books, media"
                           + " WHERE media.title = books.title  AND name like ?";

       subject_Cate_Search = " SELECT DISTINCT media.title"
                           + " FROM books, media"
                           + " WHERE media.title = books.title  AND books.subject_category like ?";

       keyword_Books_Search = title_Books_Search
                           + " UNION "
                           + author_Search
                           + " UNION "
                           + publisher_Search
                           + " UNION "
                           + subject_Cate_Search;
    }//EndOf Queries constructor

}//EndOf Queries class.
