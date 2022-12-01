--Trigger for order numbers and dates
DROP SEQUENCE IF EXISTS order_number_seq;
CREATE SEQUENCE order_number_seq START WITH 500;

CREATE OR REPLACE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION set_new_order_info() 
RETURNS "trigger" AS 
$BODY$
BEGIN
    NEW.orderNumber = nextval('order_number_seq');
    NEW.orderTime = CURRENT_TIMESTAMP::timestamp;
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
DROP SEQUENCE IF EXISTS user_id_seq;
CREATE SEQUENCE user_id_seq START WITH 100;

CREATE OR REPLACE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION set_new_user_id() 
RETURNS "trigger" AS 
$BODY$
BEGIN
    NEW.userID = nextval('user_id_seq');
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