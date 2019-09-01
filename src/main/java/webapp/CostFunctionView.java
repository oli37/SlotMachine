package webapp;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.AnimationsBuilder;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import utils.Utility;


public class CostFunctionView extends FlexLayout {

    private Series series;
    private ApexCharts barChart;

    public FlexLayout draw() {

        VerticalLayout cfGrid = new VerticalLayout();
        cfGrid.add(new H6("COST FUNCTION"));
        series = new Series(-100, 0, 100, 200, 300, 400);
        series.setName("Cost Function");
        this.barChart = getChart(series);
        var editView = new HorizontalLayout();

        NumberField a = new NumberField();
        a.setHasControls(true);
        a.setStep(100);
        a.setLabel("-15 min");
        a.setValue(-100d);

        NumberField b = new NumberField();
        b.setHasControls(true);
        b.setStep(100);
        b.setLabel("0 min");
        b.setValue(0d);

        NumberField c = new NumberField();
        c.setHasControls(true);
        c.setStep(100);
        c.setLabel("+15 min");
        c.setValue(100d);

        NumberField d = new NumberField();
        d.setHasControls(true);
        d.setStep(100);
        d.setLabel("+30 min");
        d.setValue(200d);

        NumberField e = new NumberField();
        e.setHasControls(true);
        e.setStep(100);
        e.setLabel("+45 min");
        e.setValue(300d);

        NumberField f = new NumberField();
        f.setHasControls(true);
        f.setStep(100);
        f.setLabel("+60 min");
        f.setValue(4000d);

        a.addValueChangeListener(event -> {
            series = Utility.changeSeries(series, 0, a.getValue());
            cfGrid.remove(this.barChart);
            barChart = getChart(series);
            cfGrid.add(barChart);
        });

        b.addValueChangeListener(event -> {
            series = Utility.changeSeries(series, 1, b.getValue());
            cfGrid.remove(this.barChart);
            barChart = getChart(series);
            cfGrid.add(barChart);
        });

        c.addValueChangeListener(event -> {
            series = Utility.changeSeries(series, 2, c.getValue());
            cfGrid.remove(this.barChart);
            barChart = getChart(series);
            cfGrid.add(barChart);
        });

        d.addValueChangeListener(event -> {
            series = Utility.changeSeries(series, 3, d.getValue());
            cfGrid.remove(this.barChart);
            barChart = getChart(series);
            cfGrid.add(barChart);
        });

        e.addValueChangeListener(event -> {
            series = Utility.changeSeries(series, 4, e.getValue());
            cfGrid.remove(this.barChart);
            barChart = getChart(series);
            cfGrid.add(barChart);
        });

        f.addValueChangeListener(event -> {
            series = Utility.changeSeries(series, 5, f.getValue());
            cfGrid.remove(this.barChart);
            barChart = getChart(series);
            cfGrid.add(barChart);
        });


        editView.add(a, b, c, d, e, f);
        cfGrid.add(editView, barChart);

        add(cfGrid);
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

        barChart.setWidth("848px");
        barChart.setHeight("70%");


        return barChart;
    }
}

