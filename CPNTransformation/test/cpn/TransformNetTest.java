package cpn;

import static org.junit.Assert.assertEquals;

import org.cpntools.accesscpn.model.Arc;
import org.cpntools.accesscpn.model.HLArcType;
import org.cpntools.accesscpn.model.Page;
import org.cpntools.accesscpn.model.PetriNet;
import org.cpntools.accesscpn.model.Place;
import org.cpntools.accesscpn.model.RefPlace;
import org.cpntools.accesscpn.model.Transition;
import org.junit.Before;
import org.junit.Test;

import cpn.model.CPNNet;

public class TransformNetTest {

	private CPNNet cpnNet;
	private PetriNet petriNet;

	@Before
	public void setup() throws Exception {
		String file = "file:\\Users\\Matias\\Skule\\"
				+ "Master\\CPN Models\\ImplementationTestModels\\CodegenModels\\babel_scala.cpn";
		TransformNet tnet = new TransformNet(file);
		cpnNet = tnet.transformNet();
		petriNet = tnet.petriNet;
	}

	@Test
	public void equalNumTransitions() {
		int n = 0;
		for (Page p : petriNet.getPage())
			for (Transition t : p.transition())
				n++;
		assertEquals(n, cpnNet.numTransitions());
	}

	@Test
	public void equalNumFusionGroupGlobalPlaces() {
		assertEquals(petriNet.getFusionGroups().size(), cpnNet.getPlaces()
				.values().size());
	}

	@Test
	public void equalNumPlaces() {
		int n = 0;
		for (Page p : petriNet.getPage())
			for (Place pl : p.place())
				n++;
		assertEquals(n, cpnNet.numPlaces());
	}

	@Test
	public void equalNumPortParam() {
		int n = 0;
		for (Page p : petriNet.getPage())
			for (RefPlace pp : p.portPlace())
				n++;
		assertEquals(n, cpnNet.numParameters());
	}

	@Test
	public void equalNumArcs() {
		int n = 0;
		for (Page p : petriNet.getPage()) {
			for (Arc a : p.getArc()) {
				if ((a.getSource() instanceof Place || a.getSource() instanceof Transition)
						&& (a.getTarget() instanceof Place || a.getTarget() instanceof Transition)) {
					n++;
					if (a.getKind().compareTo(HLArcType.TEST) == 0)
						n++;
				}
			}
			for (RefPlace fp : p.fusionGroup()) {
				for (Arc a : fp.getSourceArc()) {
					if (a.getTarget() instanceof Transition)
						n++;
					if (a.getKind().compareTo(HLArcType.TEST) == 0)
						n++;
				}
				for (Arc a : fp.getTargetArc()) {
					if (a.getSource() instanceof Transition)
						n++;
					if (a.getKind().compareTo(HLArcType.TEST) == 0)
						n++;
				}
			}
		}
		assertEquals(n, cpnNet.numArcs());
	}

	@Test
	public void equalPortParamArcs() {
		int n = 0;
		for (Page p : petriNet.getPage())
			for (RefPlace pp : p.portPlace()) {
				for (Arc a : pp.getTargetArc()) {
					if (a.getSource() instanceof Transition)
						n++;
					if (a.getKind().compareTo(HLArcType.TEST) == 0)
						n++;
				}
				for (Arc a : pp.getSourceArc()) {
					if (a.getTarget() instanceof Transition)
						n++;
					if (a.getKind().compareTo(HLArcType.TEST) == 0)
						n++;
				}
			}
		assertEquals(n, cpnNet.numParamArcs());
	}

}
