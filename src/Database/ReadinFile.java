package Database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ReadinFile {

	/**
	 * For DB classes to write data into text file.
	 * 
	 * @param fileName
	 *            To determine the file to write into.
	 * @param data
	 *            The data to write into the file.
	 */
	public static void write(String fileName, List data) throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(fileName));

        try {
    		for (int i =0; i < data.size() ; i++) {
          		out.println((String)data.get(i));
    		}
        }
        finally {
          out.close();
        }
	}

	/**
	 * For DB classes to read data from text file.
	 * 
	 * @param fileName
	 *            To determine the file to read from.
	 * @return the data to read from the text file.
	 */
	public static List read(String fileName) throws IOException {
		List data = new ArrayList();
	
		Scanner scanner = new Scanner(new FileInputStream(fileName));
		try {
		      while (scanner.hasNextLine()){
		        data.add(scanner.nextLine());
		      }
		    }
		    finally{
		      scanner.close();
		    }
		return data;
	}
}
