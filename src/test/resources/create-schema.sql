-- noinspection SqlDialectInspectionForFile

CREATE TABLE IF NOT EXISTS CLIENTS (ID INT PRIMARY KEY,"LIMIT" INT,BALANCE INT);

CREATE TABLE IF NOT EXISTS TRANSACTIONS (ID INT PRIMARY KEY,
                           CLIENT_ID INT,
                           MONEY INT,
                           type CHAR(1) CHECK (type IN ('d', 'c')),
                           DESCRIPTION VARCHAR(10),
                           CREATEDON DATE,
                           FOREIGN KEY (CLIENT_ID) REFERENCES CLIENTS(ID));

MERGE INTO CLIENTS
KEY(ID)
VALUES (1, 1000000,0),
       (2, 800000,0),
       (3, 10000000,0),
       (4, 100000000,0),
       (5, 5000000,0);
