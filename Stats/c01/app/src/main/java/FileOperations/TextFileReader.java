package FileOperations;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



public class TextFileReader {
    public String[] readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            String[] fileContents = new String[6];
            String line = br.readLine();
            line = line.replace("_", ",");
            fileContents = line.split(",");
            return fileContents;
        } finally {
            br.close();
        }
    }
}