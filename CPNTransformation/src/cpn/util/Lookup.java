package cpn.util;

import java.util.List;

import org.cpntools.accesscpn.model.Page;
import org.cpntools.accesscpn.model.ParameterAssignment;
import org.cpntools.accesscpn.model.RefPlace;

public class Lookup {

	public static Page getPageById(List<Page> pages, String id) {
		Page page = null;

		for (Page p : pages)
			if (p.getId().equals(id))
				page = p;

		if (page == null)
			throw new NullPointerException("Page with id " + id
					+ " is not present in the net.");

		return page;
	}

	public static RefPlace getPortPlaceById(Page page, String id) {
		RefPlace refPlace = null;

		for (RefPlace rp : page.portPlace())
			if (rp.getId().equals(id))
				refPlace = rp;

		if (refPlace == null)
			throw new NullPointerException("Port place with id " + id
					+ " is not present in the page " + page.getId() + ".");

		return refPlace;
	}

	public static ParameterAssignment getParamAssignmentByParam(
			List<ParameterAssignment> paramAssignments, String param) {
		ParameterAssignment pa = null;
		for (ParameterAssignment p : paramAssignments)
			if (p.getParameter().equals(param))
				pa = p;

		if (pa == null)
			throw new NullPointerException(
					"Parameter paramAssignments does not contain element with parameter "
							+ param + ".");

		return pa;
	}

	public static ParameterAssignment getParamAssignmentByValue(
			List<ParameterAssignment> paramAssignments, String value) {
		ParameterAssignment pa = null;
		for (ParameterAssignment p : paramAssignments)
			if (p.getValue().equals(value))
				pa = p;

		if (pa == null)
			throw new NullPointerException(
					"Parameter paramAssignments does not contain element with parameter "
							+ value + ".");

		return pa;
	}
}
