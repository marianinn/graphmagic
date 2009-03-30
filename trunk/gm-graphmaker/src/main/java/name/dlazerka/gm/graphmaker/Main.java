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

import name.dlazerka.gm.AbstractPlugin;
import name.dlazerka.gm.GraphMagicPlugin;
import name.dlazerka.gm.GraphWithUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Main extends AbstractPlugin implements GraphMagicPlugin {
	private static final Logger logger = LoggerFactory.getLogger(AbstractPlugin.class);
	private MakeGraphFrame makeGraphFrame;

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
				GraphWithUI ui = getGraphMagicAPI().getFocusedGraph();
				makeGraphFrame = new MakeGraphFrame(ui.getFrame(), "Make Graph");
			}

			makeGraphFrame.setVisible(true);
		}
	}
}
