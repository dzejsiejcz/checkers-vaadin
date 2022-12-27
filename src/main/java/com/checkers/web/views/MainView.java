package com.checkers.web.views;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


import javax.annotation.security.PermitAll;
import java.io.Serializable;

/**
 * The main view contains a button and a click listener.
 */
@Route(value = "", layout = MainLayout.class)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@PermitAll
public class MainView extends VerticalLayout implements Serializable {

    public MainView() {
        H1 logo = new H1("Vaadin CRM - testing PR");
        logo.addClassName("logo");
        add(new HorizontalLayout(logo));
    }

}
