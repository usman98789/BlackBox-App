package FileOperations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



public class TextFileReader {
    public String[] readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            String fullLine = "";
            String[] fileContents = new String[6];
            String line = br.readLine();
            fullLine += line;
            while ((line = br.readLine()) != null) {
                fullLine += line;
            }
            fileContents = fullLine.split("_");
            return fileContents;
        } finally {
            br.close();
        }
    }
}