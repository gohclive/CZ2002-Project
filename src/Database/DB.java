package Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface DB {
	
	public ArrayList read(String fileName) throws IOException ;
	public void save(String fileName, List al) throws IOException ;
}
