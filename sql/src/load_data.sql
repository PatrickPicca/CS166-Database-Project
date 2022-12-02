COPY Users
FROM '/extra/wpicc001/CS166-Database-Project-Phase-3/data/users.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE users_userID_seq RESTART 101;

COPY Store
FROM '/extra/wpicc001/CS166-Database-Project-Phase-3/data/stores.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Product
FROM '/extra/wpicc001/CS166-Database-Project-Phase-3/data/products.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Warehouse
FROM '/extra/wpicc001/CS166-Database-Project-Phase-3/data/warehouse.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Orders
FROM '/extra/wpicc001/CS166-Database-Project-Phase-3/data/orders.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE orders_orderNumber_seq RESTART 501;


COPY ProductSupplyRequests
FROM '/extra/wpicc001/CS166-Database-Project-Phase-3/data/productSupplyRequests.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE productsupplyrequests_requestNumber_seq RESTART 11;

COPY ProductUpdates
FROM '/extra/wpicc001/CS166-Database-Project-Phase-3/data/productUpdates.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE productupdates_updateNumber_seq RESTART 51;
