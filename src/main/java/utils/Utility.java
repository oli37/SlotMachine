package utils;

import com.github.appreciated.apexcharts.helper.Series;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Utility {


    public static Series changeSeries(Series series, int pos, double value) {

        assert pos < series.getData().length;
        var old = series.getData();
        old[pos] = value;

        return new Series(old);
    }

    public static String hash(String str) {
        return Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();
    }

}
