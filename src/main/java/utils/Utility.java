package utils;

import com.github.appreciated.apexcharts.helper.Series;

public class Utility {


    public static Series changeSeries(Series series, int pos, double value) {

        assert pos < series.getData().length;
        var old = series.getData();
        old[pos] = value;

        return new Series(old);
    }
}
