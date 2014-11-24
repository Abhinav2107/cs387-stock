delete from ownership;
delete from buyOrders;
delete from sellOrders;
delete from transactions;
delete from stocks;
delete from users;
delete from company;

insert into users values ('aman', 'pass1', 'Aman Gour', 'ag@gmail.com', null, null, '1000');
insert into users values ('abhinav', 'pass2', 'Abhinav Gupta', 'ag@gmail.com', null, null, '1000');
insert into users values ('ainesh', 'pass3', 'Ainesh Pandey', 'ap@gmail.com', null, null, '1000');
insert into users values ('bharat', 'bharat', 'Bharat Radhakrishan', 'br@gmail.com', null, null, '1500');

insert into company values ('google', 'Google.co', 'googleUser', 'asd', '9167941432', 'Mountain View, CA', 'google@gmail.com', 'googl');
insert into company values ('yahoo', 'Yahoo.co', 'yahooUser', 'asd', '8392849302', 'Somewhere, CA', 'yahoo@yahoo.com', 'yahoo');
insert into company values ('micrsoft', 'Microsoft.co', 'microsoftUser', 'asd', '3819349302', 'Somewhere, CA', 'microsot@microsoft.com', 'mcrsf');
insert into company values ('flipkart', 'Flipkart.co', 'flipkartUser', 'asd', '8338104822', 'Delhi, India', 'flipkart@flipkart.com', 'flpkt');
insert into company values ('nike', 'Nike.co', 'nikeUser', 'asd', '3910491022', 'New York, NY', 'nike@nike.com', 'nikes');

insert into stocks values ('googl', 'Google.co', '30', 'google');
insert into stocks values ('yahoo', 'Yahoo.co', '15', 'yahoo');
insert into stocks values ('mcrsf', 'Microsoft.co', '20', 'micrsoft');
insert into stocks values ('flpkt', 'Flipkart.co', '10', 'flipkart');
insert into stocks values ('nikes', 'Nike.co', '20', 'nike');

insert into ownership values ('aman', 'googl', '3', '30');
insert into ownership values ('aman', 'flpkt', '5', '10');
insert into ownership values ('ainesh', 'googl', '4', '30');
insert into ownership values ('ainesh', 'yahoo', '5', '15');
insert into ownership values ('ainesh', 'nikes', '10', '20');
insert into ownership values ('abhinav', 'yahoo', '3', '15');
insert into ownership values ('abhinav', 'googl', '1', '30');
insert into ownership values ('abhinav', 'mcrsf', '2', '20');
insert into ownership values ('bharat', 'googl', '5', '30');
insert into ownership values ('bharat', 'yahoo', '5', '15');

insert into sellOrders values ('googl', 'aman', '30','1');
insert into sellOrders values ('yahoo', 'ainesh', '25', '3');

insert into buyOrders values ('googl', 'bharat', '25', '4', '2014-01-20 05:38:43.25');
insert into buyOrders values ('yahoo', 'ainesh', '10', '2', '2014-06-18 07:47:30.693');
insert into buyOrders values ('flpkt', 'ainesh', '8', '7', '2014-06-18 07:57:02.931');
insert into buyOrders values ('yahoo', 'abhinav', '10', '1', '2014-01-15 01:41:18.8');
insert into buyOrders values ('nikes', 'aman', '16', '2', '2014-06-18 08:01:48.418');
insert into buyOrders values ('nikes', 'abhinav', '18', '6', '2014-08-23 08:32:57.377');
insert into buyOrders values ('mcrsf', 'bharat', '16', '8', '2014-05-22 21:37:04.454');
insert into buyOrders values ('googl', 'ainesh', '25', '2', '2014-06-18 07:53:33.344');
insert into buyOrders values ('flpkt', 'bharat', '8', '1', '2014-02-17 14:43:04.649');
insert into buyOrders values ('googl', 'aman', '25', '6', '2014-04-12 14:45:06.477');

insert into transactions values ('googl', '29.00', '1', 'aman', 'abhinav', '2014-01-20 05:38:43.25');
insert into transactions values ('flpkt', '10.00', '1', 'aman', 'ainesh', '2014-01-15 01:41:18.8');
insert into transactions values ('googl', '29.00', '1', 'ainesh', 'abhinav', '2014-02-17 14:43:04.649');
insert into transactions values ('nikes', '20.00', '1', 'bharat', 'abhinav', '2014-04-12 14:45:06.477');
insert into transactions values ('googl', '29.00', '1', 'ainesh', 'abhinav', '2014-04-23 15:07:06.166');
insert into transactions values ('yahoo', '29.00', '1', 'aman', 'bharat', '2014-05-22 21:37:04.454');
insert into transactions values ('mcrsf', '20.00', '1', 'ainesh', 'aman', '2014-06-18 07:47:30.693');
insert into transactions values ('flpkt', '12.00', '1', 'abhinav', 'aman', '2014-06-18 07:53:33.344');
insert into transactions values ('googl', '35.00', '1', 'abhinav', 'ainesh', '2014-06-18 07:57:02.931');
insert into transactions values ('mcrsf', '20.00', '1', 'aman', 'bharat', '2014-06-18 08:01:48.418');
insert into transactions values ('yahoo', '30.00', '2', 'ainesh', 'abhinav', '2014-08-23 08:32:57.377');
insert into transactions values ('googl', '28.00', '1', 'abhinav', 'bharat', '2014-10-01 10:50:02.471');
insert into transactions values('nikes', '20.00', '1', 'aman', 'ainesh', '2014-11-05 11:15:24.951');
insert into transactions values('yahoo', '32.00', '1', 'aman', 'abhinav', '2014-11-13 11:23:56.365');
