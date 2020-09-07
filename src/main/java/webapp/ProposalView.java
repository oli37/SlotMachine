package webapp;

import application.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.server.VaadinSession;
import db.AirlineServiceProvider;
import db.CostFunctionServiceProvider;
import db.FlightServiceProvider;
import db.ProposalServiceProvider;

import java.time.OffsetDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ProposalView extends FlexLayout {

    private int flightID = 0;
    private OffsetDateTime initialTime;
    private OffsetDateTime desiredTime;
    private VerticalLayout subComponent;
    private HorizontalLayout airlineSelection;
    private HorizontalLayout gridContainer;
    private HorizontalLayout singleProposal;
    private HorizontalLayout costFunctionProposal;
    private Grid<Proposal> proposalGrid;
    private Grid<Flight> flightGrid;
    private Flight flight;
    private CostFunction selectedCf;

    AirlineServiceProvider alsp = new AirlineServiceProvider();
    ProposalServiceProvider psp = new ProposalServiceProvider();
    CostFunctionServiceProvider cfsp = new CostFunctionServiceProvider();
    FlightServiceProvider flsp = new FlightServiceProvider();

    VaadinSession vaadinSession = VaadinSession.getCurrent();
    UserLogin ul = vaadinSession.getAttribute(UserLogin.class);

    public ProposalView draw() {
        subComponent = new VerticalLayout();
        gridContainer = new HorizontalLayout();
        airlineSelection = new HorizontalLayout();
        singleProposal = new HorizontalLayout();
        costFunctionProposal = new HorizontalLayout();

        var text = new H6();
        var airlines = alsp.fetch().stream().map(Airline::getAlias).collect(Collectors.toList());


        Label airlineLabel = new Label("Select Airline:");
        airlineLabel.getStyle().set("margin-top", "auto");
        airlineLabel.getStyle().set("margin-bottom", "auto");
        airlineLabel.getStyle().set("margin-left", "var(--lumo-space-m)");
        airlineLabel.setWidth("200%");

        ComboBox<String> airlineCombobox = new ComboBox<>();
        airlineCombobox.setItems(airlines);
        airlineCombobox.setWidthFull();

        var flsp = new FlightServiceProvider();
        flightGrid = new Grid<>();
        flightGrid.addColumn(flight -> flight.getFlightID()).setHeader("ID");
        flightGrid.addColumn(flight -> flight.getAirline().getAlias()).setHeader("Airline");
        flightGrid.addColumn(flight -> flight.getDepartureAirport().getAlias()).setHeader("Departure Airport");
        flightGrid.addColumn(flight -> flight.getDepartureTime()).setHeader("Departure Time");
        //flightGrid.addColumn(flight -> flight.getDestinationAirport().getAlias()).setHeader("Destination Airport");
        //flightGrid.addColumn(flight -> flight.getDestinationTime()).setHeader("Destination Time");

        proposalGrid = new Grid<>();
        //proposalGrid.addColumn(Proposal::getDesiredTime).setHeader("Proposed Time");

        proposalGrid.addColumn(d -> (d.getDelay() <= 0 ? "" : "+") + d.getDelay() + "min").setHeader("Delay");
        proposalGrid.addColumn(Proposal::getPrice).setHeader("Price");
        proposalGrid.addColumn(p -> p.isAsk() ? "ASK" : "BID").setHeader("BID/ASK");


        flightGrid.addSelectionListener(event -> {
            assert event.getFirstSelectedItem().isPresent();
            flight = event.getFirstSelectedItem().get();
            flightID = flight.getFlightID();
            initialTime = flight.getDepartureTime();
            var items = psp.fetchByFlightID(flightID);
            proposalGrid.setItems(items);
        });

        airlineCombobox.addValueChangeListener(event -> {
            var items = flsp.fetchByAirline(event.getValue());
            flightGrid.setItems(items);
        });


        flightGrid.asSingleSelect().addValueChangeListener(event -> {
            text.setText(event.getValue().toString());
            applyCF();
        });


        gridContainer.setHeight("100%");
        gridContainer.setWidth("100%");
        subComponent.setSizeFull();
        flightGrid.setWidth("100%");
        proposalGrid.setWidth("40%");
        flightGrid.setHeight("100%");
        proposalGrid.setHeight("100%");

        if (ul.isAirline()) {
            var items = flsp.fetchByAirline(ul.getAirlineAlias());
            flightGrid.setItems(items);
        } else { //ADMIN & NWMGMT
            airlineSelection.add(airlineLabel, airlineCombobox);
        }

        gridContainer.add(flightGrid, proposalGrid);
        subComponent.add(new H6("ASK/BID"),
                airlineSelection,
                gridContainer,
                singleProposal,
                costFunctionProposal);

        add(subComponent);

        return this;
    }


    private void addProposal() {

        AtomicBoolean isBid = new AtomicBoolean(false);
        singleProposal.removeAll();
        singleProposal.setWidth("100%");


        NumberField price = new NumberField();
        price.setHasControls(true);
        price.setMin(0);
        price.setStep(50);

        NumberField delay = new NumberField();
        delay.setHasControls(true);
        delay.setStep(15);

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

            double d = delay.getValue();
            double p = price.getValue();
            var psp = new ProposalServiceProvider();

            var proposal = new Proposal();


            psp.post(proposal);
            var items = psp.fetchByFlightID(flightID);
            proposalGrid.setItems(items);

            //Reset Buttons
            askButton.setEnabled(true);
            bidButton.setEnabled(true);
            bidButton.getStyle().set("background-color", "#1ABC9C");
            askButton.getStyle().set("background-color", "#EC7063");

            Notification.show("Proposal added").setPosition(Notification.Position.BOTTOM_START);
        });


        Label delayLabel = new Label("Delay [min]:");
        delayLabel.getStyle().set("margin-top", "auto");
        delayLabel.getStyle().set("margin-bottom", "auto");
        delayLabel.getStyle().set("margin-left", "var(--lumo-space-m)");

        Label priceLabel = new Label("Price:");
        priceLabel.getStyle().set("margin-top", "auto");
        priceLabel.getStyle().set("margin-bottom", "auto");
        priceLabel.getStyle().set("margin-left", "var(--lumo-space-m)");

        commitButton.getStyle().set("margin-right", "auto");

        singleProposal.add(delayLabel, delay, priceLabel, price, bidButton, askButton, commitButton);

    }

    private void applyCF() {

        costFunctionProposal.removeAll();
        costFunctionProposal.setWidth("742px");

        var strListCfNames = cfsp.fetch(ul.getAirlineAlias());

        ComboBox<String> costFunctionComboBox = new ComboBox<>();
        costFunctionComboBox.setItems(strListCfNames);
        costFunctionComboBox.setWidth("424px");
        costFunctionComboBox.setValue("Select Cost Function");

        Button applyFunctionButton = new Button("Apply Function");
        applyFunctionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        applyFunctionButton.getStyle().set("background-color", "#E74C3C");
        applyFunctionButton.setWidth("302px");

        costFunctionProposal.add(costFunctionComboBox, applyFunctionButton);

        costFunctionComboBox.addValueChangeListener(event -> {
            String selected = event.getValue();
            selectedCf = cfsp.fetchCF(selected);
            System.out.println(selectedCf);
        });

        applyFunctionButton.addClickListener(event -> {

            assert selectedCf != null;
            flsp.link(selectedCf, flight);
            var items = psp.fetchByFlightID(flightID);
            proposalGrid.setItems(items);

            Notification.show("Applied Cost Function").setPosition(Notification.Position.BOTTOM_START);


        });
    }
}