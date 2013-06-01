package cpn.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.cpntools.accesscpn.model.Arc;
import org.cpntools.accesscpn.model.FusionGroup;
import org.cpntools.accesscpn.model.HLArcType;
import org.cpntools.accesscpn.model.Instance;
import org.cpntools.accesscpn.model.Page;
import org.cpntools.accesscpn.model.ParameterAssignment;
import org.cpntools.accesscpn.model.PetriNet;
import org.cpntools.accesscpn.model.Place;
import org.cpntools.accesscpn.model.RefPlace;
import org.cpntools.accesscpn.model.Transition;

import cpn.model.CPNArc;
import cpn.model.CPNNet;
import cpn.model.Module;
import cpn.model.ParamArc;
import cpn.model.Parameter;
import cpn.model.CPNPlace;
import cpn.model.CPNTransition;
import cpn.model.CPNArc.Direction;

public class Transform {

	/**
	 * Transform places from ´PetriNet´. Does not include port places
	 * 
	 * @param places
	 * @return ´HashMap<String, CPNPlace>´ where key is the id of the
	 *         transformed ´Place´
	 */
	public static HashMap<String, CPNPlace> transformPlaces(
			final Iterable<Place> places) {
		HashMap<String, CPNPlace> cpnPlaces = new HashMap<>();

		for (Place p : places)
			cpnPlaces.put(p.getId(),
					new CPNPlace(p.getName().asString(), p.getId(), p
							.getInitialMarking().getText()));

		return cpnPlaces;
	}

	/**
	 * Transform transitions from ´PetriNet´
	 * 
	 * @param transitions
	 * @return ´HashMap<String, CPNTransition>´ where key is the id of the
	 *         transformed ´Transition´
	 */
	public static HashMap<String, CPNTransition> transformTransitions(
			final Iterable<Transition> transitions) {
		HashMap<String, CPNTransition> cpnTransitions = new HashMap<>();

		for (Transition t : transitions)
			cpnTransitions.put(t.getId(), new CPNTransition(t.getName()
					.asString(), t.getId(), t.getCondition().asString()));

		return cpnTransitions;
	}

	public static List<ParamArc> transformParamArcs(List<Arc> arcs,
			Parameter param, HashMap<String, CPNTransition> transitions,
			Direction dir) {
		List<ParamArc> paramArcs = new ArrayList<>();
		for (Arc arc : arcs) {
			if ((dir.equals(Direction.IN) && arc.getTarget() instanceof Transition)
					|| (dir.equals(Direction.OUT) && arc.getSource() instanceof Transition)) {
				CPNTransition transition = transitions.get(arc.getTransition()
						.getId());
				if (arc.getKind().compareTo(HLArcType.TEST) == 0) {
					String idIn = arc.getId() + "IN";
					String idOut = arc.getId() + "OUT";
					paramArcs.add(new ParamArc(idIn, param, transition,
							Direction.IN, arc.getHlinscription().asString()));
					paramArcs.add(new ParamArc(idOut, param, transition,
							Direction.OUT, arc.getHlinscription().asString()));
				} else {
					paramArcs.add(new ParamArc(arc.getId(), param, transition,
							dir, arc.getHlinscription().asString()));
				}
			}
		}

		return paramArcs;
	}

	/**
	 * Transform acrs from ´PetriNet´. Does not include arcs between sockets and
	 * substitution transitions, arcs to/from port places and to/from fusion places.
	 * Places and transitions must have been added to parameter ´net´.
	 * 
	 * @param arcs
	 * @param net
	 * @return ´List<CPNArc>´
	 */
	public static HashMap<String, CPNArc> transformArcs(
			final Iterable<Arc> arcs, final Module net) {
		HashMap<String, CPNArc> cpnArcs = new HashMap<>();

		for (Arc a : arcs) {
			if ((a.getSource() instanceof Place || a.getSource() instanceof Transition)
					&& (a.getTarget() instanceof Place || a.getTarget() instanceof Transition)) {

				CPNPlace p = net.getPlaces().get(a.getPlaceNode().getId());
				CPNTransition t = net.getTransitions().get(
						a.getTransition().getId());

				if (a.getKind().compareTo(HLArcType.TEST) == 0) {
					String idIn = a.getId() + "IN";
					String idOut = a.getId() + "OUT";
					cpnArcs.put(idIn, new CPNArc(idIn, p, t, Direction.IN, a
							.getHlinscription().asString()));
					cpnArcs.put(idOut, new CPNArc(idOut, p, t, Direction.OUT, a
							.getHlinscription().asString()));
				} else {
					Direction dir = Direction.IN;
					if (a.getSource() instanceof Transition)
						dir = Direction.OUT;

					cpnArcs.put(a.getId(), new CPNArc(a.getId(), p, t, dir, a
							.getHlinscription().asString()));
				}

			}
		}

		return cpnArcs;
	}

