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

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import name.dlazerka.gm.Edge;
import name.dlazerka.gm.Graph;
import name.dlazerka.gm.Vertex;
import name.dlazerka.gm.Visual;
import name.dlazerka.gm.basic.BasicGraph;
import name.dlazerka.gm.exception.EdgeCreateException;
import name.dlazerka.gm.shell.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GraphML storage facilities.
 */
public class GraphmlStorer {
	private static final Logger logger = LoggerFactory.getLogger(GraphmlStorer.class);
	private static final Pattern ID_PATTERN = Pattern.compile("^[nN]([0-9]{1,3})$");
	private static final String DEFAULT_COLOR = "#ffffffff";

	public static void save(Graph graph, File file) throws IOException {
		logger.info("Saving graph to {}", file.getCanonicalPath());
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(fileWriter);
		try {
			save(graph, writer);
		}
		finally {
			writer.close();
		}
		logger.info("Saved graph to {}", file.getCanonicalPath());
	}

	public static void save(Graph graph, Writer where) throws IOException {
		String headerFormat = ResourceBundle.getString("graphml.header");
		String nodeFormat = ResourceBundle.getString("graphml.node");
		String edgeFormat = ResourceBundle.getString("graphml.edge");
		String footer = ResourceBundle.getString("graphml.footer");

		String directed = graph.isDirected() ? "directed" : "undirected";
		int nodesCount = graph.getVertexSet().size();
		int edgesCount = graph.getEdgeSet().size();
		String header = MessageFormat.format(headerFormat, DEFAULT_COLOR, directed, nodesCount, edgesCount);

		where.write(header);
		for (Vertex vertex : graph.getVertexSet()) {
			String colorHex = extractHexColor(vertex.getVisual());
			where.write(MessageFormat.format(nodeFormat, vertex.getId(), colorHex));
		}
		logger.debug("Nodes written. Writing edges...");
		for (Edge edge : graph.getEdgeSet()) {
			String colorHex = extractHexColor(edge.getVisual());
			String sourceId = edge.getSource().getId();
			String targetId = edge.getTarget().getId();
			where.write(MessageFormat.format(edgeFormat, sourceId, targetId, colorHex));
		}
		where.write(footer);
	}

	/**
	 * @param visual not null
	 * @return visual's color as a hex string, prepended by #.
	 */
	protected static String extractHexColor(Visual visual) {
		String colorStr = DEFAULT_COLOR;
		Color color = visual.getColor();
		if (color != null) {
			colorStr = '#' + Integer.toHexString(color.getRGB());
		}
		return colorStr;
	}

	public static BasicGraph load(File source) throws GraphLoadingException {
		try {
			logger.info("Loading graph from {}", source.getCanonicalPath());
			FileReader fileReader = new FileReader(source);
			BufferedReader reader = new BufferedReader(fileReader);
			BasicGraph graph = load(reader);
			logger.info("Graph loaded");
			return graph;
		}
		catch (IOException e) {
			throw new GraphLoadingException(e);
		}
	}

	public static BasicGraph load(Reader source) throws GraphLoadingException {
		BasicGraph graph;

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		try {
			XMLStreamReader reader = inputFactory.createXMLStreamReader(source);

			proceedTo("graph", reader);

			String edgedefault = reader.getAttributeValue(null, "edgedefault");
			graph = new BasicGraph(edgedefault.equals("directed"));

			while (reader.hasNext()) {
				reader.next();
				switch (reader.getEventType()) {
				case XMLStreamReader.START_ELEMENT:
					String name = reader.getLocalName();
					if (name.equals("edge")) {
						String sourceId = reader.getAttributeValue(null, "source");
						String targetId = reader.getAttributeValue(null, "target");

						sourceId = maybeCutId(sourceId);
						targetId = maybeCutId(targetId);

						try {
							graph.createEdge(sourceId, targetId);
						}
						catch (EdgeCreateException e) {
							throw new GraphLoadingException(e);
						}
					}
					else if (name.equals("node")) {
						String id = reader.getAttributeValue(null, "id");
						id = maybeCutId(id);
						graph.createVertex(id);
					}
				}
			}
		}
		catch (XMLStreamException e) {
			throw new GraphLoadingException(e);
		}

		return graph;
	}


	private static void proceedTo(String nodeName, XMLStreamReader reader) throws XMLStreamException, GraphLoadingException {
		while (reader.hasNext()) {
			reader.next();
			switch (reader.getEventType()) {
			case XMLStreamReader.START_ELEMENT:
				String name = reader.getLocalName();
				if (name.equals(nodeName)) {
					return;
				}
			}
		}
		throw new GraphLoadingException("Hit end without hitting '" + nodeName + "'");
	}

	private static String maybeCutId(String id) {
		Matcher m = ID_PATTERN.matcher(id);
		if (m.matches()) {
			id = m.group(1);
		}
		return id;
	}
}
