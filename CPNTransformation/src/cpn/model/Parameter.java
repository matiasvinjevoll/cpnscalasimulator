package cpn.model;

public class Parameter extends Argument {
	private String id; // id of port place
	private String name; // name of port place
	private String collection; // initial marking
	
	public Parameter(String id, String name, String collection) {
		super();
		this.id = id;
		this.name = name;
		this.collection = collection;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String toString() {
		return id + ", " + name + ", " + collection;
	}
}
