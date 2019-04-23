CREATE TABLE SORTIMENT (
  ID SERIAL PRIMARY KEY,
  PRICE FLOAT,
  QUANTITY INTEGER,
  SHOP_ID INTEGER REFERENCES SHOP(ID),
  PRODUCT_ID INTEGER REFERENCES PRODUCT(ID)
);