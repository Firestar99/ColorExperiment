package colors;

import colors.Rendering.PlayMode;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Starter {
	
	public static final String exitString = "exit";
	
	public static void main(String[] args) throws Exception {
		System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Type '" + exitString + "' to exit");
		System.out.println("Default Levels are 'lvl1' to 'lvl#'");
		
		while (true) {
			System.out.println("Picture name: ");
			String name = sc.nextLine();
			if (name.equalsIgnoreCase(exitString)) {
				System.out.println("Exiting!");
				break;
			}
			if (Rendering.EMPTY.equals(name))
				name = "Lvl1";
			
			try {
				new Rendering(PlayMode.HIDDEN).loop(name);
			} catch (IOException e) {
				System.err.println("IOException! maybe the name was not correct?");
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("Error!");
				e.printStackTrace();
			}
		}
		
		sc.close();
	}
	
}
