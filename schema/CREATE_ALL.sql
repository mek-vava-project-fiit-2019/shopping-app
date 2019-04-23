CREATE TABLE SHOP (
  ID SERIAL PRIMARY KEY,
  NAME VARCHAR(30),
  LONGITUDE FLOAT,
  LATITUDE FLOAT
);

CREATE TABLE PRODUCT (
  ID SERIAL PRIMARY KEY,
  NAME VARCHAR(30),
  CATEGORY VARCHAR(30),
  IMAGE VARCHAR(30)
);

CREATE TABLE SORTIMENT (
  ID SERIAL PRIMARY KEY,
  PRICE FLOAT,
  QUANTITY INTEGER,
  SHOP_ID INTEGER REFERENCES SHOP(ID),
  PRODUCT_ID INTEGER REFERENCES PRODUCT(ID)
);

CREATE TABLE USERS (
  ID SERIAL PRIMARY KEY,
  NAME VARCHAR(30)
);

CREATE TABLE BASKET (
  ID SERIAL PRIMARY KEY,
  QUANTITY INTEGER,
  USER_ID INTEGER REFERENCES USERS(ID),
  PRODUCT_ID INTEGER REFERENCES PRODUCT(ID)
);