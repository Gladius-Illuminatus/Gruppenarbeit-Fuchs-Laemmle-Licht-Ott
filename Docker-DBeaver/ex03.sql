DROP SCHEMA IF EXISTS`ex03`;
-- In MySQL, the schema is the synonym for the database
-- DROP DATABASE [IF EXISTS] database_name;

CREATE SCHEMA `ex03` DEFAULT CHARACTER SET utf8 ;
USE `ex03` ;

CREATE TABLE ex03.auto (
	anr INT NOT NULL,
	marke varchar(100) NOT NULL,
	typ varchar(100) NULL,
	baujahr INT NULL,
	CONSTRAINT auto_PK PRIMARY KEY (anr)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;

CREATE TABLE ex03.person (
	pnr INT NOT NULL,
	name varchar(100) NULL,
	vorname varchar(100) NULL,
	anr INT NULL,
	CONSTRAINT person_PK PRIMARY KEY (pnr)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;

INSERT INTO ex03.auto (anr,marke,typ,baujahr) VALUES
	 (1,'Audi','A6',2008),
	 (2,'Opel','Astra',2007),
	 (3,'FIAT','Punto',2002),
	 (4,'VW','Bora',2003),
	 (5,'Toyota','Yaris',2006),
	 (6,'VW','Golf',2004),
	 (7,'Honda','Civic',2004);

INSERT INTO ex03.person (pnr,name,vorname,anr) VALUES
	 (1,'Zimmer','Hans',1),
	 (2,'Tolkien','John',2),
	 (3,'Pratchett','Terry',3),
	 (4,'Orwell','George',4),
	 (5,'Einstein','Albert',5);
	 	
ALTER TABLE ex03.person 
	ADD CONSTRAINT person_FK 
	FOREIGN KEY (anr)
	REFERENCES ex03.auto(anr);

CREATE OR REPLACE VIEW ex03.autoperson
AS SELECT
    p.pnr,
    p.name,
    p.vorname,
    a.marke,
    a.typ,
    a.baujahr
FROM
    ex03.auto a
INNER JOIN ex03.person p ON
    a.anr = p.anr;
