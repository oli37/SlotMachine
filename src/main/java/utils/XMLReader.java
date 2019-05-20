package utils;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLReader {

    private static final Logger LOGGER = Logger.getLogger("SlotMachine");


    /**
     * Returns a list of String arrays from XML-file
     *
     * @param path path to the XML File
     * @return a List of Rows
     */
    static List<String[]> ReadXML(String path) {

        List<String[]> list;

        try {
            FileReader fileReader = new FileReader(path);
            CSVReader csvReader = new CSVReader(fileReader);

            list = csvReader.readAll();
            fileReader.close();
            csvReader.close();

            return list;

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, String.valueOf(e));

        }
        return null;
    }

    /**
     * Returns random row from XML-file
     *
     * @param path path to XML File
     * @return String array
     */
    static String[] RandomRowFromXML(String path) {

        List<String[]> list = ReadXML(path);
        int maxLen = 0;
        if (list != null) {
            maxLen = list.size();
            Random random = new Random();
            int rand = random.nextInt(maxLen - 1) + 1;
            return list.get(rand);
        }
        return null;
    }


}



