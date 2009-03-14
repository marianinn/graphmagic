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

import name.dlazerka.gm.ui.Paintable;
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

	private static final Color EDGE_COLOR = Color.BLACK;
	private static final Stroke EDGE_STROKE = new BasicStroke(2f);
	private final QuadCurve2D curve = new QuadCurve2D.Double();
	private Shape lastShownShape = curve;

	protected AbstractEdgePanel() {
		super(null);
	}

	protected void drawEdge(Graphics2D g, Point from, Point ctrl, Point to) {
		g.setColor(EDGE_COLOR);
		g.setStroke(EDGE_STROKE);

//		Point point = new Point((to.x + from.x) / 2, (to.y + from.y) / 2);
		curve.setCurve(from, ctrl, to);

		lastShownShape = curve;
		lastShownShape = EDGE_STROKE.createStrokedShape(lastShownShape);

		g.draw(lastShownShape);
	}

	@Override
	public boolean contains(int x, int y) {
		boolean b = lastShownShape.contains(x, y);
//		if (b) logger.debug("{}", b);
		return b;
	}
}