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
    private List<String[]> list;
    private String path;

    public XMLReader(String path) {
        this.path = path;
        this.list = getListOfRows();

    }

    /**
     * Returns a list of String arrays from XML-file
     *
     * @return a List of Rows
     */
    public List<String[]> getListOfRows() {


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
     * @return String array
     */
    public String[] getRandomRow() {

        if (list != null) {
            int maxLen = list.size();
            Random random = new Random();
            int rand = random.nextInt(maxLen - 1) + 1;
            return list.get(rand);
        }
        return null;
    }

    /**
     * Returns a given row from XML-file
     *
     * @param row of the XML file
     * @return String array, NULL if not available or outside of range
     */
    public String[] getRow(int row) {

        if (list != null && row <= list.size()) {
            return list.get(row);
        }
        return null;
    }

    public int getLenght() {
        return list.size();
    }

}