	private static ArrayList<CPNArc> transformArcByPlace(final Arc arc,
			final CPNPlace place, final Direction direction, final Module net) {
		CPNTransition transition = net.getTransitions().get(
				arc.getTransition().getId());

		ArrayList<CPNArc> arcs = new ArrayList<CPNArc>();
		Direction dir = direction;
		if (arc.getKind().compareTo(HLArcType.TEST) == 0) {
			String idIn = arc.getId() + "IN";
			String idOut = arc.getId() + "OUT";

			arcs.add(new CPNArc(idIn, place, transition, Direction.IN, arc
					.getHlinscription().asString()));

			arcs.add(new CPNArc(idOut, place, transition, Direction.OUT, arc
					.getHlinscription().asString()));
		} else {
			arcs.add(new CPNArc(arc.getId(), place, transition, dir, arc
					.getHlinscription().asString()));
		}

		return arcs;
	}

	// TODO: Could be made tail-recursive
	private static HashMap<String, CPNArc> arcsOfPortToSocketRec(
			final CPNPlace socket, final RefPlace port,
			final PetriNet petriNet, final Module net) {

		HashMap<String, CPNArc> arcs = new HashMap<>();

		// List of paris containing all "regular" arcs connected to the port
		// place,
		// and their direction
		List<Pair<Arc, Direction>> portArcs = new ArrayList<Pair<Arc, Direction>>();
		for (Arc a : port.getSourceArc())
			portArcs.add(new Pair<Arc, Direction>(a, Direction.IN));
		for (Arc a : port.getTargetArc())
			portArcs.add(new Pair<Arc, Direction>(a, Direction.OUT));

		// Process each "regular" arc connected to the port place
		for (Pair<Arc, Direction> arcDir : portArcs) {

			Arc arc = arcDir.first();
			Direction direction = arcDir.second();

			if (arc.getSource() instanceof Transition
					|| arc.getTarget() instanceof Transition) {
				// If the current arc is connected to a regular transition, then
				// the arc (or two arcs if it is an I/O arc) is added to the
				// final list of arcs
				System.out.println("Add arc to net between "
						+ arc.getTransition().getName() + " and "
						+ socket.getName());
				ArrayList<CPNArc> temp = transformArcByPlace(arc, socket,
						direction, net);
				for (CPNArc t : temp)
					arcs.put(t.getId(), t);
			} else {
				// If the current ´arc´ is not connected to a ´Transition´, it
				// is connected to an ´Instance´: port is socket as well!
				Instance i = null;
				if (direction.equals(Direction.IN))
					i = (Instance) arc.getTarget();
				else
					i = (Instance) arc.getSource();

				// Can there be multiple pa? - seems not...
				ParameterAssignment pa = Lookup.getParamAssignmentByParam(
						i.getParameterAssignment(), port.getId());

				Page subPage = Lookup.getPageById(petriNet.getPage(),
						i.getSubPageID());

				RefPlace port2 = Lookup
						.getPortPlaceById(subPage, pa.getValue());

				arcs.putAll(arcsOfPortToSocketRec(socket, port2, petriNet, net));
			}
		}

		return arcs;
	}

	private static HashMap<String, CPNArc> arcsOfPortToSocket(
			final CPNPlace socket, final RefPlace port,
			final PetriNet petriNet, final Module net) {
		HashMap<String, CPNArc> arcs = new HashMap<>();

		arcs.putAll(arcsOfPortToSocketRec(socket, port, petriNet, net));

		return arcs;
	}

	private static CPNArc alterArcId(CPNArc arc, String newId) {
		return new CPNArc(newId, arc.getPlace(), arc.getTransition(),
				arc.getDirection(), arc.getExpression());
	}

	/**
	 * Transform instances (substitution transitions), by merging socket/port
	 * places.
	 * 
	 * @param instances
	 * @param petriNet
	 * @param net
	 * @return ´List<CPNArc>´
	 */
	public static HashMap<String, CPNArc> transformInstances(
			final Iterable<Instance> instances, final PetriNet petriNet,
			final Module net) {

		HashMap<String, CPNArc> arcs = new HashMap<>();

		int counter = 0;

		for (Instance i : instances) { // For each subsitution transition
			System.out.println("Substitution transition " + i.getName());
			Page subPage = Lookup.getPageById(petriNet.getPage(),
					i.getSubPageID());
			for (ParameterAssignment pa : i.getParameterAssignment()) {
				// ParameterAssignment = class with id to a socket (parameter)
				// and id to a port (value)
				CPNPlace socket = net.getPlaces().get(pa.getParameter());

				// If ´cpnPlace´ is null, then the socket place (with id
				// ´pa.getParameter()´) is also a port
				// place, and should not be considered since it is a link
				// between a port place in a
				// super-page and a port place in a sub-page.
				// Only places that are sockets only, should be added to the net
				if (socket != null) {
					RefPlace port = Lookup.getPortPlaceById(subPage,
							pa.getValue());
					System.out.println("Processing socket/port relation: "
							+ socket.getName() + "/" + port.getName());
					// arcs.putAll(arcsOfPortToSocket(socket, port, petriNet,
					// net));
					System.out.println();
					HashMap<String, CPNArc> temp = arcsOfPortToSocket(socket,
							port, petriNet, net);
					for (String key : temp.keySet()) {
						String newId = key + counter;
						CPNArc arc = alterArcId(temp.get(key), newId);
						arcs.put(newId, arc);
						counter++;
					}
				}
			}
		}
		for (CPNArc a : arcs.values())
			System.out.println(a.toString());
		return arcs;
	}

