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

import name.dlazerka.gm.GraphMagicAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class EmptyGraphMakerItem extends GraphMakerItem {
	private static final Logger logger = LoggerFactory.getLogger(EmptyGraphMakerItem.class);

	public EmptyGraphMakerItem(GraphMagicAPI graphMagicAPI) {
		super();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		logger.debug("");
	}

	@Override
	public String getLabel() {
		return "Empty Graph";
	}
}
