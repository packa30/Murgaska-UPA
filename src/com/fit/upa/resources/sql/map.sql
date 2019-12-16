DROP INDEX map_geometry_sidx;
CREATE INDEX map_geometry_sidx ON map(geometry) INDEXTYPE IS MDSYS.SPATIAL_INDEX;

DROP TABLE multiobj;
DROP TABLE map;

CREATE TABLE map(
	type VARCHAR2(32),
	name VARCHAR2(32),
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


-------------------- e1 e2 ------------------------------

INSERT INTO map VALUES(
  'electric-area',
  'em1-area',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,4),              -- One circle
    SDO_ORDINATE_ARRAY(10,90, 20,100, 10,110)   -- area
  )
);

INSERT INTO map VALUES(
   'electric-mast',
   'em1',
   MDSYS.SDO_GEOMETRY(
      2001,
      NULL,
      MDSYS.SDO_POINT_TYPE(10, 100, NULL),
      NULL,
      NULL)
);

INSERT INTO map VALUES(
  'electric-area',
  'em2-area',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,4),     -- One circle
    SDO_ORDINATE_ARRAY( 50,115, 60,125, 50,135)  -- area
  )
);

INSERT INTO map VALUES(
   'electric-mast',
   'em2',
   MDSYS.SDO_GEOMETRY(
      2001,
      NULL,
      MDSYS.SDO_POINT_TYPE(50, 125, NULL),
      NULL,
      NULL)
);

INSERT INTO map VALUES(
  'electric-net',
  'e1-e2',
  SDO_GEOMETRY(
    2002,
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,2,1), -- compound line string
    SDO_ORDINATE_ARRAY(10,100, 50,125)
  )
);

-------------------- e2 e3 ------------------------------

INSERT INTO map VALUES(
  'electric-area',
  'em3-area',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,4),        -- One circle
    SDO_ORDINATE_ARRAY( 120,115, 130,125, 120,135)  -- area
  )
);

INSERT INTO map VALUES(
   'electric-mast',
   'em3',
   MDSYS.SDO_GEOMETRY(
      2001,
      NULL,
      MDSYS.SDO_POINT_TYPE(120, 125, NULL),
      NULL,
      NULL)
);

INSERT INTO map VALUES(
  'electric-net',
  'e2-e3',
  SDO_GEOMETRY(
    2002,
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,2,1), -- compound line string
    SDO_ORDINATE_ARRAY(50,125, 120,125)
  )
);

-------------------- e3 e4 ------------------------------

INSERT INTO map VALUES(	
  'electric-area',
  'em4-area',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,4),                -- One circle
    SDO_ORDINATE_ARRAY( 135,70, 145,80, 135,90)   -- area
  )
);

INSERT INTO map VALUES(
   'electric-mast',
   'em4',
   MDSYS.SDO_GEOMETRY(
      2001,
      NULL,
      MDSYS.SDO_POINT_TYPE(135, 80, NULL),
      NULL,
      NULL)
);

INSERT INTO map VALUES(
  'electric-net',
  'e3-e4',
  SDO_GEOMETRY(
    2002,
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,2,1), -- compound line string
    SDO_ORDINATE_ARRAY(120,125, 135,80)
  )
);
-------------------- e4 e5 ------------------------------

INSERT INTO map VALUES(
  'electric-area',
  'em5-area',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,4),     -- One circle
    SDO_ORDINATE_ARRAY( 185,30, 195,40, 185,50)  -- area
  )
);

INSERT INTO map VALUES(
   'electric-mast',
   'em5',
   MDSYS.SDO_GEOMETRY(
      2001,
      NULL,
      MDSYS.SDO_POINT_TYPE(185, 40, NULL),
      NULL,
      NULL)
);

INSERT INTO map VALUES(
  'electric-net',
  'e4-e5',
  SDO_GEOMETRY(
    2002,
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,2,1), -- compound line string
    SDO_ORDINATE_ARRAY(135,80, 185,40)
  )
);
------------------- Gas Station -------------------------
-------------------- g1 g2 ------------------------------

INSERT INTO map VALUES(	
  'gas-area',
  'gm1-area',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,4),  -- One circle
    SDO_ORDINATE_ARRAY( 60,10, 75,25, 60,40)  -- area
  )
);

