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

import name.dlazerka.gm.Graph;
import name.dlazerka.gm.basic.BasicGraph;
import name.dlazerka.gm.basic.MergeException;
import name.dlazerka.gm.exception.EdgeCreateException;
import name.dlazerka.gm.shell.ResourceBundle;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class LoadGraphActionListener extends JFileChooser implements ActionListener {
	private static final Logger logger = LoggerFactory.getLogger(LoadGraphActionListener.class);
	private final Component parent;
	private final Graph graph;
	private static final Pattern ID_PATTERN = Pattern.compile("^[nN]([0-9]{1,3})$");

	public LoadGraphActionListener(Component parent, Graph graph) {
		this.parent = parent;
		this.graph = graph;
		setDialogTitle(ResourceBundle.getString("load.graph"));
		setFileSelectionMode(JFileChooser.FILES_ONLY);
		setFileFilter(
				new FileNameExtensionFilter(
						ResourceBundle.getString("graphml.files"),
						ResourceBundle.getString("graphml.ext")
				)
		);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int ret = showOpenDialog(parent);

		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = getSelectedFile();
			try {
				Graph graph = loadGraphML(file);
				try {
					this.graph.mergeFrom(graph);
				}
				catch (MergeException e1) {
					throw new GraphLoadingException(e1);
				}
			}
			catch (GraphLoadingException e1) {
				ErrorDialog.showError(e1, parent);
			}
		}
	}

	protected static Graph loadGraphML(File file) throws GraphLoadingException {
		try {
			logger.info("Loading graph from {}", file.getCanonicalPath());
			FileInputStream source = new FileInputStream(file);
			return loadGraphML(source);
		}
		catch (IOException e) {
			throw new GraphLoadingException(e);
		}
	}

	protected static BasicGraph loadGraphML(InputStream source) throws GraphLoadingException {
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

	protected static String maybeCutId(String id) {
		Matcher m = ID_PATTERN.matcher(id);
		if (m.matches()) {
			id = m.group(1);
		}
		return id;
	}
}
