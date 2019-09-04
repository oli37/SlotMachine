package webapp;

import application.CostFunction;
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
import com.vaadin.flow.data.provider.DataProvider;
import db.CostFunctionServiceProvider;
import utils.Utility;


public class CostFunctionView extends FlexLayout {

    private Series series;
    private ApexCharts cfChart;
    private VerticalLayout cfView;
    private VerticalLayout cfGrid;
    private HorizontalLayout confirmationView;

    public FlexLayout draw() {

        cfView = new VerticalLayout();
        cfView.add(new H6("COST FUNCTION"));

        cfView.getStyle().set("justify-content", "space-between");
        cfView.getStyle().set("align-items", "center");

        series = new Series(-100, 0, 100, 200, 300, 400); //needed for representation in chart
        var cf = new CostFunction("", -100, 0, 100, 200, 300, 400); //needed for representation in DB

        series.setName("Cost Function");
        this.cfChart = getChart(series);
        var editView = new HorizontalLayout();

        NumberField t1 = new NumberField();
        t1.setHasControls(true);
        t1.setStep(50);
        t1.setLabel("-15 min");
        t1.setValue(-100d);

        NumberField t2 = new NumberField();
        t2.setHasControls(true);
        t2.setStep(50);
        t2.setLabel("0 min");
        t2.setValue(0d);

        NumberField t3 = new NumberField();
        t3.setHasControls(true);
        t3.setStep(50);
        t3.setLabel("+15 min");
        t3.setValue(100d);

        NumberField t4 = new NumberField();
        t4.setHasControls(true);
        t4.setStep(50);
        t4.setLabel("+30 min");
        t4.setValue(200d);

        NumberField t5 = new NumberField();
        t5.setHasControls(true);
        t5.setStep(50);
        t5.setLabel("+45 min");
        t5.setValue(300d);

        NumberField t6 = new NumberField();
        t6.setHasControls(true);
        t6.setStep(50);
        t6.setLabel("+60 min");
        t6.setValue(400d);

        t1.addValueChangeListener(event -> {
            updateBarChart(0, t1.getValue());
            cf.setT1(t1.getValue());
        });
        t2.addValueChangeListener(event -> {
            updateBarChart(1, t2.getValue());
            cf.setT2(t2.getValue());
        });
        t3.addValueChangeListener(event -> {
            updateBarChart(2, t3.getValue());
            cf.setT3(t3.getValue());
        });
        t4.addValueChangeListener(event -> {
            updateBarChart(3, t4.getValue());
            cf.setT4(t4.getValue());
        });
        t5.addValueChangeListener(event -> {
            updateBarChart(4, t5.getValue());
            cf.setT5(t5.getValue());
        });
        t6.addValueChangeListener(event -> {
            updateBarChart(5, t6.getValue());
            cf.setT6(t6.getValue());
        });


        CostFunctionServiceProvider cfsp = new CostFunctionServiceProvider();
        DataProvider<CostFunction, Void> dataProviderCF = DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    return cfsp.fetch(offset, limit).stream();
                },
                query -> cfsp.getCount());


        //For some reason changes with grids eg. dataprovider in Vaadin require a restart of jetty
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
        cfView.setWidth("70%");//("880px");
        cfView.add(editView, cfChart, confirmationView);

        cfGrid = new VerticalLayout();
        cfGrid.setWidth("30%");
        // cfGrid.getStyle().set("background-color", "#E74C3C");
        cfGrid.add(grid);
        add(cfView, cfGrid);
        return this;
    }


    private ApexCharts getChart(Series series) {

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
                .withSeries(series)
                .withXaxis(XAxisBuilder.get()
                        .withCategories("-15 min", "0 min", "+15 min", "+30 min", "+45 min", "+60min")
                        .build());

        barChart.setWidth("100%");
        barChart.setHeight("100%");


        return barChart;
    }

    private void updateBarChart(int pos, double val) {
        series = Utility.changeSeries(series, pos, val);
        series.setName("Value");
        cfView.remove(this.cfChart);
        cfChart = getChart(series);
        cfView.add(cfChart);
        cfView.add(confirmationView);
    }

    private void updateBarChart(Series series) {
        series.setName("Value");
        cfView.remove(this.cfChart);
        cfChart = getChart(series);
        cfView.add(cfChart);
        cfView.add(confirmationView);
    }

}

