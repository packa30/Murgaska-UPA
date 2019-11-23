-------------------- e1 e2 ------------------------------
INSERT INTO map VALUES(
  'e1',
  'electric-mast',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,4, 7,1003,4),    -- Two circle
    SDO_ORDINATE_ARRAY( 10,90, 20,100, 10,110,  -- area
                        10,98, 12,100, 10,102)  -- mast
  )
);

INSERT INTO map VALUES(
  'e2',
  'electric-mast',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,4, 7,1003,4),     -- Two circle
    SDO_ORDINATE_ARRAY( 50,115, 60,125, 50,135,  -- area
                        50,123, 52,125, 50,127)  -- mast
  )
);

INSERT INTO map VALUES(
  'e1-e2',
  'electric-net',
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
  'e3',
  'electric-mast',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,4, 7,1003,4),        -- Two circle
    SDO_ORDINATE_ARRAY( 120,115, 130,125, 120,135,  -- area
                        120,123, 122,125, 120,127)  -- mast
  )
);

INSERT INTO map VALUES(
  'e2-e3',
  'electric-net',
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
  'e4',
  'electric-mast',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,4, 7,1003,4),     -- Two circle
    SDO_ORDINATE_ARRAY( 135,70, 145,80, 135,90,  -- area
                        135,78, 137,80, 135,82)  -- mast
  )
);

INSERT INTO map VALUES(
  'e3-e4',
  'electric-net',
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
  'e5',
  'electric-mast',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,4, 7,1003,4),     -- Two circle
    SDO_ORDINATE_ARRAY( 185,30, 195,40, 185,50,  -- area
                        185,38, 187,40, 185,42)  -- mast
  )
);

INSERT INTO map VALUES(
  'e4-e5',
  'electric-net',
  SDO_GEOMETRY(
    2002,
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,2,1), -- compound line string
    SDO_ORDINATE_ARRAY(135,80, 185,40)
  )
);

------------------- Gas Station -------------------------
-------------------- p1 p2 ------------------------------

INSERT INTO map VALUES(
  'g1',
  'gas-station',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,4, 7,1003,4),  -- Two circle
    SDO_ORDINATE_ARRAY( 60,10, 75,25, 60,40,  -- area
                        60,20, 65,25, 60,30)  -- mast
  )
);

INSERT INTO map VALUES(
  'g2',
  'gas-station',
  SDO_GEOMETRY(
    2003,  -- 2-dimensional polygon
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,1003,4, 7,1003,4),        -- Two circle
    SDO_ORDINATE_ARRAY( 100,160, 115,175, 100,190,  -- area
                        100,170, 105,175, 100,180)  -- mast
  )
);

------------------- Gas pipeline --------------------------

INSERT INTO map VALUES (
  'g1-g2',
  'gas-pipeline',
   SDO_GEOMETRY (
     2006,
     NULL,
     NULL,
     SDO_ELEM_INFO_ARRAY(1,4,2 , 1,2,1, 3,2,1),
     SDO_ORDINATE_ARRAY(60, 25, 100,25,
                        100,25, 100,175))
);

INSERT INTO map VALUES(
  'g2-gxl', -- no name left
  'gas-pipeline',
  SDO_GEOMETRY(
    2002,
    NULL,
    NULL,
    SDO_ELEM_INFO_ARRAY(1,2,1), -- compound line string
    SDO_ORDINATE_ARRAY(100,175, 1,163)
  )
);

INSERT INTO map VALUES(
  'g2-gxr', -- no name right
  'gas-pipeline',
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
	'area1', 'builds',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
    SDO_ELEM_INFO_ARRAY(1,1003,1, 15,2003,1, 25,2003,1), -- exterior rectangle (left-bottom, right-top) with holes
		SDO_ORDINATE_ARRAY(
                    145,175, 145,140, 160,120, 160,85, 190,85, 190,175, 145,175,  -- exterior rectangle, start=1
                    150,170, 150,145, 185,145, 185,170, 150,170,                  -- interior rectangle, start=15
                    165, 135, 165,90, 180,90,  180,135, 165,135                   -- interior rectangle, start=25
		)
	)
);

INSERT INTO map VALUES (
	'area1', 'build1',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
    SDO_ELEM_INFO_ARRAY(1,2003,1), -- interior rectangle
		SDO_ORDINATE_ARRAY(150,170, 150,145, 185,145, 185,170, 150,170)
	)
);

INSERT INTO map VALUES (
	'area1', 'build2',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
    SDO_ELEM_INFO_ARRAY(1,2003,1), -- interior rectangle
		SDO_ORDINATE_ARRAY(165, 135, 165,90, 180,90,  180,135, 165,135)
	)
);


INSERT INTO map VALUES (
	'area2', 'builds',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
    SDO_ELEM_INFO_ARRAY(1,1003,1, 11,2003,1), -- exterior rectangle
		SDO_ORDINATE_ARRAY(
            20,175, 20, 140, 115,140, 115,175, 20,175,  -- exterior rectangle
            25,170, 25,145, 70,145, 70,170, 25,170      -- interior rectangle
        )
	)
);

INSERT INTO map VALUES (
	'area2', 'build1',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
    SDO_ELEM_INFO_ARRAY(1,2003,1), -- interior rectangle (left-bottom, right-top) with holes
		SDO_ORDINATE_ARRAY(25,170, 25,145, 70,145, 70,170, 25,170)
	)
);

INSERT INTO map VALUES (
	'area3', 'builds',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
    SDO_ELEM_INFO_ARRAY(1,1003,1, 15,2003,1, 29,2003,1),
		SDO_ORDINATE_ARRAY(
            30,120, 30,1, 70,1, 70,55, 95,55,  95,120, 30,120,   -- exterior rectangle
            35,115, 35,55, 60,55, 60,70,  80,70, 80,115, 35,115,
            35,40, 35,10, 50,10, 50,40, 35,40
        )
	)
);

INSERT INTO map VALUES (
	'area3', 'build1',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
    SDO_ELEM_INFO_ARRAY(1,2003,1), -- interior rectangle
		SDO_ORDINATE_ARRAY(35,115, 35,55, 60,55, 60,70,  80,70, 80,115, 35,115)
	)
);

INSERT INTO map VALUES (
	'area3', 'build2',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
    SDO_ELEM_INFO_ARRAY(1,2003,1), -- interior rectangle
		SDO_ORDINATE_ARRAY(35,40, 35,10, 50,10, 50,40, 35,40)
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
