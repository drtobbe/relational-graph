;              
CREATE USER IF NOT EXISTS SA SALT 'e5b80e4ed7fe82f6' HASH 'e495032feda329526bb8f77045dd96487c8c87e9b49c56ddd9cfd4c8515fde64' ADMIN;            
DROP TABLE IF EXISTS PUBLIC.PERSON CASCADE;    
DROP TABLE IF EXISTS PUBLIC.FRIEND_OF CASCADE; 
CREATE CACHED TABLE PUBLIC.PERSON(
    ID INT NOT NULL,
    NAME VARCHAR(50)
);
ALTER TABLE PUBLIC.PERSON ADD CONSTRAINT PUBLIC.CONSTRAINT_8 PRIMARY KEY(ID);  
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.PERSON;   
CREATE CACHED TABLE PUBLIC.FRIEND_OF(
    PRS_ID INT,
    FRND_ID INT
);       
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.FRIEND_OF;