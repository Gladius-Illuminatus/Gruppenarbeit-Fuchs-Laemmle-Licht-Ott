DROP SCHEMA IF EXISTS`ex06`; 
-- In MySQL, the schema is the synonym for the database
-- DROP DATABASE [IF EXISTS] database_name;

CREATE SCHEMA `ex06` DEFAULT CHARACTER SET utf8 ;
USE `ex06` ;

CREATE TABLE ex06.person (
	pnr INT NOT NULL,
	name varchar(100) NULL,
	vorname varchar(100) NULL,
	CONSTRAINT person_PK PRIMARY KEY (pnr)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;

INSERT INTO ex06.person (pnr,name,vorname) VALUES
	 (1,'Einstein','Albert'),
	 (2,'Tesla','Nikola'),
	 (3,'Fermi','Enrico');
	 
CREATE TABLE ex06.auto (
	anr INT NULL,
	marke varchar(100) NULL,
	typ varchar(100) NULL,
	baujahr INT NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;

INSERT INTO ex06.auto (anr,marke,typ,baujahr) VALUES
	 (1,'Audi','A6',2008),
	 (2,'Opel','Astra',2007),
	 (3,'Fiat','Punto',2002),
	 (4,'VW','Bora',2003),
	 (5,'Toyota','Yaris',2006),
	 (6,'VW','Golf',2004),
	 (7,'Honda','Civic',2004);
	 
	
ALTER TABLE ex06.auto ADD CONSTRAINT auto_PK PRIMARY KEY (anr);

CREATE TABLE ex06.fahrzeughalter (
	pnr INT NULL,
	anr INT NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci;

INSERT INTO ex06.fahrzeughalter (pnr,anr) VALUES
	 (1,2),
	 (1,6),
	 (2,4),
	 (2,7),
	 (3,1);
	 
ALTER TABLE ex06.fahrzeughalter ADD CONSTRAINT fahrzeughalter_FK FOREIGN KEY (anr) REFERENCES ex06.auto(anr);
ALTER TABLE ex06.fahrzeughalter ADD CONSTRAINT fahrzeughalter_FK_1 FOREIGN KEY (pnr) REFERENCES ex06.person(pnr);

CREATE OR REPLACE VIEW ex06.autoperson
AS SELECT
    p.name,
    p.vorname,
    a.marke,
    a.typ,
    a.baujahr
FROM
    ex06.auto a
INNER JOIN ex06.fahrzeughalter f ON
    a.anr = f.anr
    AND a.anr = f.anr
INNER JOIN ex06.person p ON
    f.pnr = p.pnr
    AND f.pnr = p.pnr;
   
CREATE OR REPLACE VIEW ex06.auto_allperson
AS SELECT
    p.name,
    p.vorname,
    a.marke,
    a.typ,
    a.baujahr
FROM
    ex06.auto a
RIGHT JOIN ex06.fahrzeughalter f ON
    a.anr = f.anr
    AND a.anr = f.anr
RIGHT JOIN ex06.person p ON
    f.pnr = p.pnr
    AND f.pnr = p.pnr;

CREATE OR REPLACE VIEW ex06.person_allauto
AS SELECT
    a.marke,
    a.typ,
    a.baujahr,
	p.name,
    p.vorname
FROM
    ex06.person p
RIGHT JOIN ex06.fahrzeughalter f ON
    p.pnr = f.pnr
    AND p.pnr = f.pnr
RIGHT JOIN ex06.auto a ON
    f.anr = a.anr
    AND f.anr = a.anr;
   
CREATE OR REPLACE VIEW ex06.allperson_nocar 
AS SELECT
    p.name,
    p.vorname
FROM
    ex06.auto a
RIGHT JOIN ex06.fahrzeughalter f ON
    a.anr = f.anr
    AND a.anr = f.anr
RIGHT JOIN ex06.person p ON
    f.pnr = p.pnr
    AND f.pnr = p.pnr
WHERE a.baujahr IS NULL;

CREATE OR REPLACE VIEW ex06.allcar_noperson
AS SELECT
    a.marke,
    a.typ,
    a.baujahr
FROM
    ex06.person p
RIGHT JOIN ex06.fahrzeughalter f ON
    p.pnr = f.pnr
    AND p.pnr = f.pnr
RIGHT JOIN ex06.auto a ON
    f.anr = a.anr
    AND f.anr = a.anr
WHERE p.vorname IS NULL;   



