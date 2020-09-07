package webapp;

import application.Role;
import application.UserLogin;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import db.LoginServiceProvider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * The main view of the application
 */
@Route("") //must be main

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

        //Retrieve user information from session
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        //var ul = vaadinSession.getAttribute(UserLogin.class);

        //Just for testing **********************************************************
        //CHANGE ROUTE TO MAIN
        LoginServiceProvider lsp = new LoginServiceProvider();
        UserLogin ul = new UserLogin("Susi", "1234", lsp.getUserRole("Susi"), lsp.getAirline("Susi"));
        vaadinSession.setAttribute(UserLogin.class, ul);
        //***************************************************************************

        //Show current user information from session
        VerticalLayout footer = new VerticalLayout();
        H6 user;


        if (ul.isAirline()) {
            user = new H6(ul.getUserName() + "  (AIRLINE: " + ul.getAirlineAlias() + ")");
        } else {
            user = new H6(ul.getUserName() + "  (" + ul.getRole().toString() + ")");

        }

        user.getStyle().set("text-align", "center");

        Button logoutButton = new Button("Logout");
        logoutButton.setWidth("100%");
        logoutButton.setMinHeight("20px");
        logoutButton.getStyle().set("color", "white");
        logoutButton.getStyle().set("background-color", "#5D6D7E");

        logoutButton.addClickListener(ev -> {
            vaadinSession.close();
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        user.getStyle().set("color", "white");
        footer.add(user, logoutButton);
        footer.setJustifyContentMode(JustifyContentMode.CENTER);
        footer.setWidth("100%");

        VerticalLayout filler = new VerticalLayout();
        filler.setSizeFull();

        if (ul.getRole().equals(Role.ADMIN))
            sideMenu.add(createMenuOption("Admin"));

        sideMenu.add(
                createMenuOption("Flights"),
                createMenuOption("Bid/Ask"), //called Proposal
                createMenuOption("Cost Function"),
                createMenuOption("Hotspot"),
                filler,
                footer);


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
        menuButton.setHeight("10%");
        menuButton.setMinHeight("40px");
        menuButton.setClassName(title);
        menuButton.getStyle().set("color", "black");
        menuButton.getStyle().set("background-color", "white");

        //TODO: Do it right
        if (title.equals("Admin")) menuButton.addClickListener(ev -> content.drawAdmin());
        if (title.equals("Flights")) menuButton.addClickListener(ev -> content.drawFlights());
        if (title.equals("Cost Function")) menuButton.addClickListener(ev -> content.drawCostFunction());
        if (title.equals("Bid/Ask")) menuButton.addClickListener(ev -> content.drawProposal());
        if (title.equals("Hotspot")) menuButton.addClickListener(ev -> content.drawHotspot());

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
