package cpn;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.cpntools.accesscpn.model.Arc;
import org.cpntools.accesscpn.model.FusionGroup;
import org.cpntools.accesscpn.model.HLDeclaration;
import org.cpntools.accesscpn.model.Instance;
import org.cpntools.accesscpn.model.Page;
import org.cpntools.accesscpn.model.ParameterAssignment;
import org.cpntools.accesscpn.model.PetriNet;
import org.cpntools.accesscpn.model.RefPlace;
import org.cpntools.accesscpn.model.importer.DOMParser;

import cpn.model.Argument;
import cpn.model.CPNArc;
import cpn.model.CPNArc.Direction;
import cpn.model.Module;
import cpn.model.CPNPlace;
import cpn.model.CPNNet;
import cpn.model.CPNTransition;
import cpn.model.Parameter;
import cpn.model.SubstitutionTransition;
import cpn.util.Lookup;
import cpn.util.Pair;
import cpn.util.Transform;
import cpn.model.ParamArc;

public class TransformNet {
	protected PetriNet petriNet;
	
	public TransformNet(final String file) throws Exception {
		this.petriNet = DOMParser.parse(new URL(file));
	}

	public CPNNet transformNet() {

		HashMap<String, Module> modules = new HashMap<String, Module>();
		for (Page page : petriNet.getPage()) {
			Module module = new Module();

			// Set the id of the module
			module.setId(page.getId());

			// Set the name of the module
			module.setName(page.getName().getText());

			// Add regular places and transitions of the module
			HashMap<String, CPNPlace> places = new HashMap<>();
			HashMap<String, CPNTransition> transitions = new HashMap<>();
			places.putAll(Transform.transformPlaces(page.place()));
			transitions
					.putAll(Transform.transformTransitions(page.transition()));
			module.setPlaces(places);
			module.setTransitions(transitions);

			// Add regular arcs of the module
			HashMap<String, CPNArc> arcs = new HashMap<>();
			arcs.putAll(Transform.transformArcs(page.getArc(), module));
			module.setArcs(arcs);

			// Add parameters and parameter arcs to the module
			List<Parameter> params = new ArrayList<>();
			List<ParamArc> paramArcs = new ArrayList<>();
			for (RefPlace port : page.portPlace()) {
				Parameter param = new Parameter(port.getId(), port.getName()
						.asString().toLowerCase(), port.getInitialMarking()
						.asString());
				params.add(param);

				paramArcs.addAll(Transform.transformParamArcs(port.getSourceArc(), param,
						module.getTransitions(), Direction.IN));
				paramArcs.addAll(Transform.transformParamArcs(port.getTargetArc(), param,
						module.getTransitions(), Direction.OUT));
			}
			module.setParams(params);
			module.setParamArcs(paramArcs);

			modules.put(page.getId(), module);
		}

		// Add substitution transitions for modules
		for (Page page : petriNet.getPage()) {
			Module module = modules.get(page.getId());
			List<SubstitutionTransition> substTrans = new ArrayList<>();
			for (Instance inst : page.instance()) {

				Module sub = modules.get(inst.getSubPageID());
				sub.incSuperModule();

				List<ParameterAssignment> pal = inst.getParameterAssignment();

				// Add arguments to substitution transition - the socket place, CPNPlace,
				// or parameter, Parameter, if socket is also a port.
				List<Argument> args = new ArrayList<>();
				for (Parameter param : sub.getParams()) {
					ParameterAssignment pa = Lookup.getParamAssignmentByValue(
							pal, param.getId());
					CPNPlace placeArg = module.getPlaces().get(
							pa.getParameter());
					if (placeArg == null) {
						// Then the param is a param from this (´module´)
						args.add(module.getParameter(pa.getParameter()));
					} else {
						args.add(placeArg);
					}
				}
				substTrans.add(new SubstitutionTransition(sub, args));
			}
			module.setSubstitutionTransitions(substTrans);
		}
		
		CPNNet net = new CPNNet();
		
		List<String> declarations = new ArrayList<>();
		for (HLDeclaration d : petriNet.declaration()) {
			declarations.add(d.asString());
		}
		net.setDeclarations(declarations);
		
		net.setModules(modules);
		
		HashMap<String, CPNPlace> fusionPlaces = Transform
				.transformFusionGroups2(petriNet.getFusionGroups(), net);
		net.setPlaces(fusionPlaces);
		
		return net;
	}
}
