DELIMITER $$
CREATE TRIGGER beforeInsertOnPurchase BEFORE INSERT ON purchase
FOR EACH ROW BEGIN
	CASE(SELECT copies_In_Stock FROM media WHERE title = NEW.title)
		WHEN(0) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Current item is out of stock';
		ELSE
			UPDATE media
			SET    media.copies_In_Stock = media.copies_In_Stock - 1
			WHERE  media.title = NEW.title;
	END CASE;
END
$$
DELIMITER ;