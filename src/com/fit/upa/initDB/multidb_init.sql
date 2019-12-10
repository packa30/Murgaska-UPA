DROP TABLE MULTIOBJ;
DROP SEQUENCE multi_seq;

CREATE SEQUENCE multi_seq START WITH 1;

CREATE TABLE multiobj (
	id int DEFAULT multi_seq.nextval not null,
	spatname VARCHAR2(32),
    spattype VARCHAR2(32),
	image ORDSYS.ORDImage,
	image_si ORDSYS.SI_StillImage,
	image_ac ORDSYS.SI_AverageColor,
	image_ch ORDSYS.SI_ColorHistogram,
	image_pc ORDSYS.SI_PositionalColor,
	image_tx ORDSYS.SI_Texture,
    FOREIGN KEY ( spatname, spattype ) references map(name, type)
);

ALTER TABLE multiobj ADD (
  CONSTRAINT multi_pk PRIMARY KEY (ID));

