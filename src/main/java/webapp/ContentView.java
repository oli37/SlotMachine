package webapp;

import application.Airline;
import application.Airport;
import application.Flight;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.provider.DataProvider;
import db.AirlineServiceProvider;
import db.AirportServiceProvider;
import db.DbManager;
import db.FlightServceProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ContentView extends HorizontalLayout {


    public void drawDashboard() {
        removeAll();
        VerticalLayout dashboard = new VerticalLayout();
        dashboard.add(new H6("DASHBOARD"));
        add(dashboard);

    }

    public void drawFlights() {
        removeAll();
        DbManager dbManager = new DbManager();
        var flist = dbManager.getAllFlights();
        AtomicReference<String> airline = new AtomicReference<>();
        AtomicReference<String> desAirport = new AtomicReference<>();
        AtomicReference<String> depAirport = new AtomicReference<>();
        AtomicReference<LocalDate> desDate = new AtomicReference<>();
        AtomicReference<LocalTime> desTime = new AtomicReference<>();
        AtomicReference<LocalDate> depDate = new AtomicReference<>();
        AtomicReference<LocalTime> depTime = new AtomicReference<>();


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
        grid.setDataProvider(dataProviderFLight);

        grid.addColumn(flight -> flight.getAirline().getAlias()).setHeader("Airline");
        grid.addColumn(flight -> flight.getDepartureAirport().getAlias()).setHeader("Departure Airport");
        grid.addColumn(flight -> flight.getDepartureTime()).setHeader("Departure Time");
        grid.addColumn(flight -> flight.getDestinationAirport().getAlias()).setHeader("Destination Airport");
        grid.addColumn(flight -> flight.getDestinationTime()).setHeader("Departure Airport");
        grid.addColumn(flight -> flight.getFlightTime()).setHeader("Flight Time");

        flightGrid.setWidth("175%");
        flightGrid.add(grid);


        //Load available airlines and airports
        AirlineServiceProvider alsp = new AirlineServiceProvider();
        AirportServiceProvider apsp = new AirportServiceProvider();

        List<String> airlines = alsp.fetchAll().stream().map(Airline::getAlias).collect(Collectors.toList());
        List<String> airports = apsp.fetchAll().stream().map(Airport::getAlias).collect(Collectors.toList());

        //Adding new Flight
        newFlight.add(new H6("ADD NEW FLIGHT"));
        newFlight.getStyle().set("overflow", "auto");

        ComboBox<String> airlineSelect = new ComboBox<>();
        airlineSelect.setLabel("Airline");
        airlineSelect.setItems(airlines);
        airlineSelect.setWidthFull();
        airlineSelect.addValueChangeListener(event -> {
            airline.set(event.getValue());
        });

        ComboBox<String> departureAirportSelect = new ComboBox<>();
        departureAirportSelect.setLabel("Departure Airport");
        departureAirportSelect.setItems(airports);
        departureAirportSelect.setWidthFull();
        departureAirportSelect.addValueChangeListener(event -> {
            depAirport.set(event.getValue());
        });

        ComboBox<String> destinationAirportSelect = new ComboBox<>();
        destinationAirportSelect.setLabel("Destination Airport");
        destinationAirportSelect.setItems(airports);
        destinationAirportSelect.setWidthFull();
        destinationAirportSelect.addValueChangeListener(event -> {
            desAirport.set(event.getValue());
        });

        DatePicker departureDatePicker = new DatePicker();
        TimePicker departureTimePicker = new TimePicker();
        departureDatePicker.setLabel("Departure Date");
        departureDatePicker.setWidth("100%");
        departureTimePicker.setWidth("100%");
        departureTimePicker.getStyle().set("margin-top", "1px");

        departureDatePicker.addValueChangeListener(event -> {
            depDate.set(event.getValue());
        });
        departureTimePicker.addValueChangeListener(event -> {
            depTime.set(event.getValue());
        });

        DatePicker destinationDatePicker = new DatePicker();
        TimePicker destinationTimePicker = new TimePicker();
        destinationDatePicker.setLabel("Destination Date");
        destinationDatePicker.setWidth("100%");
        destinationTimePicker.setWidth("100%");
        destinationTimePicker.getStyle().set("margin-top", "1px");

        destinationDatePicker.addValueChangeListener(event -> {
            desDate.set(event.getValue());
        });
        destinationTimePicker.addValueChangeListener(event -> {
            desTime.set(event.getValue());
        });

        Button commitButton = new Button("+");
        commitButton.getStyle().set("background-color", "#E74C3C");
        commitButton.getStyle().set("color", "white");
        commitButton.setWidth("100%");
        commitButton.getStyle().set("margin-top", "50px");
        commitButton.addClickListener(event -> {
/*
            System.out.println(airline);
            System.out.println(desAirport);
            System.out.println(depAirport);
            System.out.println(Timestamp.valueOf(LocalDateTime.of(depDate.get(), depTime.get())));
            System.out.println(Timestamp.valueOf(LocalDateTime.of(desDate.get(), desTime.get())));
*/
            if (flsp.post(airline.get(),
                    depAirport.get(),
                    desAirport.get(),
                    LocalDateTime.of(depDate.get(), depTime.get()),
                    LocalDateTime.of(desDate.get(), desTime.get()))
            ) Notification.show("Flight added").setPosition(Notification.Position.BOTTOM_START);

            grid.getDataProvider().refreshAll();
        });


        newFlight.setWidth("35%");
        newFlight.getStyle().set("margin-right", "25px");
        newFlight.add(airlineSelect);
        newFlight.add(departureAirportSelect);
        newFlight.add(destinationAirportSelect);
        newFlight.add(departureDatePicker);
        newFlight.add(departureTimePicker);
        newFlight.add(destinationDatePicker);
        newFlight.add(destinationTimePicker);
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
        VerticalLayout subComponent = new VerticalLayout();
        subComponent.add(new H6("AUCTION"));

        HorizontalLayout airlineSelection = new HorizontalLayout();
        HorizontalLayout auction = new HorizontalLayout();

        Label airlineLabel = new Label("Select Airline:");
        airlineLabel.getStyle().set("margin-top", "auto");
        airlineLabel.getStyle().set("margin-bottom", "auto");
        airlineLabel.getStyle().set("margin-left", "var(--lumo-space-m)");

        airlineLabel.setWidth("200%");
        AirlineServiceProvider alsp = new AirlineServiceProvider();
        List<String> airlines = alsp.fetchAll().stream().map(Airline::getAlias).collect(Collectors.toList());

        ComboBox<String> airlineCombobox = new ComboBox<>();
        airlineCombobox.setItems(airlines);
        airlineCombobox.setWidthFull();

        airlineCombobox.addValueChangeListener(event -> {
            if (event.getSource().isEmpty()) {
                auction.removeAll();
                auction.add(new H6("Empty"));
            } else {
                auction.removeAll();
                auction.add(new H6("Selected: " + event.getValue()));
            }
        });

        airlineSelection.add(airlineLabel, airlineCombobox);

        auction.getStyle().set("background-color", "orange");
        auction.setWidthFull();
        auction.setHeightFull();

        subComponent.add(airlineSelection);
        subComponent.add(auction);


        add(subComponent);

    }


}
