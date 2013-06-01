package cpn.model;

import cpn.model.CPNArc.Direction;

public class ParamArc {
	private String id;
	private Parameter param;
	private CPNTransition transition;
	private Direction direction;
	private String expression;
	
	public ParamArc(String id, Parameter param, CPNTransition transition,
			Direction direction, String expression) {
		super();
		this.id = id;
		this.param = param;
		this.transition = transition;
		this.direction = direction;
		this.expression = expression;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Parameter getParam() {
		return param;
	}
	public void setParam(Parameter param) {
		this.param = param;
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
		this.expression = expression;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("%-20s %-20s", id, param.getName()));
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
