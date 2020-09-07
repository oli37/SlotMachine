package webapp;

import application.Flight;
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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import db.FlightServiceProvider;
import utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HotspotView extends FlexLayout {


    public FlexLayout draw() {


        var content = new VerticalLayout();
        content.add(new H6("HOTSPOT"));
        var flsp = new FlightServiceProvider();
        var flights = flsp.fetch();
        var columnChart = getColumnChart(flights);
        columnChart.setWidthFull();
        columnChart.setHeight("50%");

        content.add(columnChart);
        content.setWidthFull();
        content.setHeight("95%");
        add(content);
        return this;
    }

    public ApexCharts getColumnChart(List<Flight> flightList) {
        Series<Integer> series = getSeries(flightList);
        List<String> labels = getLabels();

        return new ApexCharts()
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
                        .withEnabled(false)
                        .build())
                .withSeries(
                        series
                )
                .withXaxis(XAxisBuilder.get()
                        .withCategories(labels)
                        .build());
    }


    public ApexCharts getHeatmap(List<Flight> flightList) {
        List<Series<Integer>> series = getSeriesForHeatmap(flightList);
        //List<String> labels = getLabels();

        return new ApexCharts()
                .withChart(ChartBuilder.get()
                        .withType(Type.heatmap)
                        .withAnimations(
                                AnimationsBuilder.get()
                                        .withEnabled(false)
                                        .build())
                        .build())
                .withColors("#273746")
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get()
                                .withHorizontal(true)
                                .build())
                        .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false)
                        .build())
                .withSeries(
                        series.get(7),
                        series.get(6),
                        series.get(5),
                        series.get(4),
                        series.get(3),
                        series.get(2),
                        series.get(1),
                        series.get(0)
                )
                .withXaxis(XAxisBuilder.get()
                        .build());

    }

    private Series<Integer> getSeries(List<Flight> fl) {
        Integer[] startingPlanes = new Integer[96]; //24*4=96
        Arrays.fill(startingPlanes, 0);

        for (var f : fl) {
            var heatValue = startingPlanes[f.getTimeslot()];
            startingPlanes[f.getTimeslot()] = heatValue + 1;
        }
        var series = new Series<Integer>();
        series.setData(startingPlanes);
        series.setName("# flights");
        return series;
    }

    private List<Series<Integer>> getSeriesForHeatmap(List<Flight> fl) {

        Integer[] heatValueArray = new Integer[96]; //24*4=96
        Arrays.fill(heatValueArray, 0);

        for (var f : fl) {
            var heatValue = heatValueArray[f.getTimeslot()];
            heatValueArray[f.getTimeslot()] = heatValue + 1;
        }


        var heatValueArray1 = new Series<Integer>();
        var heatValueArray2 = new Series<Integer>();
        var heatValueArray3 = new Series<Integer>();
        var heatValueArray4 = new Series<Integer>();
        var heatValueArray5 = new Series<Integer>();
        var heatValueArray6 = new Series<Integer>();
        var heatValueArray7 = new Series<Integer>();
        var heatValueArray8 = new Series<Integer>();


        heatValueArray1.setData(Utility.getSliceOfArray(heatValueArray, 0, 11));
        heatValueArray2.setData(Utility.getSliceOfArray(heatValueArray, 12, 23));
        heatValueArray3.setData(Utility.getSliceOfArray(heatValueArray, 24, 35));
        heatValueArray4.setData(Utility.getSliceOfArray(heatValueArray, 36, 47));
        heatValueArray5.setData(Utility.getSliceOfArray(heatValueArray, 48, 59));
        heatValueArray6.setData(Utility.getSliceOfArray(heatValueArray, 60, 71));
        heatValueArray7.setData(Utility.getSliceOfArray(heatValueArray, 72, 83));
        heatValueArray8.setData(Utility.getSliceOfArray(heatValueArray, 84, 96));

        List<Series<Integer>> seriesList = new ArrayList<>();
        seriesList.add(heatValueArray1);
        seriesList.add(heatValueArray2);
        seriesList.add(heatValueArray3);
        seriesList.add(heatValueArray4);
        seriesList.add(heatValueArray5);
        seriesList.add(heatValueArray6);
        seriesList.add(heatValueArray7);
        seriesList.add(heatValueArray8);

        return seriesList;
    }


    private List<String> getLabels() {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < 96; i++) {
            String h = (i / 4) < 10 ? "0" + (i / 4) : Integer.toString((i / 4));
            String m = ((i % 4) * 15) == 0 ? "0" + ((i % 4) * 15) : Integer.toString(((i % 4) * 15));
            String t = h + ":" + m;
            labels.add(t);
        }
        return labels;
    }
}
