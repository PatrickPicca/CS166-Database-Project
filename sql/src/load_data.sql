COPY Users
FROM '/extra/tlian020/VirtualBox VMs/CS166-Database-Project-Phase-3/data/users.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE users_userID_seq RESTART 100;

COPY Store
FROM '/extra/tlian020/VirtualBox VMs/CS166-Database-Project-Phase-3/data/stores.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Product
FROM '/extra/tlian020/VirtualBox VMs/CS166-Database-Project-Phase-3/data/products.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Warehouse
FROM '/extra/tlian020/VirtualBox VMs/CS166-Database-Project-Phase-3/data/warehouse.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Orders
FROM '/extra/tlian020/VirtualBox VMs/CS166-Database-Project-Phase-3/data/orders.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE orders_orderNumber_seq RESTART 500;


COPY ProductSupplyRequests
FROM '/extra/tlian020/VirtualBox VMs/CS166-Database-Project-Phase-3/data/productSupplyRequests.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE productsupplyrequests_requestNumber_seq RESTART 10;

COPY ProductUpdates
FROM '/extra/tlian020/VirtualBox VMs/CS166-Database-Project-Phase-3/data/productUpdates.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE productupdates_updateNumber_seq RESTART 50;
