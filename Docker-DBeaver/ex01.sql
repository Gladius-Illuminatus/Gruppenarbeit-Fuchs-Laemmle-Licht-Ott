DROP SCHEMA IF EXISTS`ex01`;
-- In MySQL, the schema is the synonym for the database
-- DROP DATABASE [IF EXISTS] database_name;

CREATE SCHEMA `ex01` DEFAULT CHARACTER SET utf8 ;
USE `ex01` ;

CREATE TABLE ex01.auto (
	anr INT NOT NULL,
	marke varchar(100) NOT NULL,
	typ varchar(100) NULL,
	baujahr INT NULL,
	CONSTRAINT auto_PK PRIMARY KEY (anr)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;

CREATE TABLE ex01.person (
	pnr INT NOT NULL,
	name varchar(100) NULL,
	vorname varchar(100) NULL,
	anr INT NULL,
	CONSTRAINT person_PK PRIMARY KEY (pnr)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;

INSERT INTO ex01.auto (anr,marke,typ,baujahr) VALUES
	 (1,'Audi','A6',2008),
	 (2,'Opel','Astra',2007),
	 (3,'FIAT','Punto',2002),
	 (4,'VW','Bora',2006),
	 (5,'Toyota','Yaris',2006);

INSERT INTO ex01.person (pnr,name,vorname,anr) VALUES
	 (1,'Zimmer','Hans',2),
	 (2,'Tolkien','John',4),
	 (3,'Pratchett','Terry',1),
	 (4,'Orwell','George',3),
	 (5,'Einstein','Albert',5);
	 	
ALTER TABLE ex01.person 
	ADD CONSTRAINT person_FK 
	FOREIGN KEY (anr)
	REFERENCES ex01.auto(anr);

CREATE OR REPLACE VIEW ex01.Autobesitzer
AS SELECT
    p.pnr,
    p.name,
    p.vorname,
    a.marke,
    a.typ,
    a.baujahr
FROM
    ex01.auto a
INNER JOIN ex01.person p ON
    a.anr = p.anr;
