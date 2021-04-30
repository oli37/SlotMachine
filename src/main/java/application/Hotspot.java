package application;

import java.util.ArrayList;
import java.util.List;

public class Hotspot {

    private String timeslotString;
    private int startTimeslotIndex = 0;
    private int endTimeslotIndex = 0;
    private int normalCapacity = 0;
    private int stressedCapacity = 0;
    private List<String> labelList;

    public Hotspot() {
        labelList = getLabelsList();

    }

    public void setStartTimeString(String start) {
        startTimeslotIndex = getTimeslotArrayIndex(start);
    }

    public void setEndTimeString(String end) {
        endTimeslotIndex = getTimeslotArrayIndex(end);
    }


    public String getStartTimeString() {
        return labelList.get(startTimeslotIndex);
    }

    public String getEndTimeString() {
        return labelList.get(endTimeslotIndex);
    }

    public String getTimeslotString() {
        return timeslotString;
    }

    public void setTimeslotString(String timeslotString) {
        this.timeslotString = timeslotString;
    }

    public int getStartTimeslotIndex() {
        return startTimeslotIndex;
    }

    public void setStartTimeslotIndex(int startTimeslotIndex) {
        this.startTimeslotIndex = startTimeslotIndex;
    }

    public int getEndTimeslotIndex() {
        return endTimeslotIndex;
    }

    public void setEndTimeslotIndex(int endTimeslotIndex) {
        this.endTimeslotIndex = endTimeslotIndex;
    }

    public int getNormalCapacity() {
        return normalCapacity;
    }

    public void setNormalCapacity(int normalCapacity) {
        this.normalCapacity = normalCapacity;
    }

    public int getStressedCapacity() {
        return stressedCapacity;
    }

    public void setStressedCapacity(int stressedCapacity) {
        this.stressedCapacity = stressedCapacity;
    }

    public List<String> getLabelList() {
        return labelList;
    }
    private int getTimeslotArrayIndex(String ts) {
        var parts = ts.split(":");
        int a = Integer.parseInt(parts[0]);
        int b = Integer.parseInt(parts[1]);

        return a * 4 + b / 15;
    }

    private List<String> getLabelsList() {
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
