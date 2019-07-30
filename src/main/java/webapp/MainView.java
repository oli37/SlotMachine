package webapp;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
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
@StyleSheet("frontend://webapp/style.css")

public class MainView extends VerticalLayout {

    private boolean devMode = true; // Turns on colors for every rendered component
    private String file = "src/main/java/webapp/paper_plane_red.png";

    public MainView() {

        //Due to the dynamic loading, this method is necessary
        Image image = getImageFromPath("plane.png", file, "SlotMachine");
        image.setHeight("40px");
        HorizontalLayout logo = new HorizontalLayout();
        //Add some padding around the icon
        logo.add(image);
        logo.setPadding(true);


        HorizontalLayout header = new HorizontalLayout();
        VerticalLayout sideMenu = new VerticalLayout();
        VerticalLayout content = new VerticalLayout();
        HorizontalLayout center = new HorizontalLayout();
        HorizontalLayout footer = new HorizontalLayout();

        // Configure layouts
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        header.setWidth("100%");
        center.setWidth("100%");
        //sideMenu.setWidth("200px");
        content.setWidth("100%");
        footer.setWidth("100%");
        footer.setPadding(true);


        header.add(logo, new H4("SlotMachine"));
        sideMenu.add(new Paragraph("sideMenu"));
        content.add(new Paragraph("content"));
        footer.add(new Paragraph("footer"));


        //Side Menu
        sideMenu.addClassName("sideMenu");
        sideMenu.setHeight("100%");
        sideMenu.setWidth("200");
        sideMenu.setSpacing(false);

        sideMenu.add(createMenuOption(VaadinIcon.DASHBOARD.create(), "Dashboard"),
                createMenuOption(VaadinIcon.AIRPLANE.create(), "Flights"),
                createMenuOption(VaadinIcon.CALENDAR.create(), "Slots"));
        sideMenu.setAlignItems(Alignment.CENTER);

        //header.getStyle().set("background-color", "#48C9B0");

        if (devMode) {
            //Coloring of Components for Development
            //sideMenu.getStyle().set("background-color", "blue");
            //center.getStyle().set("background-color", "green");
            content.getStyle().set("background-color", "yellow");
            footer.getStyle().set("background-color", "orange");
        }


        // Compose layout
        center.add(sideMenu, content);
        center.setFlexGrow(1, sideMenu);
        add(header, center, footer);
        expand(center);
    }

    private Button createMenuOption(Icon icon, String title) {

        Button menuButton = new Button(title, icon);
        menuButton.setWidth("100%");
        menuButton.setClassName("left");

        menuButton.addClickListener(ev -> menuButton.getElement().getParent().getStyle().set("left", "-1000px"));
        menuButton.addClickListener(ev -> Notification.show("Button " + title + " clicked."));
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
/*
HorizontalLayout header = new HorizontalLayout();
        VerticalLayout navBar = new VerticalLayout();
        VerticalLayout content = new VerticalLayout();
        HorizontalLayout center = new HorizontalLayout();
        HorizontalLayout footer = new HorizontalLayout();
Image imageFromStream = new Image( res,"Alternativ text description for logo image");

        // Configure layouts
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        header.setWidth("100%");
        header.setPadding(true);
        center.setWidth("100%");
        navBar.setWidth("200px");
        content.setWidth("100%");
        footer.setWidth("100%");
        footer.setPadding(true);


        header.add(new H4("SlotMachine"));
        navBar.add(new Paragraph("navbar"));
        content.add(new Paragraph("content"));
        footer.add(new Paragraph("footer"));



        if (devMode) {
            //Coloring of Components for Development
            header.getStyle().set("background-color", "red");
            navBar.getStyle().set("background-color", "blue");
            center.getStyle().set("background-color", "green");
            content.getStyle().set("background-color", "yellow");
            footer.getStyle().set("background-color", "orange");
        }


        // Compose layout
        center.add(navBar, content);
        center.setFlexGrow(1, navBar);
        add(header, center, footer);
        expand(center);
 */

/*
        //AppLayoutMenu menu = createMenu();

        //Setting the Logo
        Image image = new Image(new StreamResource("paper_plane_red.png", (InputStreamFactory) () -> {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }), "SlotMachine");

        image.setHeight("40px");
        HorizontalLayout logo = new HorizontalLayout();

        logo.add(image);
        logo.setPadding(true);
        setBranding(logo);

        VerticalLayout content = new VerticalLayout();
        content.add(new H1("Hello World!"));

        setContent(content);

        menu.addMenuItems(
                new AppLayoutMenuItem(VaadinIcon.DASHBOARD.create(), "Dashboard", "dashboard"),
                new AppLayoutMenuItem(VaadinIcon.AIRPLANE.create(), "Flights", "flights"),
                new AppLayoutMenuItem(VaadinIcon.CALENDAR.create(), "Slots", "slots"));

 */