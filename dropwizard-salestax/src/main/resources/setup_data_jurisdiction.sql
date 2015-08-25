-- states
INSERT INTO salestax.jurisdiction(fips, state, name, abbreviation)
  VALUES('06', '06', 'california', 'ca');

-- counties
INSERT INTO salestax.jurisdiction(fips, state, county, name, abbreviation)
  VALUES('06073', '06', '073', 'san diego', 'ca');

-- cities, pretent cities only exist in one county for sake of example
INSERT INTO salestax.jurisdiction(fips, state, county, city, name, abbreviation)
  VALUES('0607353322', '06', '073', '53322', 'california', 'ca');