INSERT INTO map VALUES(
   'gas-mast',
   'gm1',
   MDSYS.SDO_GEOMETRY(
      2001,
      NULL,
      MDSYS.SDO_POINT_TYPE(60, 25, NULL),
      NULL,
      NULL)
);

INSERT INTO map VALUES(
  'gas-area',
  'gm2-area',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,4),                  -- One circle
    SDO_ORDINATE_ARRAY( 100,160, 115,175, 100,190)  -- area
  )
);

INSERT INTO map VALUES(
   'gas-mast',
   'gm2',
   MDSYS.SDO_GEOMETRY(
      2001,
      NULL,
      MDSYS.SDO_POINT_TYPE(100, 175, NULL),
      NULL,
      NULL)
);

------------------- Gas pipeline --------------------------

INSERT INTO map VALUES (
  'gas-pipeline',
  'g1-g2',
   SDO_GEOMETRY (
     2006,
     NULL,
     NULL,
     SDO_ELEM_INFO_ARRAY(1,4,2 , 1,2,1, 3,2,1),
     SDO_ORDINATE_ARRAY(60, 25, 100,25,
                        100,25, 100,175))
);

INSERT INTO map VALUES(
  'gas-pipeline',
  'g2-gxl', -- no name left
  SDO_GEOMETRY(
    2002,
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,2,1), -- compound line string
    SDO_ORDINATE_ARRAY(100,175, 1,163)
  )
);

INSERT INTO map VALUES(
  'gas-pipeline',
  'g2-gxr', -- no name right
  SDO_GEOMETRY(
    2002,
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,2,1), -- compound line string
    SDO_ORDINATE_ARRAY(100,175, 199,102)
  )
);
------------------------- Areas + buildings----------------------
INSERT INTO map VALUES (
	'land', 'land01',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
    SDO_ELEM_INFO_ARRAY(1,1003,1), -- exterior rectangle (left-bottom, right-top) with holes
		SDO_ORDINATE_ARRAY(145,175, 145,140, 160,120, 160,85, 190,85, 190,175, 145,175)
	)
);

INSERT INTO map VALUES (
	'land', 'land02',
	SDO_GEOMETRY(
    2003,
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,1), -- exterior rectangle
		SDO_ORDINATE_ARRAY(20,175, 20, 140, 115,140, 115,175, 20,175)
	)
);

INSERT INTO map VALUES (
  'land', 'land03',
  SDO_GEOMETRY(
    2003,
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,1),
    SDO_ORDINATE_ARRAY(30,120, 30,1, 70,1, 70,55, 95,55, 95,120, 30,120)
	)
);
INSERT INTO map VALUES (
	'build', 'build1',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
    SDO_ELEM_INFO_ARRAY(1,1003,3), -- exterior rectangle
		SDO_ORDINATE_ARRAY(150,145, 185,170)
	)
);

INSERT INTO map VALUES (
	'build', 'build2',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
    SDO_ELEM_INFO_ARRAY(1,1003,3), -- interior rectangle
		SDO_ORDINATE_ARRAY(165,90,180,135)
	)
);

INSERT INTO map VALUES (
	'build', 'build3',
	SDO_GEOMETRY(
    2003,
    NULL,
    NULL, -- 2D polygon
    SDO_ELEM_INFO_ARRAY(1,1003,1), -- interior rectangle
		SDO_ORDINATE_ARRAY(35,115, 35,55, 60,55, 60,70,  80,70, 80,115, 35,115)
	)
);

INSERT INTO map VALUES (
	'build', 'build4',
	SDO_GEOMETRY(
    2003,
    NULL,
    NULL, -- 2D polygon
    SDO_ELEM_INFO_ARRAY(1,1003,3), -- interior rectangle
		SDO_ORDINATE_ARRAY(35,10, 50,40)
	)
);

------------------------- Ways HERO RUN -------------------------
INSERT INTO map VALUES(
  'road',
  'main_road',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1005,1, 1,2,1), -- compound polygon

    SDO_ORDINATE_ARRAY( 1,195,  1,185,  125,185,    125,145,    145,100,    145,50,     80,50,
                        80,1,   90,1,   90,40,      155,40,     155,50,     155,70,     199,70,
                        199,80, 155,80, 155,100,    135,145,    135,185,    199,185 ,   199,195, 1,195)
  )
);

--delete from map where name = 'build2';
SELECT type,name, geometry, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(geometry, 0.1) valid FROM map;
commit;

SELECT m.name, m.type, m.geometry FROM map m;


