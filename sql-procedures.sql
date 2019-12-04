DROP INDEX map_geometry_sidx;
CREATE INDEX map_geometry_sidx ON map(geometry) INDEXTYPE IS MDSYS.SPATIAL_INDEX;

create or replace procedure EstatesElectricArea
AS
map_names VARCHAR2(32);
map_types VARCHAR2(32);
BEGIN
  SELECT estate.name,estate.type into map_names, map_types from map estate, map earea
  WHERE  (estate.type = 'estate') AND (earea.type = 'electric-area') AND
		(SDO_RELATE(estate.geometry, earea.geometry, 'mask=ANYINTERACT') = 'TRUE')
        ORDER BY estate.name ASC;
END;

Create procedure EstatesGasMast
AS
map_names VARCHAR2(32);
map_types VARCHAR2(32);
BEGIN
  SELECT estate.name,estate.type into map_names, map_types
  FROM map estate, map earea
  WHERE  (estate.type = 'estate') AND (earea.type = 'gas-mast') AND
  	(SDO_RELATE(estate.geometry, earea.geometry, 'mask=ANYINTERACT') = 'TRUE')
  ORDER BY estate.name ASC;
END;

Create procedure EstatesGasArea
AS
map_names VARCHAR2(32);
map_types VARCHAR2(32);
BEGIN
  SELECT estate.name, estate.type into map_names, map_types
  FROM map estate, map earea
  WHERE  (estate.type = 'estate') AND (earea.type = 'gas-area') AND
  	(SDO_RELATE(estate.geometry, earea.geometry, 'mask=ANYINTERACT') = 'TRUE')
  ORDER BY estate.name ASC;
END;

create or replace procedure EstatesGasArea
AS
map_names VARCHAR2(32);
map_types VARCHAR2(32);
BEGIN
  SELECT estate.name,estate.type into map_names, map_types from map estate, map earea
  WHERE  (estate.type = 'estate') AND (earea.type = 'gas-area') AND
		(SDO_RELATE(estate.geometry, earea.geometry, 'mask=ANYINTERACT') = 'TRUE')
        ORDER BY estate.name ASC;
END;
