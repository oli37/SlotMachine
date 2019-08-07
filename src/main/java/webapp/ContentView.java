package webapp;

import application.Airline;
import application.Airport;
import application.Flight;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import db.DbManager;

import java.util.ArrayList;
import java.util.List;

public class ContentView extends HorizontalLayout {


    public void drawDashboard() {
        removeAll();
        add(new H6("DASHBOARD"));

    }

    public void drawFlights() {
        removeAll();
        DbManager dbm = new DbManager();
        List<Flight> flist = dbm.getAllFlights();

        List<Airline> airlines = dbm.getAllAirlines();
        List<String> airlineNames = new ArrayList<>();

        List<Airport> airports = dbm.getAllAirports();
        List<String> airportNames = new ArrayList<>();

        for (Airline al : airlines) airlineNames.add(al.getAlias());
        for (Airport ap : airports) airportNames.add(ap.getAlias());

        //Two nested Views
        VerticalLayout flightGrid = new VerticalLayout();
        VerticalLayout newFlight = new VerticalLayout();


        //Table of flights
        flightGrid.add(new H6("FLIGHTS"));
        Grid<Flight> grid = new Grid<>();
        grid.setItems(flist);
        grid.addColumn(flight -> flight.getAirline().getName()).setHeader("Airline");
        grid.addColumn(flight -> flight.getDepartureAirport().getName()).setHeader("Departure Airport");
        grid.addColumn(flight -> flight.getDepartureTime()).setHeader("Departure Time");
        grid.addColumn(flight -> flight.getDestinationAirport().getName()).setHeader("Destination Airport");
        grid.addColumn(flight -> flight.getDestinationTime()).setHeader("Departure Airport");
        grid.addColumn(flight -> flight.getFlightTime()).setHeader("Flight Time");

        flightGrid.setWidth("175%");
        flightGrid.add(grid);

        //Adding new Flight
        newFlight.add(new H6("ADD NEW FLIGHT"));
        Select<String> airlineSelect = new Select<>();
        airlineSelect.setLabel("Airline");
        airlineSelect.setItems(airlineNames);
        airlineSelect.setWidthFull();

        Select<String> departureAirportSelect = new Select<>();
        departureAirportSelect.setLabel("Departure Airport");
        departureAirportSelect.setItems(airportNames);
        departureAirportSelect.setWidthFull();

        Select<String> destinationAirportSelect = new Select<>();
        destinationAirportSelect.setLabel("Destination Airport");
        destinationAirportSelect.setItems(airportNames);
        destinationAirportSelect.setWidthFull();

        DatePicker departureDatePicker = new DatePicker();
        departureDatePicker.setLabel("Departure Date");
        departureDatePicker.setWidthFull();

        DatePicker destinationDatePicker = new DatePicker();
        destinationDatePicker.setLabel("Destination Date");
        destinationDatePicker.setWidthFull();

        Button commitButton = new Button("+");
        commitButton.getStyle().set("background-color", "#E74C3C");
        commitButton.getStyle().set("color", "white");
        commitButton.setWidthFull();
        commitButton.getStyle().set("margin-top", "50px");


        newFlight.setWidth("35%");
        newFlight.getStyle().set("margin-right", "25px");
        newFlight.add(airlineSelect);
        newFlight.add(departureAirportSelect);
        newFlight.add(destinationAirportSelect);
        newFlight.add(departureDatePicker);
        newFlight.add(destinationDatePicker);
        newFlight.add(commitButton);


        add(flightGrid);
        add(newFlight);

    }

    public void drawSchedule() {
        removeAll();
        VerticalLayout schedule = new VerticalLayout();
        schedule.add(new H6("SCHEDULE"));


        add(schedule);
    }

    public void drawAuction() {
        removeAll();
        add(new H6("AUCTION"));

    }


}
