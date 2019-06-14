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
utc_offset varchar(5) not null,
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
departureTime timestamp,
destinationTime timestamp,
primary key(flight_id),
foreign key (departureAirport) references slotmachine.airport(airport_alias),
foreign key (destinationAirport) references slotmachine.airport(airport_alias),
foreign key (airline) references slotmachine.airline(airline_alias)
);

