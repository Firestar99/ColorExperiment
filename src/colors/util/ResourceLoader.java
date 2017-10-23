package colors.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceLoader {
	
	public static InputStream getLevel(String str) throws IOException {
		return new FileInputStream("levels/" + str);
	}
	
	public static InputStream getResource(String str) throws IOException {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(str);
		if (in != null) {
			return in;
		}
		throw new IOException("Resource Inputstream was null");
	}
	
}
