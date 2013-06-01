package cpn.model;

public class CPNPlace extends Argument {
	private String name;
	private String id;
	private String initialMarking;
	
	public CPNPlace(String name, String id, String initialMarking) {
		this.name = name.replace("\n", " ");
		this.id = id;
		this.initialMarking = initialMarking;
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

	public String getInitialMarking() {
		return initialMarking;
	}

	public void setInitialMarking(String initialMarking) {
		this.initialMarking = initialMarking;
	}
	
	public String toString() {
		return String.format("%-20s %-20s [%s]", name, "("+id+")", initialMarking);
	}
	
}