	/**
	 * Converts all fusion groups to places with name of the fusion groups and
	 * map arcs to/from members of the fusion group to the new places.
	 * 
	 * Ports (and sockets) cannot be part of a fusion set, so processing fusion
	 * sets will not be mixed with processing of ports even though both fusion
	 * places and port places are represented as ´RefPlace´. Hence, an arc
	 * to/from a fusion place is always from/to a ´Transition´, never an
	 * ´Instance´.
	 * 
	 * @return Pair consisting of new places and arcs
	 */
	public static Pair<HashMap<String, CPNPlace>, HashMap<String, CPNArc>> transformFusionGroups(
			final List<FusionGroup> fusionGroups, final Module net) {

		HashMap<String, CPNPlace> places = new HashMap<>();
		HashMap<String, CPNArc> arcs = new HashMap<>();
		for (FusionGroup fg : fusionGroups) {
			// ´fg.getInitialMarking()´ does not work...
			CPNPlace place = new CPNPlace(fg.getName().asString(), fg.getId(),
					fg.getReferences().get(0).getInitialMarking().asString());

			places.put(fg.getId(), place);

			for (RefPlace rp : fg.getReferences()) {
				for (Arc arc : rp.getSourceArc()) {
					ArrayList<CPNArc> temp = transformArcByPlace(arc, place,
							Direction.IN, net);
					for (CPNArc t : temp) {
						arcs.put(t.getId(), t);
					}
				}
				for (Arc arc : rp.getTargetArc()) {
					ArrayList<CPNArc> temp = transformArcByPlace(arc, place,
							Direction.OUT, net);
					for (CPNArc t : temp) {
						arcs.put(t.getId(), t);
					}
				}
			}
		}
		return new Pair<>(places, arcs);
	}

	private static Pair<Module, CPNTransition> findModuleAndTransition(CPNNet net, String transitionId) {
		CPNTransition transition = null;
		Module module = null;

		Boolean found = false;
		for (Module m : net.getModules().values()) {
			if (found)
				break;
			else {
				transition = m.getTransitions().get(transitionId);
				if (transition != null) {
					module = m;
					found = true;
				}
			}
		}

		if (transition == null)
			throw new NullPointerException("Transition with id " + transitionId
					+ " could not be found in any modules.");
		
		return new Pair<Module, CPNTransition>(module,transition);
	}
	
	private static HashMap<String, CPNArc> transformArc(Arc arc, CPNPlace place, CPNTransition transition) {
		HashMap<String, CPNArc> arcs = new HashMap<>();
		
		if (arc.getKind().compareTo(HLArcType.TEST) == 0) {
			String idIn = arc.getId() + "IN";
			String idOut = arc.getId() + "OUT";
			arcs.put(idIn, new CPNArc(idIn, place, transition, Direction.IN, arc
					.getHlinscription().asString()));
			arcs.put(idOut, new CPNArc(idOut, place, transition, Direction.OUT, arc
					.getHlinscription().asString()));
		} else {
			Direction dir = Direction.IN;
			if (arc.getSource() instanceof Transition)
				dir = Direction.OUT;

			arcs.put(arc.getId(), new CPNArc(arc.getId(), place, transition, dir, arc
					.getHlinscription().asString()));
		}
		
		
		return arcs;
	}

	public static HashMap<String, CPNPlace> transformFusionGroups2(
			final List<FusionGroup> fusionGroups, final CPNNet net) {

		HashMap<String, CPNPlace> places = new HashMap<>();

		for (FusionGroup fg : fusionGroups) {
			// ´fg.getInitialMarking()´ does not work...
			CPNPlace place = new CPNPlace(fg.getName().asString(), fg.getId(),
					fg.getReferences().get(0).getInitialMarking().asString());

			places.put(fg.getId(), place);

			for (RefPlace rp : fg.getReferences()) {
				for (Arc arc : rp.getSourceArc()) {
					Pair<Module,CPNTransition> modTrans = findModuleAndTransition(net, arc
							.getTransition().getId());
					HashMap<String, CPNArc> arcs = transformArc(arc, place, modTrans.second());
					modTrans.first().addArcs(arcs);
				}
				for (Arc arc : rp.getTargetArc()) {
					Pair<Module,CPNTransition> modTrans = findModuleAndTransition(net, arc
							.getTransition().getId());
					HashMap<String, CPNArc> arcs = transformArc(arc, place, modTrans.second());
					modTrans.first().addArcs(arcs);
				}
			}

		}
		return places;
	}
}
