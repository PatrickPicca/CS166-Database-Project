--Trigger for order numbers and dates
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