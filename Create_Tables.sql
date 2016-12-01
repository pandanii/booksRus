CREATE TABLE Users (
        userID VARCHAR (12) PRIMARY KEY,
        password VARCHAR (12) NOT NULL,
        phone_number VARCHAR(10),
        address VARCHAR (40) NOT NULL,
        email VARCHAR (40) NOT NULL,
        name VARCHAR (40) NOT NULL,
        is_Admin TINYINT DEFAULT 0);

CREATE TABLE MEDIA ( 
        title VARCHAR (30) PRIMARY KEY,
        price INT NOT NULL,
        copies_In_Stock INT NOT NULL,
        year YEAR);

CREATE TABLE Authors (
        address VARCHAR(40),
        name VARCHAR(20),
        PRIMARY KEY (address, name));

CREATE TABLE Purchase_History (
        transactionID INT PRIMARY KEY,
        date_of_purchase DATE NOT NULL,
        number_of_copies INT NOT NULL,
        total_cost INT);
        
CREATE TABLE Publishers (
        address VARCHAR(40),
        name VARCHAR(40),
        PRIMARY KEY(address, name),
        URL VARCHAR(40),
        phone_number VARCHAR (30));


CREATE TABLE Purchase (
    	userID VARCHAR (12),
        title VARCHAR (30),
    	transactionID INT,
        FOREIGN KEY (userID) REFERENCES Users(userID),
        FOREIGN KEY (title) REFERENCES MEDIA (title) ON DELETE CASCADE ON UPDATE CASCADE,
        FOREIGN KEY (transactionID) REFERENCES Purchase_History(transactionID),
        PRIMARY KEY (userID,title, transactionID));


CREATE TABLE Books (
        title VARCHAR (30),
        ISBN INT,
        subject_category VARCHAR (20),
        address VARCHAR(40), 
        name VARCHAR(40),
        FOREIGN KEY (title) REFERENCES MEDIA (title) ON DELETE CASCADE ON UPDATE CASCADE, 
        FOREIGN KEY (address,name) REFERENCES Publishers(address,name),
        PRIMARY KEY (title));


CREATE TABLE DVDs (
        title VARCHAR (30),
        cast VARCHAR (20),
        director VARCHAR (20),
        genre VARCHAR (20),
        PRIMARY KEY (title),
        FOREIGN KEY (title) REFERENCES MEDIA (title) ON DELETE CASCADE ON UPDATE CASCADE);


CREATE TABLE Sequel_To (
        prequel_title VARCHAR (30),
		sequel_title VARCHAR (30),
        FOREIGN KEY (prequel_title) REFERENCES DVDs(title),
        FOREIGN KEY (sequel_title) REFERENCES DVDs(title));

CREATE TABLE Written_By (
        title VARCHAR (30),
        address VARCHAR(40),
        name VARCHAR(20),
        FOREIGN KEY (title) REFERENCES Books(title),
        FOREIGN KEY (address, name) REFERENCES authors(address, name),
        PRIMARY KEY (title, address, name));
