package webapp;

import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletConfiguration;

import javax.servlet.annotation.WebServlet;


@WebServlet(urlPatterns = "/*", name = "SlotMachineServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = SlotMachineUI.class, productionMode = false)
public class SlotMachineServlet extends VaadinServlet {
}