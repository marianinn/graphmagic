/*
 * GraphMagic, package for scientists working in graph theory.
 * Copyright (C) 2010 Dzmitry Lazerka www.dlazerka.name
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
 * Author: Dzmitry Lazerka www.dlazerka.name
 */
package name.dlazerka.gm.shell.store;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import name.dlazerka.gm.basic.BasicGraph;
import name.dlazerka.gm.basic.BasicVertex;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphmlStorerTest {
	private static final Logger logger = LoggerFactory.getLogger(GraphmlStorerTest.class);
	private static final URL source = GraphmlStorerTest.class.getResource("graphml.xml");
	private BasicGraph graph;

	@Before
	public void setUp() {
		graph = new BasicGraph();
		BasicVertex vertex1 = graph.createVertex();
		BasicVertex vertex2 = graph.createVertex();
		BasicVertex vertex3 = graph.createVertex();
		graph.createEdge(vertex1, vertex2);
		graph.createEdge(vertex1, vertex3);
	}

	@Test
	public void testLoadFromFile() throws Exception {
		File file = new File(source.toURI());
		BasicGraph actual = GraphmlStorer.load(file);
		Assert.assertEquals(graph, actual);
	}

	@Test
	public void testSave() throws Exception {
		StringWriter writer = new StringWriter();
		GraphmlStorer.save(graph, writer);

		String string = writer.toString();

		String expected;
		InputStream in = source.openStream();
		try {
			expected = IOUtils.toString(in);
		} finally {
			IOUtils.closeQuietly(in);
		}

		Assert.assertEquals(expected, string);
	}
}
