package utils;

import com.github.appreciated.apexcharts.helper.Series;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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

}
