package cpn.model;

public class CPNArc {
	
	// Input (IN) and output (OUT) relative to ´transition´
	public enum Direction { IN, OUT, }
	
	private String id;
	private CPNPlace place;
	private CPNTransition transition;
	private Direction direction;
	private String expression;
	
	public CPNArc(String id, CPNPlace place, CPNTransition transition,
			Direction direction, String expression) {
		this.id = id;
		this.place = place;
		this.transition = transition;
		this.direction = direction;
		setExpression(expression);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CPNPlace getPlace() {
		return place;
	}

	public void setPlace(CPNPlace place) {
		this.place = place;
	}

	public CPNTransition getTransition() {
		return transition;
	}

	public void setTransition(CPNTransition transition) {
		this.transition = transition;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression.replace("\n", " ");
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("%-20s %-20s", id, place.getName()));
		switch (direction) {
		case IN:
			sb.append(String.format("%-50s", ("[--- " + expression + " --->")));
			break;
		case OUT:
			sb.append(String.format("%-50s", ("<--- " + expression + " ---]")));
			break;
		}
		sb.append(transition.getName());
		
		return sb.toString();
	}
}
