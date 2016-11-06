/*DVD Title search*/
SELECT DISTINCT media.title 
FROM dvds, media
WHERE media.title = dvds.title AND media.title like "catdog";
/*replace "catdog" with ? in java program.*/
/************************************************************************************************************************************************************************************************************************/

/*Director*/
SELECT DISTINCT media.title 
FROM dvds, media
WHERE media.title = dvds.title AND  dvds.director like "%neil patrick harris%"
/*replace "neil patrick harris" with ? in java program.*/
/************************************************************************************************************************************************************************************************************************/

/*cast search*/ 
SELECT DISTINCT media.title 
FROM dvds, media
WHERE media.title = dvds.title AND  dvds.cast like "%some%";
/************************************************************************************************************************************************************************************************************************/

/*genre search*/ 
SELECT DISTINCT media.title 
FROM dvds, media
WHERE media.title = dvds.title AND  dvds.genre like "%gay%";
/************************************************************************************************************************************************************************************************************************/
/*sequel_Search*/
SELECT DISTINCT sequel_to.sequel_title 
FROM dvds, media, sequel_to
WHERE media.title = "catdog" AND media.title = dvds.title AND  dvds.title = sequel_to.prequel_title;
/************************************************************************************************************************************************************************************************************************/
/*Books Title search*/
SELECT DISTINCT media.title 
FROM books, written_by, authors, media
WHERE media.title = books.title  AND books.title like "WitchCraft and Wizardry";
/************************************************************************************************************************************************************************************************************************/

/*Books subject_category search*/ 
SELECT DISTINCT media.title 
FROM books, written_by, authors, media
WHERE media.title = books.title  AND books.subject_category like "horror";
/************************************************************************************************************************************************************************************************************************/
/*Authors Search*/
SELECT DISTINCT media.title 
FROM books, written_by, authors, media
WHERE media.title = books.title AND authors.name = written_by.name AND authors.address = written_by.address AND authors.name like "fred";
/*replace "fred" with ? in java program.*/
/************************************************************************************************************************************************************************************************************************/
/*Publisher Search*/
SELECT DISTINCT media.title 
FROM books, media
WHERE media.title = books.title  AND name like "%zalgo%";
/*replace "%zalgo%" with ? in java program.*/    
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
UNION 
SELECT DISTINCT media.title 
FROM dvds, media
WHERE media.title = dvds.title AND  dvds.genre like "%some%";
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
WHERE media.title = books.title AND authors.name = written_by.name AND authors.address = written_by.address AND authors.name like "%fred%"
UNION
/*Publisher Search*/
SELECT DISTINCT media.title 
FROM books, media
WHERE media.title = books.title  AND name like "%fred%"
/*replace "%zalgo%" with ? in java program.*/    
UNION
/*Books subject_category search*/ 
SELECT DISTINCT media.title 
FROM books, media
WHERE media.title = books.title  AND books.subject_category like "%fred%";
/*"%fred%" will be replaced with ? also we could just like add our static strings togther....*/
/************************************************************************************************************************************************************************************************************************/






/*FOR INSERTION INTO purhcaseHistory*//*INSERT INTO `movies&books`.`purchase_history` (`transactionID`, `date_of_purchase`, `number_of_copies`, `total_cost`) VALUES ('1', CURDATE(), '1', '666');*/