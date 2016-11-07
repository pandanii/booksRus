/*
 *This class is designed to hold the queries for the different actions performed by the database.
 *Storing them this way allows for cleaner looking code.
 */
public class Queries
{

    public final String displayResultsDVDs;
    //public final String displayResultsBooks;


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


    public Queries()
    {
        displayResultsDVDs = "SELECT DISTINCT * "
                           + "FROM media "
                           + "WHERE media. IN (";
      /*
       *!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
       *Theses queries are setup to just give us the media.title as the return.
       *An alternative query will display the results in the method we want.
       *!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
       */
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

       sequel_Search       = "SELECT DISTINCT sequel_to.sequel_title"
                           + "FROM dvds, media, sequel_to"
                           + "WHERE media.title = ? AND media.title = dvds.title AND  dvds.title = sequel_to.prequel_title";

       keyword_DVDs_Search = title_DVDs_Search
                           + " UNION "
                           + director_Search
                           + " UNION "
                           + cast_Search
                           + " UNION "
                           + genre_Search;


       /* BOOKS SEARCH STUFF */
       title_Books_Search  = "SELECT DISTINCT media.title "
                           + "FROM books, media"
                           + "WHERE media.title = books.title  AND books.title like ?";

       author_Search       = "SELECT DISTINCT media.title"
                           + "FROM books, written_by, authors, media"
                           + "WHERE media.title = books.title AND authors.name = written_by.name AND authors.address = written_by.address AND authors.name like ?";

       publisher_Search    = "SELECT DISTINCT media.title"
                           + "FROM books, media"
                           + "WHERE media.title = books.title  AND name like ?";

       subject_Cate_Search = "SELECT DISTINCT media.title"
                           + "FROM books, media"
                           + "WHERE media.title = books.title  AND books.subject_category like ?";

       keyword_Books_Search = title_Books_Search
                           + " UNION "
                           + author_Search
                           + " UNION "
                           + publisher_Search
                           + " UNION "
                           + subject_Cate_Search;
    }//EndOf Queries constructor

}//EndOf Queries class.
