/*DVD Title search*/ /*WORKS*/ /*NEEDS TOP SELLER!!!!*/
SELECT media.title as "DVD Title" , media.year as "YEAR RELEASED", media.price, media.copies_In_Stock as "Amount in stock", dvds.cast, dvds.director, dvds.genre 
FROM dvds, media
WHERE media.title = dvds.title AND media.title like "catdog";
/*replace "catdog" with ? in java program.*/
/*EndOf Director Search*/

/************************************************************************************************************************************************************************************************************************/
/*Books Title search*/ /*WORKS*/ /*NEEDS TOP SELLER!!!!*/
SELECT media.title as "DVD Title" , media.year as "YEAR RELEASED", media.price, media.copies_In_Stock as "Amount in stock", dvds.cast, dvds.director, dvds.genre 
FROM dvds, media
WHERE media.title = dvds.title AND media.title like "catdog";
/*replace "catdog" with ? in java program.*/
/*EndOf Director Search*/
/************************************************************************************************************************************************************************************************************************/

/*Director Search*/ /*WORKS*/ /*NEEDS TOP SELLER!!!!*/
SELECT media.title as "Book Title" , media.year as "YEAR RELEASED", media.price, media.copies_In_Stock as "Amount in stock" , books.ISBN, books.subject_category,books.name as "Publisher", authors.name as "Author Name"
FROM books, written_by, authors, media
WHERE media.title = books.title AND authors.name = written_by.name AND authors.address = written_by.address AND media.title = "WitchCraft and Wizardry";
/*replace "WitchCraft and Wizardry"" with ? in java program.*/
/*EndOf Director Search*/
/************************************************************************************************************************************************************************************************************************/

/*Authors Search*/ /*NEEDS TOP SELLER!!!!*/
SELECT media.title as "Book Title" , media.year as "YEAR RELEASED", media.price, media.copies_In_Stock as "Amount in stock" , books.ISBN, books.subject_category,books.name as "Publisher", authors.name as "Author Name"
FROM books, written_by, authors, media
WHERE media.title = books.title AND authors.name = written_by.name AND authors.address = written_by.address AND authors.name = "fred";
/*replace "fred" with ? in java program.*/
/*EndOf Authors Search*/
/************************************************************************************************************************************************************************************************************************/