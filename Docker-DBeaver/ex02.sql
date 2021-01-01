DROP SCHEMA IF EXISTS`ex02`;
-- In MySQL, the schema is the synonym for the database
-- DROP DATABASE [IF EXISTS] database_name;

CREATE SCHEMA `ex02` DEFAULT CHARACTER SET utf8 ;
USE `ex02` ;

CREATE TABLE ex02.auto (
	anr INT NOT NULL,
	marke varchar(100) NOT NULL,
	typ varchar(100) NULL,
	baujahr INT NULL,
	CONSTRAINT auto_PK PRIMARY KEY (anr)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;

CREATE TABLE ex02.person (
	pnr INT NOT NULL,
	name varchar(100) NULL,
	vorname varchar(100) NULL,
	anr INT NULL,
	CONSTRAINT person_PK PRIMARY KEY (pnr)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;

INSERT INTO ex02.auto (anr,marke,typ,baujahr) VALUES
	 (1,'Audi','A6',2008),
	 (2,'Opel','Astra',2007),
	 (3,'FIAT','Punto',2002);

INSERT INTO ex02.person (pnr,name,vorname,anr) VALUES
	 (1,'Zimmer','Hans',1),
	 (2,'Tolkien','John',2),
	 (3,'Pratchett','Terry',3),
	 (4,'Orwell','George',NULL),
	 (5,'Einstein','Albert',NULL);
	 	
ALTER TABLE ex02.person 
	ADD CONSTRAINT person_FK 
	FOREIGN KEY (anr)
	REFERENCES ex02.auto(anr);

CREATE OR REPLACE VIEW ex02.autoperson
AS SELECT
    p.pnr,
    p.name,
    p.vorname,
    a.marke,
    a.typ,
    a.baujahr
FROM
    ex02.auto a
INNER JOIN ex02.person p ON
    a.anr = p.anr;
