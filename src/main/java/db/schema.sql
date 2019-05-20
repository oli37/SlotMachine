
create schema if not exists slotmachine;

create table slotmachine.country(
country_id serial,
country_name varchar(255) not null unique,
primary key(country_name)
);

create table slotmachine.city(
city_id serial,
city_name varchar(255) not null,
country varchar(255) not null,
timezoneRegion varchar(255),
primary key(city_name, country),
foreign key (country) references slotmachine.country(country_name)
);

create table slotmachine.airline(
airline_id serial not null,
airline_name varchar(255) not null,
airline_alias varchar(3) not null unique,
airline_country varchar(255) not null,
primary key(airline_alias),
foreign key (airline_country) references slotmachine.country(country_name)
);

create table slotmachine.airport(
airport_id serial not null,
airport_name varchar(255) not null,
airport_alias varchar(3) not null unique,
airport_city varchar(255) not null,
airport_country varchar(255) not null,
primary key(airport_alias),
foreign key (airport_city, airport_country) references slotmachine.city(city_name, country)
);

create table slotmachine.flight(
flight_id serial not null,
departureAirport varchar(3),
destinationAirport varchar(3),
airline varchar(3),
departureTime timestamp with time zone,
destinationTime timestamp with time zone,
primary key(flight_id),
foreign key (departureAirport) references slotmachine.airport(airport_alias),
foreign key (destinationAirport) references slotmachine.airport(airport_alias),
foreign key (airline) references slotmachine.airline(airline_alias)
);

insert into slotmachine.country (country_name) values ('Austria');
insert into slotmachine.country (country_name) values ('Sweden');

insert into slotmachine.city (city_name, country, timezoneregion) values ('Vienna',  'Austria', 'MEZ')
insert into slotmachine.city (city_name, country, timezoneregion) values ('Stockholm', 'Sweden', 'MEZ')
insert into slotmachine.airline (airline_name, airline_alias, airline_country) values ('Austrian Airlines', 'AAL', 'Austria'))
insert into slotmachine.airport (airport_name, airport_alias, airport_city, airport_country) values ('Vienna Airport', 'WWW',  'Vienna', 'Austria')
insert into slotmachine.airport (airport_name, airport_alias, airport_city, airport_country) values ('Stockholm Airport', 'STH', 'Stockholm', 'Sweden')


insert into slotmachine.flight (departureAirport, destinationAirport, airline, departureTime, destinationTime) values (
(select airport_alias from slotmachine.airport where airport_name='Vienna Airport'),
(select airport_alias from slotmachine.airport where airport_name='Stockholm Airport'),
(select airline_alias from slotmachine.airline where airline_name='Austrian Airlines'),
'2004-10-19 10:23:54+02',
'2004-10-19 10:23:54+02'
)
