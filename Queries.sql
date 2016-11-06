/*DVD Title search*/ /*WORKS*/ /*NEEDS TOP SELLER!!!!*/
SELECT DISTINCT media.title /* as "DVD Title" , media.year as "YEAR RELEASED", media.price, media.copies_In_Stock as "Amount in stock", dvds.cast, dvds.director, dvds.genre */
FROM dvds, media
WHERE media.title = dvds.title AND media.title like "catdog";
/*replace "catdog" with ? in java program.*/
/************************************************************************************************************************************************************************************************************************/

/*Books Title search*/ /*WORKS*/ /*NEEDS TOP SELLER!!!!*/
SELECT DISTINCT media.title /*as "Book Title" , media.year as "YEAR RELEASED", media.price, media.copies_In_Stock as "Amount in stock" , books.ISBN, books.subject_category,books.name as "Publisher", authors.name as "Author Name"*/
FROM books, written_by, authors, media
WHERE media.title = books.title AND authors.name = written_by.name AND authors.address = written_by.address AND media.title like "WitchCraft and Wizardry";
/*replace "catdog" with ? in java program.*/
/************************************************************************************************************************************************************************************************************************/

/*Director*/ /*WORKS*/ /*NEEDS TOP SELLER!!!!*/
SELECT DISTINCT media.title /*as "Movie Title" , media.year as "YEAR RELEASED",  media.price, media.copies_In_Stock as "Amount in stock", dvds.cast, dvds.director, dvds.genre */
FROM dvds, media
WHERE media.title = dvds.title AND  dvds.director like "%neil patrick harris%"
/*replace "neil patrick harris" with ? in java program.*/
/************************************************************************************************************************************************************************************************************************/

/*cast search*/ 
SELECT DISTINCT media.title /*as "Movie Title" , media.year as "YEAR RELEASED",  media.price, media.copies_In_Stock as "Amount in stock", dvds.cast, dvds.director, dvds.genre */
FROM dvds, media
WHERE media.title = dvds.title AND  dvds.cast like "%some%";
/*replace "someDudes" with ? in java program.*/
/************************************************************************************************************************************************************************************************************************/

/*Authors Search*/
SELECT DISTINCT media.title /*as "Book Title" , media.year as "YEAR RELEASED", media.price, media.copies_In_Stock as "Amount in stock" , books.ISBN, books.subject_category,books.name as "Publisher", authors.name as "Author Name"*/
FROM books, written_by, authors, media
WHERE media.title = books.title AND authors.name = written_by.name AND authors.address = written_by.address AND authors.name like "fred";
/*replace "fred" with ? in java program.*/
/************************************************************************************************************************************************************************************************************************/


/*keyword search dvds*/
CREATE OR REPLACE view  KeyWordSearch as
SELECT DISTINCT media.title
FROM dvds, media
WHERE media.title = dvds.title AND  dvds.cast like "%some%"
UNION
SELECT DISTINCT media.title
FROM dvds, media
WHERE media.title = dvds.title AND  dvds.cast like "%some%"
UNION
SELECT DISTINCT media.title
FROM dvds, media
WHERE media.title = dvds.title AND  dvds.director like "%some%"
UNION
SELECT DISTINCT media.title
FROM dvds, media
WHERE media.title = dvds.title AND media.title like "%some%";
/*"%some%" will be replaced with ?, also we could just like add our static strings togther....*/
/************************************************************************************************************************************************************************************************************************/



/*keyword search books*/
CREATE OR REPLACE view  KeyWordSearch as
SELECT DISTINCT media.title
FROM books, written_by, authors, media
WHERE media.title = books.title AND authors.name = written_by.name AND authors.address = written_by.address AND media.title like "%fred%"
UNION
/*Authors Search*/
SELECT DISTINCT media.title
FROM books, written_by, authors, media
WHERE media.title = books.title AND authors.name = written_by.name AND authors.address = written_by.address AND authors.name like "%fred%";
/*"%fred%" will be replaced with ? also we could just like add our static strings togther....*/
/************************************************************************************************************************************************************************************************************************/








SELECT DISTINCT media.title /*as "Book Title" , media.year as "YEAR RELEASED", media.price, media.copies_In_Stock as "Amount in stock" , books.ISBN, books.subject_category,books.name as "Publisher", authors.name as "Author Name"*/
FROM books, written_by, authors, media
WHERE media.title = books.title AND authors.name = written_by.name AND authors.address = written_by.address AND authors.name = "fred";


/*FOR INSERTION INTO purhcaseHistory*//*INSERT INTO `movies&books`.`purchase_history` (`transactionID`, `date_of_purchase`, `number_of_copies`, `total_cost`) VALUES ('1', CURDATE(), '1', '666');*/