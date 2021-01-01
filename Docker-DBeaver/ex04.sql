DROP SCHEMA IF EXISTS`ex04`;
-- In MySQL, the schema is the synonym for the database
-- DROP DATABASE [IF EXISTS] database_name;

CREATE SCHEMA `ex04` DEFAULT CHARACTER SET utf8 ;
USE `ex04` ;

CREATE TABLE ex04.person (
	pnr INT NOT NULL,
	name varchar(100) NULL,
	vorname varchar(100) NULL,
	CONSTRAINT person_PK PRIMARY KEY (pnr)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;

INSERT INTO ex04.person (pnr,name,vorname) VALUES
	 (1,'Einstein','Albert'),
	 (2,'Tesla','Nikola'),
	 (3,'Fermi','Enrico'),
	 (4,'Hoffmann','Amadeus'),
	 (5,'Feynman','Richard');
	 
CREATE TABLE ex04.auto (
	anr INT NULL,
	marke varchar(100) NULL,
	typ varchar(100) NULL,
	baujahr INT NULL,
	pnr INT NOT NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;

INSERT INTO ex04.auto (anr,marke,typ,baujahr,pnr) VALUES
	 (1,'Audi','A6','2008',3),
	 (2,'Opel','Astra',2007,1),
	 (3,'Fiat','Punto',2002,4),
	 (4,'VW','Bora',2003,2),
	 (5,'Toyota','Yaris',2006,2),
	 (6,'VW','Golf',2004,1),
	 (7,'Honda','Civic',2004,2);
	
ALTER TABLE ex04.auto ADD CONSTRAINT auto_PK PRIMARY KEY (anr);

CREATE OR REPLACE VIEW ex04.autoperson
AS SELECT
	a.pnr,
	p.name,
	p.vorname,
	a.marke,
	a.typ,
    a.baujahr
FROM
    ex04.auto a
INNER JOIN ex04.person p ON
    a.pnr = p.pnr;
   
CREATE OR REPLACE VIEW ex04.auto_allperson
AS SELECT
    p.pnr,
    p.name,
    p.vorname,
    a.marke,
    a.typ,
    a.baujahr
FROM
    ex04.auto a
RIGHT JOIN ex04.person p ON
    a.pnr = p.pnr;

CREATE OR REPLACE VIEW ex04.person_no_car
AS SELECT
    p.pnr,
    p.name,
    p.vorname,
    a.marke,
    a.typ,
    a.baujahr
FROM
    ex04.auto a
RIGHT JOIN ex04.person p ON
    a.pnr = p.pnr
WHERE 
	a.baujahr IS NULL; 

