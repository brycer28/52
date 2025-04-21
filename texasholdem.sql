DROP TABLE users;

CREATE TABLE users
        (Username        	VARCHAR(25) NOT NULL,
        Password         	VARBINARY(16) NOT NULL,
		ChipCount			INT
		);
		
		
ALTER TABLE users
	ADD CONSTRAINT users_username_pk PRIMARY KEY(Username);

INSERT INTO users VALUES ('thi', aes_encrypt('thi123', 'Key'), 500);
INSERT INTO users VALUES ('cali', aes_encrypt('cali123', 'Key'), 500);
INSERT INTO users VALUES ('bryce', aes_encrypt('bryce123', 'Key'), 500);
INSERT INTO users VALUES ('kendal', aes_encrypt('kendal123', 'Key'), 500);
INSERT INTO users VALUES ('michael', aes_encrypt('michael123', 'Key'), 500);

