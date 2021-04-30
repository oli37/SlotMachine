CREATE MATERIALIZED VIEW IF NOT EXISTS slotmachine.flight_view AS
SELECT DISTINCT f.flight_id, f.departuretime, f.destinationtime,
            al.airline_name, al.airline_alias, al.airline_country,
            ap_des.airport_name AS des_airport_name, ap_des.airport_alias AS des_airport_alias,
            ap_des.airport_city AS des_airport_city, ap_des.airport_country AS des_airport_country,
            des_city.utc_offset AS des_airport_utcoffset,
            ap_dep.airport_name AS dep_airport_name, ap_dep.airport_alias AS dep_airport_alias,
            ap_dep.airport_city AS dep_airport_city, ap_dep.airport_country AS dep_airport_country,
            dep_city.utc_offset AS dep_airport_utcoffset
            FROM slotmachine.flight f
            LEFT OUTER JOIN slotmachine.airline al ON f.airline = al.airline_alias
            LEFT OUTER JOIN slotmachine.airport ap_des ON f.destinationairport = ap_des.airport_alias
            LEFT OUTER JOIN slotmachine.airport ap_dep ON f.departureairport = ap_dep.airport_alias
            LEFT OUTER JOIN slotmachine.city dep_city ON ap_dep.airport_city = dep_city.city_name
            LEFT OUTER JOIN slotmachine.city des_city ON ap_des.airport_city = des_city.city_name;

CREATE MATERIALIZED VIEW IF NOT EXISTS slotmachine.flights_per_timeslot_view AS
SELECT COUNT(*) cnt, to_timestamp(floor((extract('epoch' FROM slotmachine.flight.departuretime) / 900 )) * 900)
            AT TIME ZONE 'UTC' as interval_alias
            FROM slotmachine.flight GROUP BY interval_alias
            ORDER BY interval_alias;