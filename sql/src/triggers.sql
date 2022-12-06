--Trigger for order numbers and dates
DROP SEQUENCE IF EXISTS orders_orderNumber_seq CASCADE;
CREATE SEQUENCE orders_orderNumber_seq START WITH 501;

CREATE OR REPLACE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION set_new_order_info() 
RETURNS "trigger" AS 
$BODY$
BEGIN
    NEW.orderNumber = nextval('orders_orderNumber_seq');
    NEW.orderTime = now()::timestamp(0);
    RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

DROP TRIGGER IF EXISTS order_trigger ON Orders;
CREATE TRIGGER order_trigger
BEFORE INSERT
ON Orders
FOR EACH ROW
EXECUTE PROCEDURE set_new_order_info();

--Trigger for new user IDs
DROP SEQUENCE IF EXISTS users_userID_seq CASCADE;
CREATE SEQUENCE users_userID_seq START WITH 101;

CREATE OR REPLACE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION set_new_user_id() 
RETURNS "trigger" AS 
$BODY$
BEGIN
    NEW.userID = nextval('users_userID_seq');
    RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

DROP TRIGGER IF EXISTS user_id_trigger ON Users;
CREATE TRIGGER user_id_trigger
BEFORE INSERT
ON Users
FOR EACH ROW
EXECUTE PROCEDURE set_new_user_id();

--Trigger for new product updates
DROP SEQUENCE IF EXISTS productupdates_updateNumber_seq CASCADE;
CREATE SEQUENCE productupdates_updateNumber_seq START WITH 51;

CREATE OR REPLACE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION set_new_product_update_number()
RETURNS "trigger" AS
$BODY$
BEGIN
    NEW.updateNumber = nextval('productupdates_updateNumber_seq');
    NEW.updatedOn = now()::timestamp(0);
    RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

DROP TRIGGER IF EXISTS product_update_number_trigger ON ProductUpdates;
CREATE TRIGGER product_update_number_trigger
BEFORE INSERT
ON ProductUpdates
FOR EACH ROW
EXECUTE PROCEDURE set_new_product_update_number();

--Trigger for product request
DROP SEQUENCE IF EXISTS productsupplyrequests_requestNumber_seq CASCADE;
CREATE SEQUENCE productsupplyrequests_requestNumber_seq START WITH 11;

CREATE OR REPLACE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION set_new_product_request_number()
RETURNS "trigger" AS
$BODY$
BEGIN
    NEW.requestNumber = nextval('productsupplyrequests_requestNumber_seq');
    RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

DROP TRIGGER IF EXISTS product_request_number_trigger ON ProductSupplyRequests;
CREATE TRIGGER product_request_number_trigger
BEFORE INSERT
ON ProductSupplyRequests
FOR EACH ROW
EXECUTE PROCEDURE set_new_product_request_number();