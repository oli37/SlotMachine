package webapp;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * The main view of the application
 */
@Route("")

public class MainView extends VerticalLayout {

    private String file = "src/main/java/webapp/paper_plane_red.png";
    ContentView content;

    public MainView() {

        //Contains all other components
        HorizontalLayout center = new HorizontalLayout();
        center.setWidth("100%");

        //Configure layouts
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        //This method is necessary for dynamic loading (aka files from path)
        Image image = getImageFromPath("plane.png", file, "SlotMachine");
        image.setHeight("40px");
        HorizontalLayout logo = new HorizontalLayout();
        logo.add(image);
        logo.setPadding(true);

        //Header
        HorizontalLayout header = new HorizontalLayout();
        header.setWidth("100%");
        H4 text = new H4("SlotMachine");
        header.add(logo, text);
        header.getStyle().set("background-color", "#273746");
        text.getStyle().set("color", "#FFFFFF");


        //Content
        content = new ContentView();
        content.setWidth("100%");
        content.getStyle().set("background-color", "#F8F9F9");


        //Side Menu
        VerticalLayout sideMenu = new VerticalLayout();
        sideMenu.addClassName("sideMenu");
        sideMenu.setHeight("100%");
        sideMenu.setWidth("15%");
        sideMenu.setSpacing(false);


        sideMenu.add(createMenuOption("Dashboard"),
                createMenuOption("Flights"),
                createMenuOption("Bid/Ask"), //called Proposal
                createMenuOption("Cost Function"));

        sideMenu.setAlignItems(Alignment.CENTER);
        sideMenu.getStyle().set("background-color", "#E74C3C");


        // Compose layout
        center.add(sideMenu, content);
        center.setFlexGrow(1, sideMenu);
        add(header, center);
        expand(center);
    }

    private Button createMenuOption(String title) {

        Button menuButton = new Button(title);
        menuButton.setWidth("100%");
        menuButton.setClassName(title);
        menuButton.getStyle().set("color", "black");
        menuButton.getStyle().set("background-color", "white");

        //TODO: Do it right
        if (title.equals("Dashboard")) menuButton.addClickListener(ev -> content.drawDashboard());
        if (title.equals("Flights")) menuButton.addClickListener(ev -> content.drawFlights());
        if (title.equals("Cost Function")) menuButton.addClickListener(ev -> content.drawCostFunction());
        if (title.equals("Bid/Ask")) menuButton.addClickListener(ev -> content.drawProposal());

        return menuButton;
    }

    private Image getImageFromPath(String name, String file, String altText) {
        Image image = new Image(new StreamResource(name, (InputStreamFactory) () -> {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }), altText);
        return image;

    }


}
