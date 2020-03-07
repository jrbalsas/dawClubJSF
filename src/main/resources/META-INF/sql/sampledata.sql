-- Insert sample data for Persistence Unit (important: ONE ROW per operation)

insert into Cliente (dni,nombre,socio) VALUES ('22222222-B','María López',true);
insert into Cliente (dni,nombre,socio) VALUES ('33333333-C','Samuel Aranda',false);
insert into Cliente (dni,nombre,socio) VALUES ('44444444-D','Sonia Pérez',false);
insert into Cliente (dni,nombre,socio) VALUES ('11111111-A','Manuel García',true);


-- sample password: secreto
insert into authinfo (dni,password,rolname) VALUES ('22222222-B','PBKDF2WithHmacSHA512:3072:kN6Xy8mLfmpS15I2QQ6oww2GV8ahZGZMKi8jq8CXge7mRQtItsqXl7EJ/JSEX4I/VofdPpWqLj20mgkkk4+hZw==:phiHq1GmgmNMFusGuCsarWtbiiKKkuAs+PEla7mlrmU=','ADMINISTRADORES');
insert into authinfo (dni,password,rolname) VALUES ('33333333-C','PBKDF2WithHmacSHA512:3072:kN6Xy8mLfmpS15I2QQ6oww2GV8ahZGZMKi8jq8CXge7mRQtItsqXl7EJ/JSEX4I/VofdPpWqLj20mgkkk4+hZw==:phiHq1GmgmNMFusGuCsarWtbiiKKkuAs+PEla7mlrmU=','USUARIOS');
insert into authinfo (dni,password,rolname) VALUES ('44444444-D','PBKDF2WithHmacSHA512:3072:kN6Xy8mLfmpS15I2QQ6oww2GV8ahZGZMKi8jq8CXge7mRQtItsqXl7EJ/JSEX4I/VofdPpWqLj20mgkkk4+hZw==:phiHq1GmgmNMFusGuCsarWtbiiKKkuAs+PEla7mlrmU=','ADMINISTRADORES');
insert into authinfo (dni,password,rolname) VALUES ('11111111-A','PBKDF2WithHmacSHA512:3072:kN6Xy8mLfmpS15I2QQ6oww2GV8ahZGZMKi8jq8CXge7mRQtItsqXl7EJ/JSEX4I/VofdPpWqLj20mgkkk4+hZw==:phiHq1GmgmNMFusGuCsarWtbiiKKkuAs+PEla7mlrmU=','ADMINISTRADORES');
