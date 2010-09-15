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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import name.dlazerka.gm.Edge;
import name.dlazerka.gm.Graph;
import name.dlazerka.gm.GraphElement;
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
	private static final String DEFAULT_VERTEX_COLOR = "#ffffffff";
	private static final String DEFAULT_EDGE_COLOR = "#ff000000";
	private static final Pattern COLOR_PATTERN = Pattern.compile("^#[0-9a-fA-F]{6,8}$");

	public static void save(Graph graph, File file)
			throws IOException {
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

	public static void save(Graph graph, Writer where)
			throws IOException {
		writeHeader(graph, where);
		writeNodes(graph, where);
		writeEdges(graph, where);
		String footer = ResourceBundle.getString("graphml.footer");
		where.write(footer);
	}

	private static void writeHeader(Graph graph, Writer where)
			throws IOException {
		String headerFormat = ResourceBundle.getString("graphml.header");
		String directed = graph.isDirected() ? "directed" : "undirected";
		int nodesCount = graph.getVertexSet().size();
		int edgesCount = graph.getEdgeSet().size();
		String header = MessageFormat.format(
				headerFormat,
				DEFAULT_VERTEX_COLOR,
				DEFAULT_EDGE_COLOR,
				directed,
				nodesCount,
				edgesCount);
		where.write(header);
		logger.debug("Wrote header");
	}

	private static void writeNodes(Graph graph, Writer where)
			throws IOException {
		String nodeFormat = ResourceBundle.getString("graphml.node");
		for (Vertex vertex : graph.getVertexSet()) {
			String colorHex = extractHexColor(vertex.getVisual());
			colorHex = colorHex == null ? DEFAULT_VERTEX_COLOR : colorHex;
			where.write(MessageFormat.format(nodeFormat, vertex.getId(), colorHex));
			logger.debug("Wrote {}", vertex);
		}
	}

	private static void writeEdges(Graph graph, Writer where)
			throws IOException {
		String edgeFormat = ResourceBundle.getString("graphml.edge");
		String edgeDirectedFormat = ResourceBundle.getString("graphml.edge.directed");
		for (Edge edge : graph.getEdgeSet()) {
			String sourceId = edge.getSource().getId();
			String targetId = edge.getTarget().getId();
			String colorHex = extractHexColor(edge.getVisual());
			colorHex = colorHex == null ? DEFAULT_EDGE_COLOR : colorHex;
			String directed = "";
			if (edge.isDirected() != graph.isDirected()) {
				directed = MessageFormat.format(edgeDirectedFormat, edge.isDirected() + "");
			}
			where.write(MessageFormat.format(edgeFormat, sourceId, targetId, directed, colorHex));
			logger.debug("Wrote {}", edge);
		}
	}

	/**
	 * @param visual not null
	 * @return visual's color as a hex string, prepended by #.
	 * @see #parseColor(String)
	 */
	protected static String extractHexColor(Visual visual) {
		String colorStr = null;
		Color color = visual.getColor();
		if (color != null) {
			colorStr = '#' + Integer.toHexString(color.getRGB());
		}
		return colorStr;
	}

	public static BasicGraph load(File source)
			throws GraphLoadingException {
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

	public static BasicGraph load(Reader source)
			throws GraphLoadingException {
		BasicGraph graph;

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		try {
			XMLStreamReader reader = inputFactory.createXMLStreamReader(source);

			Set<GraphmlKey> keys = readKeys(reader);

			// reader now on <graph>
			String edgedefault = reader.getAttributeValue(null, "edgedefault");
			graph = new BasicGraph(edgedefault.equals("directed"));

			readGraphBody(graph, reader, keys);
		}
		catch (XMLStreamException e) {
			throw new GraphLoadingException(e);
		}

		return graph;
	}

	private static Set<GraphmlKey> readKeys(XMLStreamReader reader)
			throws XMLStreamException, GraphLoadingException {
		String node = proceedToNodes(reader, "key", "graph");
		Set<GraphmlKey> keys = new HashSet<GraphmlKey>();
		while (node.equals("key")) {
			String id = reader.getAttributeValue(null, "id");
			String for_ = reader.getAttributeValue(null, "for");
			String name = reader.getAttributeValue(null, "attr.name");
			String type = reader.getAttributeValue(null, "attr.type");
			String default_ = null;

			node = proceedToNodes(reader, "key", "default", "graph");

			if (reader.getLocalName().equals("default")) {
				default_ = reader.getElementText();
				node = proceedToNodes(reader, "key", "graph");
			}

			GraphmlKeyFor forEnum = GraphmlKeyFor.valueOf(for_.toUpperCase());

			GraphmlKey key = new GraphmlKey(id, default_, forEnum, name, type);
			keys.add(key);
			logger.debug("Loaded {}", key);
		}
		return keys;
	}

	/**
	 * Reads nodes and edges with their data to given graph.
	 *
	 * @param graph not null
	 * @param reader not null
	 * @param keys not null
	 * @throws XMLStreamException
	 * @throws GraphLoadingException
	 */
	private static void readGraphBody(BasicGraph graph, XMLStreamReader reader, Set<GraphmlKey> keys)
			throws XMLStreamException, GraphLoadingException {
		GraphElement lastReadElement = null;
		String name = proceedToNodesOrEnd(reader, "node", "edge", "data");
		while (name != null) {
			if (name.equals("node")) {
				String id = reader.getAttributeValue(null, "id");
				id = maybeCutId(id);
				lastReadElement = graph.createVertex(id);
				logger.debug("Read {}", lastReadElement);
			}
			else if (name.equals("edge")) {
				String sourceId = reader.getAttributeValue(null, "source");
				String targetId = reader.getAttributeValue(null, "target");
				String directedStr = reader.getAttributeValue(null, "directed");

				sourceId = maybeCutId(sourceId);
				targetId = maybeCutId(targetId);
				Boolean directed = directedStr != null ? Boolean.valueOf(directedStr) : null;

				try {
					if (directed == null) {
						lastReadElement = graph.createEdge(sourceId, targetId);
					}
					else {
						lastReadElement = graph.createEdge(sourceId, targetId, directed);
					}
				}
				catch (EdgeCreateException e) {
					throw new GraphLoadingException(e);
				}
				logger.debug("Read {}", lastReadElement);
			}
			else if (name.equals("data")) {
				if (lastReadElement == null) continue;
				String keyName = reader.getAttributeValue(null, "key");
				String keyValue = reader.getElementText();
				GraphmlKey key = lookupKey(keyName, lastReadElement, keys);
				if (key.getName().equalsIgnoreCase("color")) {
					Color color = parseColor(keyValue);
					lastReadElement.getVisual().setColor(color);
				}
				logger.debug("Read {}", key);
			}
			name = proceedToNodesOrEnd(reader, "node", "edge", "data");
		}
	}

	/**
	 * If color matches pattern, then read its rgba, else lets {@link Color} find it in sys props.
	 *
	 * @param colorStr like '#ff000000', or '#000000' or 'black'
	 * @return color instance
	 * @see #extractHexColor(Visual)
	 */
	private static Color parseColor(String colorStr) {
		logger.debug("Parsing color from string '{}'", colorStr);
		Color color;
		Matcher matcher = COLOR_PATTERN.matcher(colorStr);
		if (matcher.matches()) {
			int rgba = (int) (long) Long.decode(colorStr);
			color = new Color(rgba, true);
		}
		else {
			color = Color.getColor(colorStr);
		}
		return color;
	}

	private static GraphmlKey lookupKey(String id, GraphElement el, Set<GraphmlKey> keys)
			throws GraphLoadingException {
		String for_ = el instanceof Vertex ? "node" : "edge";
		for (GraphmlKey key : keys) {
			if (id.equals(key.getId()) && key.getFor().fits(for_)) {
				return key;
			}
		}
		throw new GraphLoadingException("Unable to found key '" + id + "' for " + for_ + " in declared keys " + keys);
	}

	/**
	 * Skips all XML elements in reader until any of given nodeNames are found or end is hit.
	 *
	 * @param reader not null
	 * @param nodeNames not null
	 */
	private static String proceedToNodesOrEnd(XMLStreamReader reader, String... nodeNames)
			throws XMLStreamException, GraphLoadingException {
		try {
			return proceedToNodes(reader, nodeNames);
		}
		catch (EndHitException e) {
			logger.debug("End hit, returning null");
			return null;
		}
	}

	/**
	 * Skips all XML elements in reader until any of given nodeNames are found.
	 *
	 * @param reader not null
	 * @param nodeNames not null
	 */
	private static String proceedToNodes(XMLStreamReader reader, String... nodeNames)
			throws XMLStreamException, GraphLoadingException {
		logger.debug("Proceeding to {}", new Object[] { nodeNames });
		while (reader.hasNext()) {
			reader.next();
			if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
				String name = reader.getLocalName();
				for (String nodeName : nodeNames) {
					if (nodeName.equals(name))
						return name;
				}
			}
		}

		throw new EndHitException(nodeNames);
	}

	/**
	 * If given id matches canonical ID pattern, then returns only numeric part, else returns as is.
	 *
	 * @param id
	 * @return numeric part of given id or id as is.
	 */
	private static String maybeCutId(String id) {
		Matcher m = ID_PATTERN.matcher(id);
		if (m.matches()) {
			id = m.group(1);
		}
		return id;
	}

	private static class EndHitException extends GraphLoadingException {
		protected EndHitException(String... nodeNames) {
			super("Hit end without hitting any of '" + Arrays.toString(nodeNames) + "'.");
		}
	}
}
