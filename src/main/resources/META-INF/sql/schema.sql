-- Manual tables definition, e.g. non managed tables (IMPORTANT: only ONE ROW per sentence)

--Delete previous tables
DROP TABLE authinfo;

create table if not exists authinfo ( id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), dni VARCHAR(10), password VARCHAR(512), rolname VARCHAR(50) )
