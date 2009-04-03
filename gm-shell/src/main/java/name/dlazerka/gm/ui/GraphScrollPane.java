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

package name.dlazerka.gm.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class GraphScrollPane extends JScrollPane {
	private final static Dimension DEFAULT_DIMENSION = new Dimension(600, 400);
	private final static Dimension MINIMUM_DIMENSION = new Dimension(300, 200);

	public GraphScrollPane(GraphPanel graphPanel) {
		super(graphPanel);

		setPreferredSize(DEFAULT_DIMENSION);// for MainFrame
		setSize(DEFAULT_DIMENSION);// for GraphLayoutManager@58
		setMinimumSize(MINIMUM_DIMENSION);// to not to reduce to zero while "Restoring Down"
	}

}
