CREATE INDEX map_geometry_sidx ON map(geometry) INDEXTYPE IS MDSYS.SPATIAL_INDEX;

Create procedure EstatesElectricArea
AS
  SELECT estate.name, estate.type building
  FROM map estate, map earea
  WHERE  (estate.type = 'estate') AND (earea.COtype = 'electric-area') AND
  	(SDO_RELATE(estate.geometry, earea.geometry, 'mask=ANYINTERACT') = 'TRUE')
  ORDER BY estate.name ASC;
GO;

Create procedure EstatesGasMast
AS
  SELECT estate.name, estate.type building
  FROM map estate, map earea
  WHERE  (estate.type = 'estate') AND (earea.type = 'gas-mast') AND
  	(SDO_RELATE(estate.geometry, earea.geometry, 'mask=ANYINTERACT') = 'TRUE')
  ORDER BY estate.name ASC;
GO;

Create procedure EstatesGasArea
AS
  SELECT estate.name, estate.type building
  FROM map estate, map earea
  WHERE  (estate.type = 'estate') AND (earea.type = 'gas-area') AND
  	(SDO_RELATE(estate.geometry, earea.geometry, 'mask=ANYINTERACT') = 'TRUE')
  ORDER BY estate.name ASC;
GO;

create procedure BuildsInGasArea
AS
  SELECT build.name, build.type building
  FROM map build, map earea
  WHERE  (build.type = 'build') AND (earea.type = 'gas-area') AND
  	(SDO_RELATE(build.geometry, earea.geometry, 'mask=ANYINTERACT') = 'TRUE')
  ORDER BY build.name ASC;
GO;
