package webapp;

import application.Airline;
import application.Flight;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import db.AirlineServiceProvider;
import db.AuctionServiceProvider;
import db.FlightServceProvider;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class AuctionView extends FlexLayout {

    HorizontalLayout offer = new HorizontalLayout();

    public FlexComponent draw() {
        VerticalLayout subComponent = new VerticalLayout();
        subComponent.add(new H6("AUCTION"));

        AirlineServiceProvider alsp = new AirlineServiceProvider();
        List<String> airlines = alsp.fetchAll().stream().map(Airline::getAlias).collect(Collectors.toList());

        HorizontalLayout airlineSelection = new HorizontalLayout();
        VerticalLayout auction = new VerticalLayout();

        Label airlineLabel = new Label("Select Airline:");
        airlineLabel.getStyle().set("margin-top", "auto");
        airlineLabel.getStyle().set("margin-bottom", "auto");
        airlineLabel.getStyle().set("margin-left", "var(--lumo-space-m)");

        airlineLabel.setWidth("200%");

        ComboBox<String> airlineCombobox = new ComboBox<>();
        airlineCombobox.setItems(airlines);
        airlineCombobox.setWidthFull();

        var flsp = new FlightServceProvider();
        Grid<Flight> grid = new Grid<>();
        grid.addColumn(flight -> flight.getFlightID()).setHeader("ID");
        grid.addColumn(flight -> flight.getAirline().getAlias()).setHeader("Airline");
        grid.addColumn(flight -> flight.getDepartureAirport().getAlias()).setHeader("Departure Airport");
        grid.addColumn(flight -> flight.getDepartureTime()).setHeader("Departure Time");
        grid.addColumn(flight -> flight.getDestinationAirport().getAlias()).setHeader("Destination Airport");
        grid.addColumn(flight -> flight.getDestinationTime()).setHeader("Destination Time");
        grid.setHeight("50%");

        airlineCombobox.addValueChangeListener(event -> {
            var items = flsp.fetchByAirline(event.getValue());
            grid.setItems(items);
            items.forEach(System.out::println);
        });


        var text = new H6();
        grid.asSingleSelect().addValueChangeListener(event -> {
            text.setText(event.getValue().toString());
            auction();
        });


        subComponent.setSizeFull();
        auction.setSizeFull();

        auction.add(grid, offer);
        airlineSelection.add(airlineLabel, airlineCombobox);
        subComponent.add(airlineSelection, auction);
        add(subComponent);

        return this;
    }


    private void auction() {

        AtomicBoolean isBid = new AtomicBoolean(false);
        offer.removeAll();
        offer.setWidth("100%");

        NumberField price = new NumberField();
        price.setHasControls(true);
        price.setMin(0);
        price.setStep(50);

        NumberField delay = new NumberField();
        delay.setHasControls(true);
        delay.setStep(5);

        Button bidButton = new Button("Bid");
        Button askButton = new Button("Ask");
        Button commitButton = new Button("Commit");

        bidButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        askButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        commitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        bidButton.getStyle().set("background-color", "#1ABC9C");
        askButton.getStyle().set("background-color", "#EC7063");
        commitButton.getStyle().set("background-color", "#E74C3C");


        bidButton.addClickListener(event -> {
            askButton.setEnabled(false);
            askButton.getStyle().set("background-color", "#CACFD2");
            isBid.set(true);
        });

        askButton.addClickListener(event -> {
            bidButton.setEnabled(false);
            bidButton.getStyle().set("background-color", "#CACFD2");
            isBid.set(false);
        });

        commitButton.addClickListener(event -> {

            var d = delay.getValue();
            var p = price.getValue();

            var asp = new AuctionServiceProvider();
            offer.removeAll();
        });


        Label delayLabel = new Label("Delay [min]:");
        delayLabel.getStyle().set("margin-top", "auto");
        delayLabel.getStyle().set("margin-bottom", "auto");
        delayLabel.getStyle().set("margin-left", "var(--lumo-space-m)");

        Label priceLabel = new Label("Price [€]:");
        priceLabel.getStyle().set("margin-top", "auto");
        priceLabel.getStyle().set("margin-bottom", "auto");
        priceLabel.getStyle().set("margin-left", "var(--lumo-space-m)");

        commitButton.getStyle().set("margin-right", "auto");

        offer.add(delayLabel, delay, priceLabel, price, bidButton, askButton, commitButton);

    }
}
