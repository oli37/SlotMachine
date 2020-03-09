package webapp;

import application.CostFunction;
import application.Proposal;
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
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import db.CostFunctionServiceProvider;
import utils.Utility;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class CostFunctionView extends FlexLayout {

    private ApexCharts cfChart;
    private VerticalLayout cfView;
    private VerticalLayout cfSelection;
    private VerticalLayout cfValues;
    private VerticalLayout sidebar;
    private HorizontalLayout valueChanger;

    private int from = -15, to = 60, incr = 15, stdPrice = 50;
    private Series<Integer> dataSeries = new Series<>(); //Series version of data
    private List<Integer> dadataList = new LinkedList<>();
    private List<String> labelList = new LinkedList<>();
    VaadinSession vaadinSession = VaadinSession.getCurrent();
    UserLogin ul = vaadinSession.getAttribute(UserLogin.class);
    private Grid<String> grid;
    CostFunctionServiceProvider cfsp = new CostFunctionServiceProvider();


    public FlexLayout draw() {


        cfView = new VerticalLayout();
        cfView.add(new H6("COST FUNCTION"));

        cfView.getStyle().set("justify-content", "space-between");
        cfView.getStyle().set("align-items", "left");
        cfView.setHeightFull();
        cfView.setMinWidth("880px");
        cfView.setWidth("200%");

        valueChanger = renderValueChanger(from, to);
        cfView.add(valueChanger);
        cfChart = renderEmptyBarChart(from, to);
        cfView.add(cfChart);

        sidebar = getSidebar();

        add(cfView, sidebar);

        return this;
    }


    private VerticalLayout getSidebar() {

        sidebar = new VerticalLayout();
        cfSelection = new VerticalLayout();
        cfValues = new VerticalLayout();
        cfSelection.add(new H6("COST FUNCTIONS"));
        cfValues.add(new H6("VALUES"));

        grid = renderCfSelectionGrid();
        cfSelection.add(grid);
        cfValues.add(renderValueChangeGrid());
        cfSelection.setHeight("50%");
        cfValues.setHeight("50%");
        sidebar.add(cfSelection, cfValues);

        return sidebar;
    }


    private Grid<String> renderValueChangeGrid() {
        Grid<String> valueGrid = new Grid<>();
        valueGrid.setItems(this.labelList);
        valueGrid.addColumn(x -> x).setHeader("Delay");
        valueGrid.addComponentColumn(x -> new Button("TEST")).setHeader("Price");
        valueGrid.setHeightFull();
        return valueGrid;
    }


    private Grid<String> renderCfSelectionGrid() {

        List<String> items = cfsp.fetch(ul.getAirlineAlias());

        Grid<String> cfGrid = new Grid<>();
        cfGrid.setItems(items);
        cfGrid.addColumn(x -> x).setHeader("CF Name");

        cfGrid.addSelectionListener(event -> {
            String selection = event.getFirstSelectedItem().get();
            changeBarChart(renderSelectedBarChart(selection));
        });

        cfGrid.setHeightFull();
        return cfGrid;
    }

    private HorizontalLayout renderValueChanger(int from, int to) {
        valueChanger = new HorizontalLayout();
        NumberField fromNF = new NumberField();
        fromNF.setHasControls(true);
        fromNF.setTitle("from");
        fromNF.setValue((double) from);
        fromNF.setStep(incr);

        NumberField toNF = new NumberField();
        toNF.setHasControls(true);
        toNF.setTitle("to");
        toNF.setValue((double) to);
        toNF.setStep(incr);

        TextField cfName = new TextField();
        cfName.setPlaceholder("CostFunction Name");
        cfName.setWidth("75%");
        Button cfCreateButton = new Button("Create");
        cfCreateButton.getStyle().set("background-color", "#E74C3C");
        cfCreateButton.getStyle().set("color", "white");
        cfCreateButton.setWidth("25%");

        fromNF.addValueChangeListener(event -> {
            this.from = event.getValue().intValue();
            var newChart = renderEmptyBarChart(this.from, this.to);
            changeBarChart(newChart);
        });

        toNF.addValueChangeListener(event -> {
            this.to = event.getValue().intValue();
            var newChart = renderEmptyBarChart(this.from, this.to);
            changeBarChart(newChart);
        });



        cfCreateButton.addClickListener(event -> {
            var cfsp = new CostFunctionServiceProvider();
            var cf = new CostFunction();

            if (cfName.getValue().equals("")) {
                    Notification.show("CostFunction must not be empty").setPosition(Notification.Position.BOTTOM_START);
                } else {
                    cf.setName(cfName.getValue());
                    cf.setOwner(ul.getAirlineAlias());

                    for (int i = from; i <= to; i += incr) {
                        var prop = new Proposal();
                        prop.setDelay(i);
                        prop.setPrice(stdPrice);
                        prop.setBid(true);
                        cf.addProposal(prop);
                    }
                    cfsp.post(cf);
                    Notification.show("CostFunction added").setPosition(Notification.Position.BOTTOM_START);
                    changeBarChart(renderBarChart());
                    updateGrid();
                }
        });



        valueChanger.setHeight("5%");
        valueChanger.setWidthFull();
        valueChanger.add(fromNF, toNF, cfName, cfCreateButton);
        return valueChanger;

    }

    private ApexCharts renderBarChart() {
        return this.cfChart;
    }

    private ApexCharts renderEmptyBarChart(int from, int to) {

        this.labelList = new LinkedList<>();
        var data = new LinkedList<>();
        for (int i = from; i <= to; i += incr) {
            data.add(stdPrice);
            labelList.add((i <= 0 ? "" : "+") + i + "min");
        }
        this.dataSeries.setData(data.toArray(Integer[]::new));
        this.dataSeries.setName("Value");

        return renderBarChart(this.dataSeries, this.labelList);

    }

    private ApexCharts renderBarChart(Series<Integer> data, List<String> labels) {

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
        barChart.setHeight("90%"); //does strange things when set to 100%

        return barChart;
    }

    private ApexCharts renderSelectedBarChart(String selection) {

        var cf = cfsp.fetchCF(selection);
        var data = cf.getProposalList().stream().map(Proposal::getPrice).map(Math::round).collect(Collectors.toList());
        this.labelList = cf.getProposalList().stream().map(Proposal::getDelay).map(x -> (x <= 0 ? "" : "+") + x + "min").collect(Collectors.toList());
        this.dataSeries.setData(data.toArray(Integer[]::new));
        this.dataSeries.setName("Value");

        return renderBarChart(dataSeries, labelList);

    }

    private void updateBarChart(int pos, int val) {
        Series<Integer> newDataSeries = Utility.changeSeries(dataSeries, pos, val);
        var newChart = renderBarChart(newDataSeries, this.labelList);
        changeBarChart(newChart);

    }

    private void updateGrid() {
        List<String> items = cfsp.fetch(ul.getAirlineAlias());
        grid.setItems(items);
    }

    private void changeBarChart(ApexCharts newChart) {
        cfView.replace(cfChart, newChart);
        cfChart = newChart;
    }
}

