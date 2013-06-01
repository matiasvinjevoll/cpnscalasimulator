package cpn.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import cpn.model.CPNArc.Direction;

public class Module {
	private String name;
	private String id;
	private List<String> declarations; // should be removed
	private HashMap<String, CPNPlace> places;
	private HashMap<String, CPNTransition> transitions;
	private HashMap<String, CPNArc> arcs; // "regular" arcs
	private List<SubstitutionTransition> substitutionTransitions;
	private List<Parameter> params; // info for parameters to the module
	private List<ParamArc> paramArcs; // arc from parameter to transitions
	private int superModules; // Number of super modules

	public Module() {
		declarations = new ArrayList<>();
		places = new HashMap<>();
		transitions = new HashMap<>();
		arcs = new HashMap<>();
		substitutionTransitions = new ArrayList<>();
		params = new ArrayList<>();
		paramArcs = new ArrayList<>();
		superModules = 0;
	}

	public Module(List<String> declarations, HashMap<String, CPNPlace> places,
			HashMap<String, CPNTransition> transitions,
			HashMap<String, CPNArc> arcs,
			List<SubstitutionTransition> substitutionTransitions, String name) {
		setDeclarations(declarations);
		this.places = places;
		this.transitions = transitions;
		this.arcs = arcs;
		this.substitutionTransitions = substitutionTransitions;
		this.name = name.replace(" ", "");
		superModules = 0;
	}
	
	public int getSuperModules() {
		return superModules;
	}
	public void incSuperModule() {
		superModules++;
	}

	public List<ParamArc> getParamArcs() {
		return paramArcs;
	}

	public void setParamArcs(List<ParamArc> paramArcs) {
		this.paramArcs = paramArcs;

		for (ParamArc arc : paramArcs)
			if (arc.getDirection().equals(Direction.IN))
				arc.getTransition().addInputArc(arc.getId());
			else if (arc.getDirection().equals(Direction.OUT))
				arc.getTransition().addOutputArc(arc.getId());
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

	public List<Parameter> getParams() {
		return params;
	}

	public void setParams(List<Parameter> params) {
		this.params = params;
	}

	public void setName(String name) {
		this.name = name.replace(" ", "");
	}

	public List<SubstitutionTransition> getSubstitutionTransitions() {
		return substitutionTransitions;
	}

	public void setSubstitutionTransitions(
			List<SubstitutionTransition> substitutionTransitions) {
		this.substitutionTransitions = substitutionTransitions;
	}

	public List<String> getDeclarations() {
		return declarations;
	}

	public void setDeclarations(List<String> declarations) {
		List<String> temp = new ArrayList<>();
		for (String decl : declarations)
			temp.add(decl);
		this.declarations = temp;
	}

	public HashMap<String, CPNPlace> getPlaces() {
		return places;
	}

	public void setPlaces(HashMap<String, CPNPlace> places) {
		this.places = places;
	}

	public HashMap<String, CPNTransition> getTransitions() {
		return transitions;
	}

	public void setTransitions(HashMap<String, CPNTransition> transitions) {
		this.transitions = transitions;
	}

	public HashMap<String, CPNArc> getArcs() {
		return arcs;
	}

	public void setArcs(HashMap<String, CPNArc> arcs) {
		this.arcs = arcs;
		for (CPNArc arc : arcs.values())
			if (arc.getDirection().equals(Direction.IN))
				arc.getTransition().addInputArc(arc.getId());
			else if (arc.getDirection().equals(Direction.OUT))
				arc.getTransition().addOutputArc(arc.getId());
	}

	public void addArcs(HashMap<String, CPNArc> arcs) {
		this.arcs.putAll(arcs);
		for (CPNArc arc : arcs.values())
			if (arc.getDirection().equals(Direction.IN))
				arc.getTransition().addInputArc(arc.getId());
			else if (arc.getDirection().equals(Direction.OUT))
				arc.getTransition().addOutputArc(arc.getId());
	}

	public Parameter getParameter(String id) {
		Parameter param = null;
		for (Parameter p : params) {
			if (p.getId().equals(id))
				param = p;
		}
		if (param == null)
			throw new NoSuchElementException("Param does not exist.");
		return param;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("---- Module " + name + " ---\n");

		/*
		 * sb.append("\tDeclarations:\n"); for (String decl : declarations)
		 * sb.append("\t\t" + decl + "\n"); sb.append("\n");
		 */

		sb.append("\tSubstitution Transitions:\n");
		for (SubstitutionTransition st : substitutionTransitions)
			sb.append("\t\t" + st.toString() + "\n");
		sb.append("\n");

		sb.append("\tParams:\n");
		for (Parameter param : params)
			sb.append("\t\t" + param.toString() + "\n");
		sb.append("\n");

		sb.append("\tPlaces:\n");
		for (CPNPlace p : places.values())
			sb.append("\t\t" + p.getName() + "\n");
		sb.append("\n");

		sb.append("\tTransitions:\n");
		for (CPNTransition t : transitions.values())
			sb.append("\t\t" + t.getName() + "\n");
		sb.append("\n");

		sb.append("\tArcs:\n");
		for (CPNArc a : arcs.values())
			sb.append("\t\t" + a.toString() + "\n");

		sb.append("\tParamArcs:\n");
		for (ParamArc pa : paramArcs)
			sb.append("\t\t" + pa.toString() + "\n");

		return sb.toString();
	}
}
