package cpn.model;

import java.util.HashMap;
import java.util.List;

public class CPNNet {

	private List<String> declarations;
	private HashMap<String, Module> modules;
	private HashMap<String, CPNPlace> places; // representing fusion groups
	
	public List<String> getDeclarations() {
		return declarations;
	}
	public void setDeclarations(List<String> declarations) {
		this.declarations = declarations;
	}
	public HashMap<String, Module> getModules() {
		return modules;
	}
	public void setModules(HashMap<String, Module> modules) {
		this.modules = modules;
	}
	public HashMap<String, CPNPlace> getPlaces() {
		return places;
	}
	public void setPlaces(HashMap<String, CPNPlace> places) {
		this.places = places;
	}
	
	public int numTransitions() {
		int n = 0;
		for(Module m : modules.values()){
			n += m.getTransitions().size();
		}
		return n;
	}
	
	public int numPlaces() {
		int n = 0;
		for(Module m : modules.values()){
			n += m.getPlaces().size();
		}
		return n;
	}
	
	public int numParameters() {
		int n = 0;
		for(Module m : modules.values()){
			n += m.getParams().size();
		}
		return n;
	}
	
	public int numArcs() {
		int n = 0;
		for(Module m : modules.values()){
			n += m.getArcs().size();
		}
		return n;
	}

	public int numParamArcs() {
		int n = 0;
		for(Module m : modules.values()){
			n += m.getParamArcs().size();
		}
		return n;
	}
	
	public Boolean findParamArc(String id) {
		Boolean found = false;
		for(Module m : modules.values()){
			for(ParamArc pa:m.getParamArcs()) {
				if(pa.getId().equals(id))
					found = true;
			}
		}
		return found;
	}
	
}
