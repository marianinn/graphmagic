/*
 * GraphMagic, package for scientists working in graph theory.
 * Copyright (C) 2010 Dzmitry Lazerka dlazerka@dlazerka.name
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

package name.dlazerka.gm.ui;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import name.dlazerka.gm.basic.BasicGraph;
import name.dlazerka.gm.basic.BasicVertex;
import name.dlazerka.gm.exception.EdgeCreateException;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class LoadGraphActionListenerTest {
	private static URL source;

	@BeforeClass
	public static void setUpSources() throws IOException {
		String fileName = "LoadGraphActionListenerTest.1.xml";
		source = LoadGraphActionListenerTest.class.getResource(fileName);
	}

	@Test
	public void testLoadFromFile() throws EdgeCreateException, GraphLoadingException, IOException {
		BasicGraph actual = LoadGraphActionListener.loadGraphML(source.openStream());

		BasicGraph expected = new BasicGraph();
		BasicVertex vertex1 = expected.createVertex();
		BasicVertex vertex2 = expected.createVertex();
		BasicVertex vertex3 = expected.createVertex();
		expected.createEdge(vertex1, vertex2);
		expected.createEdge(vertex1, vertex3);

		Assert.assertEquals(expected, actual);
	}
}
