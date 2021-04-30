--drop schema slotmachine cascade

create schema if not exists slotmachine;

create table if not exists slotmachine.country(
country_name varchar(255) unique,
primary key(country_name)
);

create table if not exists slotmachine.city(
city_name varchar(255),
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
airport_alias varchar(4) unique,
airport_name varchar(255) not null,
airport_city varchar(255) not null,
airport_country varchar(255) not null,
primary key(airport_alias),
foreign key (airport_city, airport_country) references city(city_name, country)
);

create table if not exists slotmachine.users(
username varchar (255),
pwhash varchar (255) not null,
role varchar(10) not null,
airline varchar (4),
primary key (username),
foreign key (airline) references slotmachine.airline(airline_alias),
check (role in ('ADMIN', 'NWMGMT', 'AIRLINE')),
check ((airline is not null and role in ('AIRLINE')) or (airline is null and role not in ('AIRLINE')))
);

create table if not exists slotmachine.costfunction(
cf_name varchar (255) not null,
owner varchar (4),
primary key(cf_name),
foreign key (owner) references slotmachine.airline(airline_alias)
);

create table if not exists slotmachine.flight(
flight_id serial not null,
departureAirport varchar(4),
destinationAirport varchar(4),
airline varchar(4),
departureTime timestamp,
destinationTime timestamp,
primary key(flight_id),
foreign key (departureAirport) references airport(airport_alias),
foreign key (destinationAirport) references airport(airport_alias),
foreign key (airline) references airline(airline_alias)
);

create table if not exists slotmachine.proposal(
proposal_id serial not null,
price real not null,
delay int not null,
bid bool not null,
ask bool not null,
cf varchar (255), -- must be nullable
primary key (proposal_id),
foreign key (cf) references costfunction(cf_name),
check (bid !=ask)
);

create table if not exists slotmachine.cf_flight_attr(
cf_name varchar (255),
flight_id int,
foreign key (cf_name) references costfunction(cf_name),
foreign key (flight_id) references flight(flight_id),
primary key(cf_name, flight_id)
);

create table if not exists slotmachine.hotspot(
hotspot_id serial not null,
date date,
startslot_index int,
endslot_index int,
normal_capacity int,
stressed_capacity int,
airline varchar (4) not null,
foreign key (airline) references slotmachine.airline(airline_alias),
primary key(hotspot_id),
check (startslot_index between 0 and 95),
check (endslot_index between 0 and 95),
check (endslot_index >= startslot_index),
check (normal_capacity >= stressed_capacity)
);



--should NOT WORK
--insert into slotmachine.user values ('Sepp', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'AIRLINE');
--insert into slotmachine.user values ('Lola', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'NWMGMT', 'AAA');
