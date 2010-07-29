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

import name.dlazerka.gm.Graph;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class BasicEdgeTest {
	@Test
	public final void testEquals() {
		Graph graph = new BasicGraph();
		BasicVertex source = new BasicVertex(graph, "1");
		BasicVertex target = new BasicVertex(graph, "2");

		BasicEdge edge1 = new BasicEdge(graph, source, target);
		BasicEdge edge2 = new BasicEdge(graph, source, target);
		BasicEdge edge3 = new BasicEdge(graph, target, source );

		graph.setDirected(true);
		graph.setMulti(true);
		assertFalse(edge1.equals(edge2));
		assertFalse(edge1.equals(edge3));
		assertFalse(edge2.equals(edge3));

		graph.setDirected(true);
		graph.setMulti(false);
		assertTrue(edge1.equals(edge2));
		assertFalse(edge1.equals(edge3));
		assertFalse(edge2.equals(edge3));

		graph.setDirected(false);
		graph.setMulti(true);
		assertFalse(edge1.equals(edge2));
		assertFalse(edge1.equals(edge3));
		assertFalse(edge2.equals(edge3));

		graph.setDirected(false);
		graph.setMulti(false);
		assertTrue(edge1.equals(edge2));
		assertTrue(edge1.equals(edge3));
		assertTrue(edge2.equals(edge3));
	}
}
