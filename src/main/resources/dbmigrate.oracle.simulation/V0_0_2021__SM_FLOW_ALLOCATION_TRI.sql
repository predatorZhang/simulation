CREATE OR REPLACE TRIGGER TRI_SM_FLOW_ALLOCATION
BEFORE INSERT ON SM_FLOW_ALLOCATION
FOR EACH ROW

when (NEW.DBID IS NULL)
BEGIN
    SELECT SEQ_SM_FLOW_ALLOCATION_ID.NEXTVAL INTO:NEW.DBID FROM DUAL;
END;