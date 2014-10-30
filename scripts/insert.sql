delete from sellOrders;
delete from ownership;
delete from stocks;
delete from users;


alter table sellOrders alter sellDateTime set default now();
insert into users values ('user1', 'pass1', 'Aman Gour', 'ag@gmail.com');
insert into users values ('user2', 'pass2', 'abhinav', 'ag@gmail.com');
insert into users values ('user3', 'pass3', 'ainesh', 'ap@gmail.com');
insert into stocks values ('googl', 'google.co', 'passgoogle', '30');
insert into stocks values ('yahoo', 'yahoo.co', 'passyahoo', '15');
insert into ownership values ('user1', 'googl', '3', '30');
insert into ownership values ('user2', 'googl', '4', '30');
insert into ownership values ('user1', 'yahoo', '3', '15');

insert into sellOrders values('googl', 'user1', '30','1');
insert into sellOrders values('googl', 'user2', '28', '1');
