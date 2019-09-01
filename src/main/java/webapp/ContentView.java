package webapp;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ContentView extends HorizontalLayout {


    public void drawDashboard() {
        removeAll();
        VerticalLayout dashboard = new VerticalLayout();
        dashboard.add(new H6("DASHBOARD"));
        add(dashboard);
    }

    public void drawFlights() {
        var flightView = new FlightView();
        flightView.setSizeFull();
        removeAll();
        add((Component) flightView.draw());
    }

    public void drawCostFunction() {
        var cfView = new CostFunctionView();
        cfView.setSizeFull();
        removeAll();
        add(cfView.draw());
    }

    public void drawProposal() {
        var auctionView = new ProposalView();
        auctionView.setSizeFull();
        removeAll();
        add((Component) auctionView.draw());
    }
}
