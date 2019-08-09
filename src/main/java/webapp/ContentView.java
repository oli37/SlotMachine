package webapp;

import application.Airline;
import application.Airport;
import application.Flight;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import db.AirlineServiceProvider;
import db.AirportServiceProvider;
import db.DbManager;
import db.FlightServceProvider;

import java.util.List;
import java.util.stream.Collectors;

public class ContentView extends HorizontalLayout {


    public void drawDashboard() {
        removeAll();
        add(new H6("DASHBOARD"));

        AirlineServiceProvider alsp = new AirlineServiceProvider();

        ComboBox<String> cb = new ComboBox<>();
        cb.setItems(alsp.fetch(0, 0).stream().map(Airline::getAlias));
        add(cb);
    }

    public void drawFlights() {
        removeAll();

        DbManager dbManager = new DbManager();
        var flist = dbManager.getAllFlights();

        //Two nested Views
        VerticalLayout flightGrid = new VerticalLayout();
        VerticalLayout newFlight = new VerticalLayout();

        //Table of flights
        flightGrid.add(new H6("FLIGHTS"));

        FlightServceProvider flsp = new FlightServceProvider();
        DataProvider<Flight, Void> dataProviderFLight = DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    return flsp.fetch(offset, limit).stream();
                },
                query -> flsp.getCount());


        Grid<Flight> grid = new Grid<>();
        grid.addColumn(flight -> flight.getAirline().getName()).setHeader("Airline");
        grid.addColumn(flight -> flight.getDepartureAirport().getName()).setHeader("Departure Airport");
        grid.addColumn(flight -> flight.getDepartureTime()).setHeader("Departure Time");
        grid.addColumn(flight -> flight.getDestinationAirport().getName()).setHeader("Destination Airport");
        grid.addColumn(flight -> flight.getDestinationTime()).setHeader("Departure Airport");
        grid.addColumn(flight -> flight.getFlightTime()).setHeader("Flight Time");
        grid.setDataProvider(dataProviderFLight);

        flightGrid.setWidth("175%");
        flightGrid.add(grid);


        //Load available airlines and airports
        AirlineServiceProvider alsp = new AirlineServiceProvider();
        AirportServiceProvider apsp = new AirportServiceProvider();

        List<String> airlines = alsp.fetch(0, 0).stream().map(Airline::getAlias).collect(Collectors.toList());
        List<String> airports = apsp.fetch(0, 0).stream().map(Airport::getAlias).collect(Collectors.toList());

        //Adding new Flight
        newFlight.add(new H6("ADD NEW FLIGHT"));

        ComboBox<String> airlineSelect = new ComboBox<>();
        airlineSelect.setLabel("Airline");
        airlineSelect.setItems(airlines);
        airlineSelect.setWidthFull();

        ComboBox<String> departureAirportSelect = new ComboBox<>();
        departureAirportSelect.setLabel("Departure Airport");
        departureAirportSelect.setItems(airports);
        departureAirportSelect.setWidthFull();

        ComboBox<String> destinationAirportSelect = new ComboBox<>();
        destinationAirportSelect.setLabel("Destination Airport");
        destinationAirportSelect.setItems(airports);
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
