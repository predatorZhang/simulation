CREATE OR REPLACE TRIGGER TRI_SM_FLOW
BEFORE INSERT ON SM_FLOW
FOR EACH ROW

when (NEW.DBID IS NULL)
BEGIN
    SELECT SEQ_SM_FLOW_ID.NEXTVAL INTO:NEW.DBID FROM DUAL;
END;