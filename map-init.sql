DROP TABLE map;

CREATE TABLE map(
    id   NUMBER(8,2),
	name VARCHAR2(32),
	type VARCHAR2(32),
	geometry SDO_GEOMETRY,
	PRIMARY KEY (name, type)
);

DELETE FROM USER_SDO_GEOM_METADATA WHERE
	TABLE_NAME = 'MAP' AND COLUMN_NAME = 'GEOMETRY';

INSERT INTO USER_SDO_GEOM_METADATA VALUES (
	'map', 'geometry',
	-- X/Y axes in range 0-150 and accurancy 0.1 points (the size of the mesh is given, the accurancy needs to be lower than 1 point for circular trees)
	SDO_DIM_ARRAY(SDO_DIM_ELEMENT('X', 0, 200, 0.1), SDO_DIM_ELEMENT('Y', 0, 200, 0.1)),
	-- a local spatial reference system (not geographical; analytical functions will be without units)
	NULL
);

COMMIT;

-- check the validity (there can be an error message with an error code, see http://www.ora-code.com/)
-- with the custom accurance
SELECT name, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(geometry, 0.1) valid -- 0.1 is accurance
FROM map;
-- without the custom accurance (with respect to the accurance set in the SDO_GEOM_METADATA
SELECT c.name, c.geometry.ST_IsValid()
FROM map c;

quit
