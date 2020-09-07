package webapp;

import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ContentView extends HorizontalLayout {


    public void drawAdmin() {
        removeAll();
        VerticalLayout admin = new VerticalLayout();
        admin.add(new H6("ADMIN"));
        add(admin);
    }

    public void drawFlights() {
        var flightView = new FlightView();
        flightView.setSizeFull();
        removeAll();
        add(flightView.draw());
    }

    public void drawCostFunction() {
        var cfView = new CostFunctionView();
        cfView.setSizeFull();
        removeAll();
        add(cfView.draw());
    }

    public void drawProposal() {
        var proposalView = new ProposalView();
        proposalView.setSizeFull();
        removeAll();
        add(proposalView.draw());
    }

    public void drawHotspot() {
        var hotspotView = new HotspotView();
        hotspotView.setSizeFull();
        removeAll();
        add(hotspotView.draw());
    }
}
