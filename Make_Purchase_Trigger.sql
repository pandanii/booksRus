DELIMITER $$
FOR EACH ROW BEGIN
		WHEN(0) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Current item is out of stock';
		ELSE
			SET    media.copies_In_Stock = media.copies_In_Stock - 1
			WHERE  media.title = NEW.title;
	END CASE;
END
$$
DELIMITER ;
