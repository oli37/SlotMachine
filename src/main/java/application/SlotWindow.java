package application;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class SlotWindow {

    List<Slot> slotWindow = new ArrayList<>();
    private int windowSlotSize;
    private OffsetDateTime estimatedFlightEventDate;

    /**
     * Initializes a slot window (window of adjacent slots to the regular estimatedFlightEventDate)
     *
     * @param windowSlotSize           number of slots to consider, must be odd number
     * @param estimatedFlightEventDate departure or destination time
     */
    public SlotWindow(int windowSlotSize, OffsetDateTime estimatedFlightEventDate) {
        this.estimatedFlightEventDate = estimatedFlightEventDate;
        this.windowSlotSize = (windowSlotSize % 2 == 1) ? windowSlotSize : windowSlotSize + 1;//must be odd
        int halfWindowSlotSize = (this.windowSlotSize - 1) / 2;

        System.out.println(this.windowSlotSize);
        for (int i = halfWindowSlotSize; i > 0; i--) {
            OffsetDateTime zdt = estimatedFlightEventDate.minusMinutes(i * 15);
            this.slotWindow.add(new Slot(zdt, 100, false));
        }
        this.slotWindow.add(new Slot(estimatedFlightEventDate, 100, true));

        for (int i = 0; i < halfWindowSlotSize; i++) {
            OffsetDateTime zdt = estimatedFlightEventDate.plusMinutes(i * 15);
            this.slotWindow.add(new Slot(zdt, 100, false));
        }
    }

    public List<Slot> getSlotWindow() {
        return slotWindow;
    }

    public int getWindowSlotSize() {
        return windowSlotSize;
    }

    public OffsetDateTime getEstimatedFlightEventDate() {
        return estimatedFlightEventDate;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Slot s : this.slotWindow) {
            sb.append(s.toString());
            sb.append("\n");
        }
        return "SlotWindow{\n" +
                sb.toString() +
                "}";
    }
}
