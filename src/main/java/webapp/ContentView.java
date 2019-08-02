package webapp;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ContentView extends VerticalLayout {


    public void addText(String text) {
        add(new H1(text));
    }

    public void drawFlights() {

    }

}
