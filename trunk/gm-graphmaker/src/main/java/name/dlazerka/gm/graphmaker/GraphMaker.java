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

import name.dlazerka.gm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class GraphMaker extends AbstractPlugin implements GraphMagicPlugin {
	private static final Logger logger = LoggerFactory.getLogger(AbstractPlugin.class);
	private MakeGraphFrame makeGraphFrame;
	private List<GraphMakerItem> items = new LinkedList<GraphMakerItem>();

	@Override
	public void init(GraphMagicAPI graphMagicAPI, Locale locale) {
		super.init(graphMagicAPI, locale);
		
		items.add(new EmptyGraphMakerItem(getGraphMagicAPI()));
		items.add(new CycleGraphMakerItem(getGraphMagicAPI()));
		items.add(new WheelGraphMakerItem(getGraphMagicAPI()));
		items.add(new BipartiteGraphMakerItem(getGraphMagicAPI()));
		CompleteGraphMakerItem completeGraphMakerItem = new CompleteGraphMakerItem(getGraphMagicAPI());
		items.add(completeGraphMakerItem);

		completeGraphMakerItem.createAndConnect(5);
	}

	@Override
	public List<Action> getActions() {
		LinkedList<Action> actionList = new LinkedList<Action>();
		AbstractAction action = new MakeGraphAction();
		actionList.add(action);
		return actionList;
	}

	@Override
	public String getName() {
		return "GraphMaker";
	}

	private class MakeGraphAction extends AbstractAction {

		private MakeGraphAction() {
			super("Make Graph");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (makeGraphFrame == null) {
				Graph graph = getGraphMagicAPI().getFocusedGraph();
				GraphUI ui = graph.getUI();
				makeGraphFrame = new MakeGraphFrame(ui.getOwnerFrame(), "Make Graph", items);
			}

			makeGraphFrame.setVisible(true);
		}
	}
}
