package webapp;

import application.CostFunction;
import application.UserLogin;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.AnimationsBuilder;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.server.VaadinSession;
import db.CostFunctionServiceProvider;
import utils.Utility;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class CostFunctionView extends FlexLayout {

    private ApexCharts cfChart;
    private VerticalLayout cfView;
    private VerticalLayout cfGrid;
    private VerticalLayout cfValues;
    private VerticalLayout sidebar;
    private HorizontalLayout valueChanger;

    private int from = -15, to = 60;
    private Series<Integer> dataSeries = new Series<>(); //Series version of data
    private List<Integer> data = new LinkedList<>();
    private List<String> labels = new LinkedList<>();
    VaadinSession vaadinSession = VaadinSession.getCurrent();
    UserLogin ul = vaadinSession.getAttribute(UserLogin.class);


    public FlexLayout draw() {

        cfView = new VerticalLayout();
        cfView.add(new H6("COST FUNCTION"));

        cfView.getStyle().set("justify-content", "space-between");
        cfView.getStyle().set("align-items", "left");
        cfView.setHeight("100%");
        cfView.setMinWidth("880px");
        cfView.setWidth("70%");

        valueChanger = getValueChanger(from, to);
        cfView.add(valueChanger);
        cfChart = getChart(from, to);
        cfView.add(cfChart);

        sidebar = getSidebar();


        add(cfView, sidebar);


/*
        CostFunctionServiceProvider cfsp = new CostFunctionServiceProvider();
        DataProvider<CostFunction, Void> dataProviderCF = DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    return cfsp.fetch(offset, limit).stream();
                },
                query -> cfsp.getCount());


        Grid<CostFunction> grid = new Grid<>();
        grid.setDataProvider(dataProviderCF);
        grid.addColumn(CostFunction::getName).setHeader("CF Name");
        grid.setSizeFull();

        grid.addSelectionListener(event -> {
            CostFunction selected = event.getFirstSelectedItem().get();

            //For performance reasons
            series = Utility.changeSeries(series, 0, selected.getT1());
            series = Utility.changeSeries(series, 1, selected.getT2());
            series = Utility.changeSeries(series, 2, selected.getT3());
            series = Utility.changeSeries(series, 3, selected.getT4());
            series = Utility.changeSeries(series, 4, selected.getT5());
            series = Utility.changeSeries(series, 5, selected.getT6());

            updateBarChart(series);

            t1.setValue(selected.getT1());
            t2.setValue(selected.getT2());
            t3.setValue(selected.getT3());
            t4.setValue(selected.getT4());
            t5.setValue(selected.getT5());
            t6.setValue(selected.getT6());
        });

        TextField cfName = new TextField();
        cfName.setPlaceholder("Cost Function Name");
        cfName.setClearButtonVisible(true);
        Button cfSaveButton = new Button("Save");
        cfSaveButton.getStyle().set("background-color", "#E74C3C");
        cfSaveButton.getStyle().set("color", "white");

        cfSaveButton.addClickListener(event -> {
            cf.setName(cfName.getValue());
            cfsp.post(cf);
            Notification.show("CostFunction added").setPosition(Notification.Position.BOTTOM_START);
            grid.getDataProvider().refreshAll();
        });

        confirmationView = new HorizontalLayout();
        confirmationView.add(cfName, cfSaveButton);
        editView.add(t1, t2, t3, t4, t5, t6);

        cfView.setHeight("90%");
        cfView.setMinWidth("880px");
        cfView.setWidth("70%");
        cfView.add(editView, cfChart, confirmationView);

        cfGrid = new VerticalLayout();
        cfGrid.setWidth("30%");
        // cfGrid.getStyle().set("background-color", "#E74C3C");
        cfGrid.add(grid);
        add(cfView, cfGrid);
        /*
 */
        return this;
    }


    private VerticalLayout getSidebar() {

        sidebar = new VerticalLayout();
        cfGrid = new VerticalLayout();
        cfValues = new VerticalLayout();
        cfGrid.add(new H6("COST FUNCTIONS"));
        cfGrid.setHeight("50%");
        cfValues.add(new H6("VALUES"));
        cfValues.setHeight("50%");

        CostFunctionServiceProvider cfsp = new CostFunctionServiceProvider();
        List<String> items = cfsp.fetch(ul.getAirlineAlias());

        Grid<String> grid = new Grid<>();
        grid.setItems(items);
        grid.addColumn(x -> x).setHeader("CF Name");
        cfGrid.add(grid);


        sidebar.add(cfGrid, cfValues);

        return sidebar;
    }

    private HorizontalLayout getValueChanger(int from, int to) {
        valueChanger = new HorizontalLayout();
        NumberField fromNF = new NumberField();
        fromNF.setHasControls(true);
        fromNF.setTitle("from");
        fromNF.setValue(-15d);
        fromNF.setStep(15);

        NumberField toNF = new NumberField();
        toNF.setHasControls(true);
        toNF.setTitle("to");
        toNF.setValue(60d);
        toNF.setStep(15);

        fromNF.addValueChangeListener(event -> {
            this.from = event.getValue().intValue();
            var temp = getChart(this.from, this.to);
            this.cfView.replace(this.cfChart, temp);
            this.cfChart = temp;

        });

        toNF.addValueChangeListener(event -> {
            this.to = event.getValue().intValue();
            var temp = getChart(this.from, this.to);
            this.cfView.replace(this.cfChart, temp);
            this.cfChart = temp;
        });

        valueChanger.setHeight("5%");
        valueChanger.add(fromNF, toNF);
        return valueChanger;

    }

    private ApexCharts getChart() {
        return this.cfChart;
    }

    private ApexCharts getChart(int from, int to) {

        int incr = 15;
        this.labels = new LinkedList<>();
        this.data = new LinkedList<>();
        for (int i = from; i <= to; i += incr) {
            this.data.add(100);
            this.labels.add((i <= 0 ? "" : "+") + i + "min");
        }
        this.dataSeries.setData(data.toArray(Integer[]::new));
        this.dataSeries.setName("Value");

        return getChart(this.dataSeries, this.labels);

    }

    private ApexCharts getChart(Series<Integer> data, List<String> labels) {

        ApexCharts barChart = new ApexCharts()
                .withChart(ChartBuilder.get()
                        .withType(Type.bar)
                        .withAnimations(
                                AnimationsBuilder.get()
                                        .withEnabled(false)
                                        .build())
                        .build())
                .withColors("#273746")
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get()
                                .withHorizontal(false)
                                .build())
                        .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(true)
                        .build())
                .withSeries(data)
                .withXaxis(XAxisBuilder.get()
                        .withCategories(labels)
                        .build());

        barChart.setWidth("100%");
        barChart.setHeight("95%"); //does strange things when set to 100%

        return barChart;
    }

    private void updateBarChart(int pos, int val) {
        Series<Integer> newDataSeries = Utility.changeSeries(dataSeries, pos, val);
        var newChart = getChart(newDataSeries, this.labels);
        this.cfView.replace(this.cfChart, newChart);
        this.cfChart = newChart;

    }
}

