/*DVD Title search*/ /*WORKS*/ /*NEEDS TOP SELLER!!!!*/
SELECT DISTINCT media.title as "DVD Title" , media.year as "YEAR RELEASED", media.price, media.copies_In_Stock as "Amount in stock", dvds.cast, dvds.director, dvds.genre 
FROM dvds, media
WHERE media.title = dvds.title AND media.title like "catdog";
/*replace "catdog" with ? in java program.*/
/************************************************************************************************************************************************************************************************************************/

/*Books Title search*/ /*WORKS*/ /*NEEDS TOP SELLER!!!!*/
SELECT DISTINCT media.title as "Book Title" , media.year as "YEAR RELEASED", media.price, media.copies_In_Stock as "Amount in stock" , books.ISBN, books.subject_category,books.name as "Publisher", authors.name as "Author Name"
FROM books, written_by, authors, media
WHERE media.title = books.title AND authors.name = written_by.name AND authors.address = written_by.address AND media.title = "WitchCraft and Wizardry";
/*replace "catdog" with ? in java program.*/
/************************************************************************************************************************************************************************************************************************/

/*Director*/ /*WORKS*/ /*NEEDS TOP SELLER!!!!*/
SELECT DISTINCT media.title as "Movie Title" , media.year as "YEAR RELEASED",  media.price, media.copies_In_Stock as "Amount in stock", dvds.cast, dvds.director, dvds.genre 
FROM dvds, media
WHERE media.title = dvds.title AND  dvds.director like "%neil patrick harris%"
/*replace "neil patrick harris" with ? in java program.*/
/************************************************************************************************************************************************************************************************************************/

/*cast search/ /*WORKS*/ /*NEEDS TOP SELLER!!!!*/
SELECT DISTINCT media.title as "Movie Title" , media.year as "YEAR RELEASED",  media.price, media.copies_In_Stock as "Amount in stock", dvds.cast, dvds.director, dvds.genre 
FROM dvds, media
WHERE media.title = dvds.title AND  dvds.cast like "%some%";
/*replace "someDudes" with ? in java program.*/
/************************************************************************************************************************************************************************************************************************/

/*Authors Search*/ /*NEEDS TOP SELLER!!!!*/
SELECT DISTINCT media.title as "Book Title" , media.year as "YEAR RELEASED", media.price, media.copies_In_Stock as "Amount in stock" , books.ISBN, books.subject_category,books.name as "Publisher", authors.name as "Author Name"
FROM books, written_by, authors, media
WHERE media.title = books.title AND authors.name = written_by.name AND authors.address = written_by.address AND authors.name = "fred";
/*replace "fred" with ? in java program.*/
/************************************************************************************************************************************************************************************************************************/






/*FOR INSERTION INTO purhcaseHistory*//*INSERT INTO `movies&books`.`purchase_history` (`transactionID`, `date_of_purchase`, `number_of_copies`, `total_cost`) VALUES ('1', CURDATE(), '1', '666');*/