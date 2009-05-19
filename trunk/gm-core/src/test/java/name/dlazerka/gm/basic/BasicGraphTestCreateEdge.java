/*
 * GraphMagic, package for scientists working in graph theory.
 * Copyright (C) 2009 Dzmitry Lazerka dlazerka@dlazerka.name
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Author: Dzmitry Lazerka dlazerka@dlazerka.name
 */

package name.dlazerka.gm.basic;

import name.dlazerka.gm.Vertex;
import name.dlazerka.gm.exception.EdgeCreateException;
import org.junit.Assert;
import static org.junit.Assume.assumeNoException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
@RunWith(Parameterized.class)
public class BasicGraphTestCreateEdge {
	private final boolean directed;
	private final boolean multi;
	private final boolean successfulCreate;
	private final boolean successfulCreateInverse;

	public BasicGraphTestCreateEdge(boolean directed, boolean multi, boolean successfulCreate, boolean successfulCreateInverse) {
		this.directed = directed;
		this.multi = multi;
		this.successfulCreate = successfulCreate;
		this.successfulCreateInverse = successfulCreateInverse;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		LinkedList<Object[]> result = new LinkedList<Object[]>();

		result.add(new Object[]{true, true, true, true});
		result.add(new Object[]{true, true, true, true});
		result.add(new Object[]{true, false, false, true});
		result.add(new Object[]{true, false, false, true});
		result.add(new Object[]{false, false, false, false});
		result.add(new Object[]{false, false, false, false});
		result.add(new Object[]{false, true, true, true});
		result.add(new Object[]{false, true, true, true});

		return result;
	}

	@Test
	public void testAddMultipleEdge() {
		BasicGraph basicGraph = new BasicGraph();
		basicGraph.setDirected(directed);
		basicGraph.setMulti(multi);

		Vertex vertex1 = basicGraph.createVertex();
		Vertex vertex2 = basicGraph.createVertex();

		try {
			basicGraph.createEdge(vertex1, vertex2);
		} catch (EdgeCreateException e) {
			assumeNoException(e);
		}

		try {
			basicGraph.createEdge(vertex1, vertex2);
			if (!successfulCreate) {
				Assert.fail();
			}
		} catch (EdgeCreateException e) {
			if (successfulCreate) {
				assumeNoException(e);
			}
		}
	}

	@Test
	public void testAddInverseEdge() {
		BasicGraph basicGraph = new BasicGraph();
		basicGraph.setDirected(directed);
		basicGraph.setMulti(multi);

		Vertex vertex1 = basicGraph.createVertex();
		Vertex vertex2 = basicGraph.createVertex();

		try {
			basicGraph.createEdge(vertex1, vertex2);
		} catch (EdgeCreateException e) {
			assumeNoException(e);
		}

		try {
			basicGraph.createEdge(vertex2, vertex1);
			if (!successfulCreateInverse) {
				Assert.fail();
			}
		} catch (EdgeCreateException e) {
			if (successfulCreateInverse) {
				assumeNoException(e);
			}
		}
	}
}
