package webapp;

import application.Hotspot;
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
import com.vaadin.flow.component.timepicker.TimePicker;
import db.FlightServiceProvider;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HotspotView extends FlexLayout {


    private FlightServiceProvider flsp = new FlightServiceProvider();
    private VerticalLayout chartContainer = new VerticalLayout();
    private ApexCharts columnChart;
    private Grid<String> grid = new Grid<>();
    private Hotspot hotspot = new Hotspot();


    public FlexLayout draw() {

        var content = new VerticalLayout();
        content.add(new H6("HOTSPOT"));
        var flsp = new FlightServiceProvider();
        var flights = flsp.fetch();
        columnChart = getColumnChart();
        columnChart.setWidthFull();
        columnChart.setSizeFull();

        chartContainer.setPadding(false);
        chartContainer.setPadding(false);
        chartContainer.setHeight("25%");

        var controlContainer = new HorizontalLayout();
        controlContainer.setPadding(false);
        controlContainer.setPadding(false);

        chartContainer.add(columnChart);


        controlContainer.add(getStartTimeslotPicker(),
                getEndTimeslotPicker(),
                getNormalCapacity(),
                getStressedCapacity(),
                getAddButton());

        grid.addColumn(x -> x).setHeader("Name");
        grid.addColumn(x -> x).setHeader("Start");
        grid.addColumn(x -> x).setHeader("End");
        grid.addColumn(x -> x).setHeader("Normal Capacity");
        grid.addColumn(x -> x).setHeader("Stressed Capacity");


        content.add(chartContainer, controlContainer, getGrid());
        content.setWidthFull();
        content.setHeight("95%");
        add(content);
        return this;
    }


    private Grid<String> getGrid() {

        return grid;
    }

    private ApexCharts getColumnChart() {
        var list = getFlightCapacityList(
                hotspot.getStartTimeslotIndex(),
                hotspot.getEndTimeslotIndex(),
                hotspot.getStressedCapacity(),
                hotspot.getNormalCapacity(),
                flsp);

        var normalSeries = new Series<Integer>();
        var stressedSeries = new Series<Integer>();
        var recoverySeries = new Series<Integer>();

        normalSeries.setData(list.get(0));
        normalSeries.setName("normal");
        stressedSeries.setData(list.get(1));
        stressedSeries.setName("stressed");
        recoverySeries.setData(list.get(2));
        recoverySeries.setName("recovery");


        return new ApexCharts()
                .withChart(ChartBuilder.get()
                        .withType(Type.bar)
                        .withStacked(true)
                        .withAnimations(
                                AnimationsBuilder.get()
                                        .withEnabled(false)
                                        .build())
                        .build())
                .withColors("#273746", "#E74C3C", "#239966")
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get()
                                .withHorizontal(false)
                                .build())
                        .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false)
                        .build())
                .withSeries(
                        normalSeries, stressedSeries, recoverySeries
                )
                .withXaxis(XAxisBuilder.get()
                        .withCategories(hotspot.getLabelList())
                        .build());


    }

    private TimePicker getStartTimeslotPicker() {
        var tp = new TimePicker();
        tp.setStep(Duration.ofMinutes(15));
        tp.setLabel("Start");
        tp.addValueChangeListener(event ->
        {
            hotspot.setStartTimeString(tp.getValue().toString());
            updateBarChart();
        });
        return tp;
    }

    private TimePicker getEndTimeslotPicker() {
        var tp = new TimePicker();
        tp.setStep(Duration.ofMinutes(15));
        tp.setLabel("End");
        tp.addValueChangeListener(event ->
        {
            hotspot.setEndTimeString(tp.getValue().toString());
            updateBarChart();
        });
        return tp;
    }

    private NumberField getNormalCapacity() {
        var capacity = new NumberField();
        capacity.setLabel("Capacity");
        capacity.setMin(0);
        capacity.setStep(10);
        capacity.setHasControls(true);

        capacity.addValueChangeListener(event ->
        {
             hotspot.setNormalCapacity((int) capacity.getValue().longValue());
            updateBarChart();
        });
        return capacity;
    }

    private NumberField getStressedCapacity() {
        var stressedCapacity = new NumberField();
        stressedCapacity.setLabel("Stressed Capacity");
        stressedCapacity.setMin(0);
        stressedCapacity.setStep(10);
        stressedCapacity.setHasControls(true);
        stressedCapacity.addValueChangeListener(event ->
        {
            hotspot.setStressedCapacity((int) stressedCapacity.getValue().longValue());
            updateBarChart();
        });
        return stressedCapacity;
    }

    private Button getAddButton() {
        Button addButton = new Button("Add");
        addButton.getStyle().set("position", "relative");
        addButton.getStyle().set("bottom", "-43%");
        addButton.getStyle().set("background-color", "#E74C3C");
        addButton.getStyle().set("color", "white");
        ArrayList<String> l = new ArrayList<>();

        addButton.addClickListener(e -> {
            //l.add();
            grid.setItems(l);
        });

        return addButton;
    }



    private void updateBarChart() {

        var newChart = getColumnChart();
        newChart.setWidthFull();
        newChart.setSizeFull();
        chartContainer.replace(columnChart, newChart);
        columnChart = newChart;
    }


    /***
     *
     * @param startStressedPeriodTimeslot as Integer (0-95)
     * @param endStressedPeriodTimeslot as Integer (0-95)
     * @param stressedCapacity as Integer
     * @param normalCapacity as Integer
     * @param flsp FlightServiceProvider
     * @return List of Flight Capacity Timeslots (0) Normal (1) Stressed (2) Recovery
     */
    private List<Integer[]> getFlightCapacityList(int startStressedPeriodTimeslot,
                                                  int endStressedPeriodTimeslot,
                                                  int stressedCapacity,
                                                  int normalCapacity,
                                                  FlightServiceProvider flsp) {


        Integer[] flightSlots = flsp.getNrOfDeparturesByTimeslot();
        Integer[] normalFlightSlots;
        Integer[] stressedFlightSlots = new Integer[flightSlots.length];
        Integer[] recoveryFlightSlots = new Integer[flightSlots.length];
        normalFlightSlots = flightSlots.clone();

        List<Integer[]> normalFlightCapacityList = new ArrayList<>();
        normalFlightCapacityList.add(normalFlightSlots);
        normalFlightCapacityList.add(stressedFlightSlots);
        normalFlightCapacityList.add(recoveryFlightSlots);


        //start -> draw just normal capacity
        if (stressedCapacity == 0 || normalCapacity == 0)
            return normalFlightCapacityList;

        //invalid data --> normal capacity
        if (startStressedPeriodTimeslot == 0 ||
                endStressedPeriodTimeslot == 0 ||
                startStressedPeriodTimeslot > endStressedPeriodTimeslot ||
                stressedCapacity > normalCapacity) {

            Notification.show("Invalid data").setPosition(Notification.Position.BOTTOM_START);
            return normalFlightCapacityList;
        }


        int backlog = 0;
        for (int i = 0; i < flightSlots.length; i++) {

            // STRESSED PERIOD
            if (i >= startStressedPeriodTimeslot && i <= endStressedPeriodTimeslot) {
                backlog += flightSlots[i];
                if (backlog > stressedCapacity) {
                    stressedFlightSlots[i] = stressedCapacity;
                    backlog -= stressedCapacity;
                } else {
                    stressedFlightSlots[i] = backlog;
                    backlog = 0;
                }
                normalFlightSlots[i] = 0;
            } else {
                stressedFlightSlots[i] = 0;
            }

            //RECOVERY PERIOD
            if (i > endStressedPeriodTimeslot && backlog > 0) {
                backlog += flightSlots[i];
                if (backlog > normalCapacity) {
                    recoveryFlightSlots[i] = normalCapacity;
                    backlog -= normalCapacity;
                } else {
                    recoveryFlightSlots[i] = backlog;
                    backlog = 0;
                }
                normalFlightSlots[i] = 0;
            } else {
                recoveryFlightSlots[i] = 0;
            }
        }

        List<Integer[]> list = new ArrayList<>();
        list.add(normalFlightSlots);
        list.add(stressedFlightSlots);
        list.add(recoveryFlightSlots);

        return list;
    }
}
