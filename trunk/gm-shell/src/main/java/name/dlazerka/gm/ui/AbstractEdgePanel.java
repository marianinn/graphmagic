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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public abstract class AbstractEdgePanel extends JPanel implements Paintable {
	private static final Logger logger = LoggerFactory.getLogger(AbstractEdgePanel.class);

	protected static final Color EDGE_COLOR = Color.BLACK;
	protected static final Color EDGE_HOVER_COLOR = new Color(0x00, 0x00, 0x00);
	protected static final Stroke EDGE_STROKE = new BasicStroke(2f);
	protected static final Stroke EDGE_HOVER_STROKE = new BasicStroke(6f);
	protected final QuadCurve2D curve = new QuadCurve2D.Double();

	protected AbstractEdgePanel() {
		super(null);
	}
}