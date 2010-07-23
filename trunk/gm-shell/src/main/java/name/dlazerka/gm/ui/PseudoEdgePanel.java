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

import name.dlazerka.gm.Edge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.CubicCurve2D;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class PseudoEdgePanel extends EdgePanel {
	private final static Logger logger = LoggerFactory.getLogger(PseudoEdgePanel.class);

	protected CubicCurve2D curve;

	public PseudoEdgePanel(Edge edge, VertexPanel vertexPanel) {
		super(edge, vertexPanel, vertexPanel);
	}

	@Override
	protected void initOddPoint() {
		oddPoint.x = getFromPoint().x + 50;
		oddPoint.y = getFromPoint().y - 50;
	}

	@Override
	protected void updateGeometry() {
		throw new UnsupportedOperationException("Not implemented");
	}


	protected void initShape() {
		curve = new CubicCurve2D.Float();
	}

	@Override
	protected CubicCurve2D getShape() {
		return curve;
	}


}
