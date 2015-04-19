package main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import util.Atlas2Plist;

/**
 * Compatible with libgdx texture packer 3.2.0 & java 7+
 * 
 * @author Sergio
 *
 */
public class Main {
	private final static Charset ENCODING = StandardCharsets.UTF_8;

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.out
					.println("Usage: java -jar <filename>.atlast in order to get <filename>.plist");
		} else {
			new Main().run(args[0]);
		}

	}

	private void run(String arg) {
		try {
			List<String> result = new Atlas2Plist(readTextFile(arg)).getPlist();
			
			writerTextFile(arg.replace(".atlas", ".plist"),result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private List<String> readTextFile(String fileName) throws IOException {
		ArrayList<String> file = new ArrayList<String>();
		Path path = Paths.get(fileName);
		try (Scanner scanner = new Scanner(path, ENCODING.name())) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if(line != null && !line.equals("")){
					file.add(line);
				}
			}
		}
		return file;
	}
	
	void writerTextFile(String fileName, List<String> lines) throws IOException {
	    Path path = Paths.get(fileName);
	    try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
	      for(String line : lines){
	        writer.write(line);
	        writer.newLine();
	      }
	    }
	  }

}
