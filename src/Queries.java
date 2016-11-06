/*
*This class is designed to hold the queries for the different actions performed by the database.
*/
public class Queries 
{
    public final String title_DVDs_Search;
    public final String director_Search;
    public final String cast_Search;
    public final String genre_Search;
    public final String sequel_Search;
    public final String keyword_DVDs_Search;
    
    public final String title_Books_Search;
    public final String author_Search;
    public final String publisher_Search;
    public final String subject_category_Search;
    
    
    public Queries()
    {
       /* DVDS SEARCH STUFF */
       title_DVDs_Search   = "SELECT DISTINCT media.title" 
                           + "FROM dvds, media" 
                           + "WHERE media.title = dvds.title AND media.title like ?";
       
       director_Search     = "SELECT DISTINCT media.title"  
                           + "FROM dvds, media" 
                           + "WHERE media.title = dvds.title AND  dvds.director like ?";
       
       cast_Search         = "SELECT DISTINCT media.title"  
                           + "FROM dvds, media" 
                           + "WHERE media.title = dvds.title AND  dvds.cast like ?";
        
       genre_Search        = "SELECT DISTINCT media.title"
                           + "FROM dvds, media"
                           + "WHERE media.title = dvds.title AND  dvds.genre like ?";
       
       keyword_DVDs_Search = title_DVDs_Search 
                           + " UNION " 
                           + director_Search 
                           + " UNION " 
                           + cast_Search 
                           + " UNION " 
                           + genre_Search;
       
       /* BOOKS SEARCH STUFF */
       title_Books_Search  = "SELECT DISTINCT media.title "
                           + "FROM books, written_by, authors, media"
                           + "WHERE media.title = books.title  AND books.title like ?";
    }
    
    
}
