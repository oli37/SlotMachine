package db;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is for playing around with DB stuff
 * Do not use it for something else
 */
public class Playground {


    public static void main(String[] args) {
       var res =  getFlightSeries(2,
                6,
                60,
                100,
                new FlightServiceProvider());

        System.out.println(Arrays.toString(res.get(0)));
        System.out.println(Arrays.toString(res.get(1)));
        System.out.println(Arrays.toString(res.get(2)));
    }

    public static List<Integer[]> getFlightSeries(int startStressedPeriodTimeslot,
                                                        int endStressedPeriodTimeslot,
                                                        int stressedCapacity,
                                                        int normalCapacity,
                                                        FlightServiceProvider flsp) {


        Integer[] flightSlots = flsp.getNrOfDeparturesByTimeslot();
        Integer[] normalFlightSlots;
        Integer[] stressedFlightSlots = new Integer[flightSlots.length];
        Integer[] recoveryFlightSlots = new Integer[flightSlots.length];
        normalFlightSlots = flightSlots.clone();

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
