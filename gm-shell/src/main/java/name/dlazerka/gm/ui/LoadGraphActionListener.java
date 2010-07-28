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
import name.dlazerka.gm.shell.ResourceBundle;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class LoadGraphActionListener extends JFileChooser implements ActionListener {
	private static final Logger logger = LoggerFactory.getLogger(LoadGraphActionListener.class);
	private final Component parent;
	private final Graph graph;

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
				graph.clear();
				loadGraphML(file);
			}
			catch (IOException e1) {
				ErrorDialog.showError(e1, parent);
			}
		}
	}

	private void loadGraphML(File file) throws IOException {
		logger.info("Loading graph from {}", file.getCanonicalPath());
		throw new UnsupportedOperationException("Not implemented");
/*
		FileReader fr = new FileReader(file);
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		try {
			XMLStreamReader reader = inputFactory.createXMLStreamReader(fr);
			while (reader.hasNext()) {
				reader.next();
				switch (reader.getEventType()) {
					case XMLStreamReader.START_ELEMENT:
						String name = reader.getLocalName();
						if (name.equals("node")) {
							String id = reader.getAttributeValue("", "id");
							graph.createVertex(id);
						}
						else if (name.equals("graph")) {
							String edgedefault = reader.getAttributeValue("", "edgedefault");
							graph.setDirected(edgedefault.equals("directed"));
						}
				}
			}
		}
		catch (XMLStreamException e) {
			throw new IOException(e);
		}
		logger.info("Loading graph from {}", file.getCanonicalPath());
		*/
	}
}
