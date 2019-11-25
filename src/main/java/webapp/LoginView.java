package webapp;


import application.UserLogin;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import db.LoginServiceProvider;


@Route("")
public class LoginView extends VerticalLayout {


    private String pw;
    private String user;
    private LoginServiceProvider lsp = new LoginServiceProvider();

    public LoginView() {

        var title = new H3("Login");
        var usernamefield = new TextField();
        var passwordfield = new PasswordField();
        var loginbutton = new Button("Login");


        usernamefield.setLabel("Username");
        usernamefield.setAutofocus(true);
        passwordfield.setLabel("Password");
        loginbutton.getStyle().set("background-color", "#E74C3C");
        loginbutton.getStyle().set("color", "white");

        add(title, usernamefield, passwordfield, loginbutton);

        loginbutton.addClickListener(event -> {
            user = usernamefield.getValue();
            pw = passwordfield.getValue();


            VaadinSession vaadinSession = VaadinSession.getCurrent();
            UserLogin ul = new UserLogin(user, pw, lsp.getUserRole(user), lsp.getAirline(user));

            if (lsp.isAuthenticated(ul)) {
                vaadinSession.setAttribute(UserLogin.class, ul);
                Notification.show("Login succeeded").setPosition(Notification.Position.BOTTOM_START);
                getUI().ifPresent(ui -> ui.navigate("main"));

            } else {
                Notification.show("Login failed").setPosition(Notification.Position.BOTTOM_START);
            }
        });
    }

}



