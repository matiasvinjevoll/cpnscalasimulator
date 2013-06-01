package cpn;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.cpntools.accesscpn.model.Arc;
import org.cpntools.accesscpn.model.FusionGroup;
import org.cpntools.accesscpn.model.HLArcType;
import org.cpntools.accesscpn.model.HLDeclaration;
import org.cpntools.accesscpn.model.Instance;
import org.cpntools.accesscpn.model.Label;
import org.cpntools.accesscpn.model.Page;
import org.cpntools.accesscpn.model.ParameterAssignment;
import org.cpntools.accesscpn.model.PetriNet;
import org.cpntools.accesscpn.model.Place;
import org.cpntools.accesscpn.model.RefPlace;
import org.cpntools.accesscpn.model.ToolInfo;
import org.cpntools.accesscpn.model.Transition;
import org.cpntools.accesscpn.model.importer.DOMParser;

import cpn.model.CPNArc;
import cpn.model.Module;
import cpn.model.CPNPlace;
import cpn.model.CPNTransition;
import cpn.util.Pair;
import cpn.util.Transform;

public class FlattenCPNNet {
	private PetriNet petriNet;

	public FlattenCPNNet(final String file) throws Exception {
		this.petriNet = DOMParser.parse(new URL(file));

	}

	public Module transformPetriNet() {

		Module net = new Module();

		List<String> declarations = new ArrayList<>();
		for (HLDeclaration d : petriNet.declaration()) {
			declarations.add(d.asString());
		}
		net.setDeclarations(declarations);

		HashMap<String, CPNPlace> places = new HashMap<>();
		HashMap<String, CPNTransition> transitions = new HashMap<>();
		for (Page page : petriNet.getPage()) {
			places.putAll(Transform.transformPlaces(page.place()));
			transitions
					.putAll(Transform.transformTransitions(page.transition()));
		}
		net.setPlaces(places);
		net.setTransitions(transitions);

		for (FusionGroup fg : petriNet.getFusionGroups()) {
			fg.getName();
			fg.getInitialMarking();
			for (RefPlace rp : fg.getReferences())
				rp.getInitialMarking().asString();
		}

		HashMap<String, CPNArc> arcs = new HashMap<>();

		Pair<HashMap<String, CPNPlace>, HashMap<String, CPNArc>> fa = Transform
				.transformFusionGroups(petriNet.getFusionGroups(), net);
		places.putAll(fa.first());
		arcs.putAll(fa.second());

		for (Page page : petriNet.getPage()) {
			arcs.putAll(Transform.transformArcs(page.getArc(), net));
			System.out.println("Processing substitution transitions in the module " + page.getName());
			arcs.putAll(Transform.transformInstances(page.instance(), petriNet,
					net));
			System.out.println("\n\n");
		}

		net.setArcs(arcs);

		return net;
	}

	public void netInfo() {

		for (HLDeclaration d : petriNet.declaration()) {
			System.out.println(d.asString());
			System.out.println(d);
		}

		int placeCount = 0;
		int transitionCount = 0;
		int arcCount = 0;
		for (Page page : petriNet.getPage()) {

			System.out.println("Page " + page.getName());

			System.out.println("\tPlaces:");
			for (Place place : page.place()) {
				placeCount += 1;
				System.out.println("\t\t" + place.toString());
				System.out.println("\t\t\tInitial Marking: "
						+ place.getInitialMarking());
				System.out.println("\t\t\tLabel: " + place.getLabel());
				System.out.println("\t\t\tReferences:");
				for (RefPlace refplace : place.getReferences()) {
					System.out.println("\t\t\t\t" + refplace.toString());
				}
			}
			System.out.println();

			System.out.println("\tPort Places:");
			for (RefPlace refplace : page.portPlace()) {
				placeCount += 1;
				System.out.println("\t\t" + refplace.toString());
				System.out.println("\t\t\tInitial Marking: "
						+ refplace.getInitialMarking());
				System.out.println("\t\t\tLabel: " + refplace.getLabel());
				System.out.println("\t\t\tRef: " + refplace.getRef());
			}
			System.out.println();

			System.out.println("\tTransitons:");
			for (Transition transition : page.transition()) {
				transitionCount += 1;
				System.out.println("\t\t" + transition.toString());
			}
			System.out.println();

			System.out.println("\tSubstitution Transitions:");
			for (Instance instance : page.instance()) {
				System.out.println("\t\t" + instance.toString());
				System.out.println("---------------------------- INFO ---------------------");
				System.out.println(instance.getName().getText());
				System.out.println("-------------------------------------------------------");
				
				
				
				System.out.println("\t\tParameter Assignments:");
				for (ParameterAssignment paramAssign : instance
						.getParameterAssignment()) {
					System.out.println("\t\t\tSocket: "
							+ paramAssign.getParameter() + " port: "
							+ paramAssign.getValue());
				}
				transitionCount += 1;
			}
			System.out.println();

			System.out.println("\tArcs:");
			for (Arc arc : page.getArc()) {
				System.out.println("\t\t" + arc.toString());
				System.out.println("\t\t\tInscription: "
						+ arc.getHlinscription());
				System.out.println("\t\t\tPlace: " + arc.getPlaceNode());
				// System.out.println("\t\t\tTransition: " +
				// arc.getTransition());
				System.out.println("\t\t\tSource: " + arc.getSource());
				System.out.println("\t\t\tTarget: " + arc.getTarget());

				if (arc.getSource() instanceof Transition
						|| arc.getTarget() instanceof Transition) {
					arcCount += 1;
					if (arc.getKind().compareTo(HLArcType.TEST) == 0)
						arcCount += 1;
				}
			}

			System.out.println();
			System.out.println("--------------");
		}
		System.out.println("Fusion Groups:");
		for (FusionGroup fg : petriNet.getFusionGroups()) {
			placeCount += 1;
			System.out.println(fg.getName());
			System.out.println("Initial Marking (doesn't work: "
					+ fg.getInitialMarking());
			for (RefPlace rp : fg.getReferences()) {
				System.out.println(rp.getName());
				System.out.println("Initial Makring works: "
						+ rp.getInitialMarking().asString());
			}
			System.out.println();
		}

		System.out.println("Places: " + placeCount);
		System.out.println("Transitions: " + transitionCount);
		System.out.println("Arcs: " + arcCount);
	}
}
