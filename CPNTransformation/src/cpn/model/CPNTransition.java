package cpn.model;

import java.util.ArrayList;
import java.util.List;

public class CPNTransition {
	private String name;
	private String id;
	private String guard;
	private List<String> in;
	private List<String> out;
	
	public CPNTransition(String name, String id, String guard) {
		this.name = name.replace("\n", " ");
		this.id = id;
		this.guard = guard;
		in = new ArrayList<>();
		out = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGuard() {
		return guard;
	}

	public void setGuard(String guard) {
		this.guard = guard;
	}
	
	public List<String> getInputArcs() {
		return in;
	}
	
	public void addInputArc(String arcId) {
		in.add(arcId);
	}

	public List<String> getOutputArcs() {
		return out;
	}

	public void addOutputArc(String arcId) {
		out.add(arcId);
	}

	public String toString() {
		return String.format("%-20s %-20s %-20s \n\tInput arcs:\n\t\t%s\n\n\tOutput arcs:\n\t\t%s",
				name, "("+id+")", guard, in.toString(), out.toString());
	}
}
