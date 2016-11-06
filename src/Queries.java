/*
*This class is designed to hold the queries for the different actions performed by the database.
*/
public class Queries 
{
    public final String titleDVDsSearch;
    public final String titleBooksSearch;
    public final String directorSearch;
    public final String castSearch;
    public final String genreSearch;
    public final String movieGenreSearch;
    public final String genreSearch;
    public final String sequelSearch;
    public final String keywordDVDsSearch;
    
    
    public Queries()
    {
       /* DVDS SEARCH STUFF */
       titleDVDsSearch   = "SELECT DISTINCT media.title" 
                         + "FROM dvds, media" 
                         + "WHERE media.title = dvds.title AND media.title like ?";
       
       directorSearch    = "SELECT DISTINCT media.title"  
                         + "FROM dvds, media" 
                         + "WHERE media.title = dvds.title AND  dvds.director like ?";
       
       castSearch        = "SELECT DISTINCT media.title"  
                         + "FROM dvds, media" 
                         + "WHERE media.title = dvds.title AND  dvds.cast like ?";
       
       genreSearch       = "SELECT DISTINCT media.title"
                         + "FROM dvds, media"
                         + "WHERE media.title = dvds.title AND  dvds.genre like ?";
       
       keywordDVDsSearch = titleDVDsSearch + " UNION " + directorSearch + " UNION " + castSearch + " UNION " + genreSearch;
       
       /* BOOKS SEARCH STUFF */
       titleBooksSearch = "SELECT DISTINCT media.title "
                        + "FROM books, written_by, authors, media"
                        + "WHERE media.title = books.title  AND books.title like ?";
    }
    
    
}
