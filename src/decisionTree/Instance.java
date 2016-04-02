package decisionTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Instance {

	public String className = "";
	public String classifiedName = "";
	public Map<String, Boolean> attributes = new HashMap<String, Boolean>();

	public Instance(String cN, Scanner s, List<String> attributeNames) {
		className = cN;
		int i = 0;
		while (s.hasNext()) {
			attributes.put(attributeNames.get(i), s.nextBoolean());
			i++;
		}

	}

	public boolean correct() {
		return className.equals(classifiedName) ? true : false;
	}

	public boolean get(String attribute) {
		return attributes.get(attribute);
	}

	@Override
	public String toString() {
		className = className.concat(classifiedName + ", ");

		for (String key : attributes.keySet()) {
			className = className.concat(String.valueOf(attributes.get(key)) + ", ");
		}

		return className;
	}

}
