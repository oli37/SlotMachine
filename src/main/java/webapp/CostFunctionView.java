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
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import db.CostFunctionServiceProvider;

import java.util.LinkedList;
import java.util.List;


public class CostFunctionView extends FlexLayout {

    private VerticalLayout cfView;
    private VerticalLayout sidebar;
    private HorizontalLayout valueChanger;
    private Grid<String> cfGrid;
    private static int enumeratorPrice = 0;
    private static int enumeratorBid = 0;


    private int from = -15, to = 60, incr = 15, stdPrice = 50;

    private CostFunction currentCostFunction;
    private ApexCharts currentBarChart;
    private Grid<String> currentPriceGrid;


    VaadinSession vaadinSession = VaadinSession.getCurrent();
    UserLogin ul = vaadinSession.getAttribute(UserLogin.class);
    CostFunctionServiceProvider cfsp = new CostFunctionServiceProvider();


    public FlexLayout draw() {

        cfView = new VerticalLayout();
        cfView.add(new H6("COST FUNCTION"));

        cfView.getStyle().set("justify-content", "space-between");
        cfView.getStyle().set("align-items", "left");
        cfView.setHeightFull();
        cfView.setMinWidth("880px");
        cfView.setWidth("200%");

        valueChanger = getValueChanger(from, to);
        cfView.add(valueChanger);

        currentCostFunction = new CostFunction("empty", "empty", from, to, incr, stdPrice);
        currentBarChart = getBarChart(currentCostFunction);
        cfView.add(currentBarChart);

        sidebar = getSidebar();

        add(cfView, sidebar);

        return this;
    }


    private VerticalLayout getSidebar() {

        sidebar = new VerticalLayout();
        VerticalLayout cfSelection = new VerticalLayout();
        VerticalLayout cfValues = new VerticalLayout();
        cfSelection.add(new H6("COST FUNCTIONS"));
        cfValues.add(new H6("VALUES"));

        cfGrid = getCfSelectionGrid();
        cfSelection.add(cfGrid);
        currentPriceGrid = getPriceChangeGrid();
        cfValues.add(currentPriceGrid);
        cfSelection.setHeight("50%");
        cfValues.setHeight("50%");
        sidebar.add(cfSelection, cfValues);

        return sidebar;
    }

    private Grid<String> getPriceChangeGrid() {
        Grid<String> priceGrid = new Grid<>();
        priceGrid.setItems(currentCostFunction.getLabelList());
        priceGrid.addColumn(x -> x).setHeader("Delay");

        enumeratorPrice = 0;
        enumeratorBid = 0;

        priceGrid.addComponentColumn(x ->
        {
            var nf = new NumberField();
            nf.setId(Integer.toString(enumeratorPrice));
            nf.setValue((double) currentCostFunction.getPriceList().get(enumeratorPrice));
            nf.setMin(0d);
            nf.setSuffixComponent(new Icon(VaadinIcon.EURO));
            nf.setWidth("90%");
            nf.addValueChangeListener(cl -> {
                int value = nf.getValue().intValue();
                if (nf.getId().isPresent())
                    currentCostFunction.setPrice(Integer.parseInt(nf.getId().get()), value);
                updateBarChart(currentCostFunction);
            });
            enumeratorPrice += 1;

            return nf;
        }).setHeader("Price");


        priceGrid.addComponentColumn(x -> {
            var askButton = new Button("ASK");
            askButton.setId(Integer.toString(enumeratorBid));

            askButton.addClickListener(c -> {
                if (askButton.getId().isPresent()) {
                    var selected = Integer.parseInt(askButton.getId().get());
                    var ask = !currentCostFunction.getAskList().get(selected);
                    currentCostFunction.setAsk(selected, ask);

                    if (ask) askButton.setText("ASK");
                    else askButton.setText("BID");

                    updateBarChart(currentCostFunction);
                    System.out.println("ASK:"+ask+currentCostFunction.getAskList());
                }
            });

            enumeratorBid += 1;
            return askButton;
        }).setHeader("Bid/Ask");


        enumeratorPrice = 0;
        enumeratorBid = 0;
        priceGrid.setHeightFull();
        return priceGrid;
    }

    private Grid<String> getCfSelectionGrid() {

        List<String> items = cfsp.fetch(ul.getAirlineAlias());

        Grid<String> cfGrid = new Grid<>();
        cfGrid.setItems(items);
        cfGrid.addColumn(x -> x).setHeader("CF Name");

        cfGrid.addSelectionListener(event -> {
            assert event.getFirstSelectedItem().isPresent();
            String selection = event.getFirstSelectedItem().get();
            currentCostFunction = cfsp.fetchCF(selection);
            updateBarChart(currentCostFunction);
            updatePriceChangerGrid(currentCostFunction);
            updateCfSelectionGrid();
        });

        cfGrid.setHeightFull();
        return cfGrid;
    }

    private HorizontalLayout getValueChanger(int from, int to) {
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
            var cf = new CostFunction("empty", "empty", this.from, this.to, incr, stdPrice);
            updateBarChart(cf);
            updatePriceChangerGrid(cf);
            currentCostFunction = cf;
        });

        toNF.addValueChangeListener(event -> {
            this.to = event.getValue().intValue();
            var cf = new CostFunction("empty", "empty", this.from, this.to, incr, stdPrice);
            updateBarChart(cf);
            updatePriceChangerGrid(cf);
            currentCostFunction = cf;
        });


        cfCreateButton.addClickListener(event -> {

            if (cfName.getValue().equals("")) {
                Notification.show("CostFunction must not be empty").setPosition(Notification.Position.BOTTOM_START);
            } else {
                currentCostFunction.setName(cfName.getValue());
                currentCostFunction.setOwner(ul.getAirlineAlias());

                cfsp.post(currentCostFunction);
                Notification.show("CostFunction added").setPosition(Notification.Position.BOTTOM_START);
                updateBarChart(currentCostFunction);
                updateCfSelectionGrid();

            }
        });


        valueChanger.setHeight("5%");
        valueChanger.setWidthFull();
        valueChanger.add(fromNF, toNF, cfName, cfCreateButton);
        return valueChanger;

    }

    private ApexCharts getBarChart() {
        return getBarChart(currentCostFunction);
    }


    private ApexCharts getBarChart(CostFunction cf)  {
        var priceSeries = new Series<Float>();
        var tempPriceList = new LinkedList<Float>(cf.getPriceList());

        for (int i = 0; i < cf.getPriceList().size(); i++) {
            if(!cf.getAskList().get(i))
                tempPriceList.set(i, tempPriceList.get(i)*-1);

        }
        priceSeries.setData(tempPriceList.toArray(Float[]::new));
        priceSeries.setName("Value");

        return getBarChart(priceSeries, cf.getLabelList());
    }

    private ApexCharts getBarChart(Series<Float> data, List<String> labels) {

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
        barChart.setHeight("93%"); //does strange things when set to 100%

        return barChart;
    }

    private ApexCharts getSelectedBarChart(String selection) {
        currentCostFunction = cfsp.fetchCF(selection);
        return getBarChart(currentCostFunction);
    }


    private void updateCfSelectionGrid() {
        List<String> items = cfsp.fetch(ul.getAirlineAlias());
        cfGrid.setItems(items);

    }

    private void updatePriceChangerGrid(CostFunction cf) {
        currentPriceGrid.setItems(cf.getLabelList());
        enumeratorPrice = 0;
        enumeratorBid = 0;
    }

    private void updateBarChart(CostFunction cf) {
        var newChart = getBarChart(cf);
        cfView.replace(currentBarChart, newChart);
        currentBarChart = newChart;
    }

}

