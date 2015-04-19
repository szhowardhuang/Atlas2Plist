package util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Sergio
 *
 */
public class Atlas2Plist {
	
	private HashMap<String, HashMap<String, String>> bodyData;
	private List<String> output;
	private List<String> keys;

	public Atlas2Plist(List<String> input) {
		init(input);
		createPlist();
	}
	
	private void init(List<String> input){
		output = new LinkedList<String>();
		keys = new LinkedList<String>();
		bodyData(input);
	}
	
	private void createPlist(){
		head();
		body();
		footer();
	}
	


	private void head() {
		output.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		output.add("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
		output.add("<plist version=\"1.0\">");
	}

	private void footer() {
		output.add("</plist>");
	}

	private void body() throws IllegalStateException {
		// begin
		output.add("\t<dict>");
		// frames
		output.add("\t\t<key>frames</key>");
		output.add("\t\t<dict>");
		bodyTags();
		output.add("\t\t</dict>");
		// metadata
		output.add("\t\t<key>metadata</key>");
		metadataTags();
		// end
		output.add("\t</dict>");
	}

	private void metadataTags() {
		output.add("\t\t<dict>");
		output.add("\t\t\t<key>format</key>");
		output.add("\t\t\t<integer>2</integer>");
		output.add("\t\t\t<key>realTextureFileName</key>");
		output.add("\t\t\t<string>" + keys.get(0) + "</string>");
		output.add("\t\t\t<key>textureFileName</key>");
		output.add("\t\t\t<string>" + keys.get(0) + "</string>");
		output.add("\t\t</dict>");
	}

	private void bodyData(List<String> input) {
		bodyData = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> values = null;
		String[] pair;
		for (String srt : input) {
			pair = srt.split(":");
			if (pair.length == 1) {
				keys.add(srt.trim());
				values = new HashMap<String, String>();
				bodyData.put(srt.trim(), values);
			} else {
				values.put(pair[0].trim(), pair[1].trim());
			}
		}
		if (bodyData.size() != keys.size()) {
			throw new IllegalStateException("Data set not valid");
		}
	}

	private void bodyTags() {
		// the position 0 is the metadata so start from 1
		String[] origin;
		String[] size;
		HashMap<String, String> values = null;
		for (int i = 1; i < bodyData.size(); i++) {
			values = bodyData.get(keys.get(i));
			output.add("\t\t\t<key>" + keys.get(i) + "</key>");
			output.add("\t\t\t<dict>");
			origin = values.get("xy").split(",");
			size = values.get("size").split(",");
			output.add("\t\t\t\t<key>frame</key>");
			output.add("\t\t\t\t<string>{{" + origin[0].trim() + ","
					+ origin[1].trim() + "}" + ",{" + size[0].trim() + ","
					+ size[1].trim() + "}}</string>");
			output.add("\t\t\t\t<key>offset</key>");
			output.add("\t\t\t\t<string>{0,0}</string>");
			output.add("\t\t\t\t<key>rotated</key>");
			output.add("\t\t\t\t<" + values.get("rotate") + "/>");
			output.add("\t\t\t\t<key>sourceColorRect</key>");
			output.add("\t\t\t\t<string>{{0,0},{" + size[0].trim() + ","
					+ size[1].trim() + "}}</string>");
			output.add("\t\t\t\t<key>sourceSize</key>");
			output.add("\t\t\t\t<string>{" + size[0].trim() + ","
					+ size[1].trim() + "}</string>");
			output.add("\t\t\t</dict>");
		}
	}

	public List<String> getPlist() {
		return output;
	}

}
