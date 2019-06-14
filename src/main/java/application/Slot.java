package application;

import java.time.OffsetDateTime;


public class Slot {


    private OffsetDateTime slotOpening;
    private OffsetDateTime slotClosing;
    private OffsetDateTime estimatedFlightEventDate; //departure or destination
    private long priceDiff; //compared to regular slot
    private boolean isRegularSlot;


    public Slot() {
    }

    /**
     * Initializes a new Slot
     *
     * @param estimatedFlightEventDate departure or destination date
     * @param priceDiff                price difference to regular slot (regular = 0)
     * @param isRegularSlot            true if slot is regular one
     */
    public Slot(OffsetDateTime estimatedFlightEventDate, long priceDiff, boolean isRegularSlot) {
        this.estimatedFlightEventDate = estimatedFlightEventDate;
        this.priceDiff = isRegularSlot ? 0 : priceDiff;
        this.isRegularSlot = isRegularSlot;
        this.setSlotOpening();
        this.setSlotClosing();
    }

    private void setSlotOpening() {
        OffsetDateTime temp = this.estimatedFlightEventDate;
        int min = this.diffMinutesDownToQuarterHour(temp.getMinute());
        this.slotOpening = temp.minusMinutes(min);
    }

    private void setSlotClosing() {
        OffsetDateTime temp = this.estimatedFlightEventDate;
        int min = this.diffMinutesUpToQuarterHour(temp.getMinute());
        this.slotClosing = temp.plusMinutes(min);
    }

    private int diffMinutesDownToQuarterHour(int min) {
        assert min < 60;
        return min - ((min / 15) * 15);
    }

    private int diffMinutesUpToQuarterHour(int min) {
        assert min < 60;
        return (((min / 15) + 1) * 15) - min - 1;
    }

    public OffsetDateTime getSlotOpening() {
        return slotOpening;
    }

    public OffsetDateTime getSlotClosing() {
        return slotClosing;
    }

    public OffsetDateTime getEstimatedFlightEventDate() {
        return estimatedFlightEventDate;
    }

    public long getPriceDiff() {
        return priceDiff;
    }

    public boolean isRegularSlot() {
        return isRegularSlot;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "slotOpening=" + slotOpening +
                ", slotClosing=" + slotClosing +
                ", estimatedFlightEventDate=" + estimatedFlightEventDate +
                ", priceDiff=" + priceDiff +
                ", isRegularSlot=" + isRegularSlot +
                '}';
    }
}
//