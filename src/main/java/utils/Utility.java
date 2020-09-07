package utils;

import com.github.appreciated.apexcharts.helper.Series;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Utility {


    public static Series<Integer> changeSeries(Series<Integer> series, int pos, int value) {

        assert pos < series.getData().length;
        Integer[] old = series.getData();
        old[pos] = value;

        var newSeries = new Series<Integer>();
        newSeries.setData(old);
        return newSeries;
    }

    public static String hash(String str) {
        return Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();
    }

    public static Integer[] getSliceOfArray(Integer[] arr, int start, int end) {

        // Get the slice of the Array
        Integer[] slice = new Integer[end - start];

        // Copy elements of arr to slice
        for (int i = 0; i < slice.length; i++) {
            slice[i] = arr[start + i];
        }

        // return the slice
        return slice;
    }
    }
