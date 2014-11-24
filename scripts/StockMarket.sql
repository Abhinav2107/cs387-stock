drop table ownership;
drop table sellOrders;
drop table buyOrders;
drop table transactions;
drop table users;
drop table stocks;
drop table company;

create table users
(
	username varchar(20) primary key,
	password varchar(50) not null,
	name varchar(50),
	email varchar(50) check (email like '%@%'),
	phoneNumber numeric(10,0),    
	address varchar(100),
	balance numeric(10,2) check (balance>0)
);

create table stocks
(
	stockSymbol char(8) primary key,
	companyName varchar(100) unique,
	password varchar(50) not null,
	ltp numeric (8,2) check (ltp>0)
);

create table ownership
(
	username varchar(20) references users,
	stockSymbol char(8) references stocks,
	quantity numeric(8,0) check (quantity>0),
	salesPrice numeric (8,2) check (salesPrice>0),
	primary key(username, stockSymbol)
);

create table sellOrders
(
	stockSymbol char(8) references stocks not null,
	username varchar(20) references users not null,
	askPrice numeric(8, 2) not null,
	quantity numeric(4) not null check (quantity >0),
	sellDateTime timestamp
);

CREATE INDEX sort_sell
ON sellOrders (askPrice, sellDateTime);

create table buyOrders
(
	stockSymbol char(8) references stocks not null,
	username varchar(20) references users not null,
	bidPrice numeric(8, 2) not null,
	quantity numeric(4) not null check (quantity >0),
	buyDateTime timestamp
);

CREATE INDEX sort_buy
ON buyOrders (bidPrice, buyDateTime);

create table transactions
(
	stockSymbol char(8) references stocks(stockSymbol),
	tradedPrice numeric(8,2) not null, 
	quantity numeric(4) not null check (quantity >0),
	seller varchar(20) references users(username), 
	buyer varchar(20) references users(username), 
	transDateTime timestamp
);
