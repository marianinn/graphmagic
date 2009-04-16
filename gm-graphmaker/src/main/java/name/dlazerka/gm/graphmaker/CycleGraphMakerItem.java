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

package name.dlazerka.gm.graphmaker;

import name.dlazerka.gm.Graph;
import name.dlazerka.gm.GraphMagicAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class CycleGraphMakerItem extends GraphMakerItem {
	private static final Logger logger = LoggerFactory.getLogger(EmptyGraphMakerItem.class);
	private JTextField nField;

	public CycleGraphMakerItem(GraphMagicAPI graphMagicAPI) {
		super(graphMagicAPI);
	}

	@Override
	public String getLabel() {
		return "Cycle(n)";
	}

	@Override
	public void perform() {
		Graph graph = getGraphMagicAPI().getFocusedGraph();
		graph.clear();

		String nText = nField.getText();
		Integer n = Integer.valueOf(nText);


		for (int i = 0; i < n; i++) {
			graph.createVertex();
		}

		for (int i = 1; i < n; i++) {
			graph.createEdge(i, i + 1);
		}

		if (n > 0) {
			graph.createEdge(n, 1);
		}
	}

	@Override
	public void fillParamsPanel(JPanel panel) {
		logger.debug("");
		nField = new JTextField(20);
		panel.add(nField);
	}
}
