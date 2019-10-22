drop schema slotmachine cascade

create schema if not exists slotmachine;

create table if not exists slotmachine.country(
country_name varchar(255) unique,
primary key(country_name)
);

create table if not exists slotmachine.city(
city_name varchar(255) unique,
country varchar(255) not null,
utc_offset varchar(5) not null,
primary key(city_name, country),
foreign key (country) references country(country_name)
);

create table if not exists slotmachine.airline(
airline_name varchar(255) not null,
airline_alias varchar(3) unique,
airline_country varchar(255) not null,
primary key(airline_alias),
foreign key (airline_country) references country(country_name)
);

create table if not exists slotmachine.airport(
airport_name varchar(255) not null,
airport_alias varchar(3) unique,
airport_city varchar(255) not null,
airport_country varchar(255) not null,
primary key(airport_alias),
foreign key (airport_city, airport_country) references city(city_name, country)
);

create table if not exists slotmachine.flight(
flight_id serial not null,
departureAirport varchar(3),
destinationAirport varchar(3),
airline varchar(3),
departureTime timestamp,
destinationTime timestamp,
primary key(flight_id),
foreign key (departureAirport) references airport(airport_alias),
foreign key (destinationAirport) references airport(airport_alias),
foreign key (airline) references airline(airline_alias)
);

create table if not exists slotmachine.proposal(
auction_id serial not null,
flight_id integer not null,
price real not null,
bid bool not null,
ask bool not null,
initialTime timestamp not null,
desiredTime timestamp not null,
primary key (auction_id),
foreign key (flight_id) references flight(flight_id),
check (bid !=ask)
);

create table if not exists slotmachine.costfunction(
cf_name varchar (255),
t1 real not null,
t2 real not null,
t3 real not null,
t4 real not null,
t5 real not null,
t6 real not null,
primary key(cf_name)
);

create table if not exists slotmachine.user(
username varchar (255),
pw varchar (255),
role varchar(10) not null,
primary key (username),
check (role in ('admin', 'nwmgmt', 'airline'))
);