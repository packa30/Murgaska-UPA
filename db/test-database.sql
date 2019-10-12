DROP table Students;

CREATE TABLE Students(
    id int not null,
    login varchar(10) not null,
    fullName varchar(25) not null,
    Primary key (id)
);

insert into Students values (1,'xgrofc00','Grofcik Peter');
insert into Students values (2,'xkrajc17','Krajc Patrik');
insert into Students values (3,'xnocia00','Nociar Marian');

commit;

select * from Students;