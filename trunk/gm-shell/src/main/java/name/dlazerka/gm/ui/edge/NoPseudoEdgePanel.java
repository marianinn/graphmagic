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

package name.dlazerka.gm.ui.edge;

import name.dlazerka.gm.Edge;
import name.dlazerka.gm.ui.VertexPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.QuadCurve2D;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class NoPseudoEdgePanel extends EdgePanel {
	private static final Logger logger = LoggerFactory.getLogger(NoPseudoEdgePanel.class);
	private QuadCurve2D curve;
	private Point ctrlPoint;
	private boolean curved = false;

	public NoPseudoEdgePanel(Edge edge, VertexPanel tail, VertexPanel head) {
		super(edge, tail, head);
		logger.debug("{}", ctrlPoint);
	}

	@Override
	protected void initShape() {
		super.initShape();
		curve = new QuadCurve2D.Float();
		ctrlPoint = new Point();
		oddPoint.x = (getFromPoint().x + getToPoint().x) / 2;
		oddPoint.y = (getFromPoint().y + getToPoint().y) / 2;
	}

	@Override
	protected Shape getShape() {
		return curve;
	}

	public void setCurved(boolean curved) {
		this.curved = curved;
	}

	@Override
	protected void pullTo(int x, int y) {
		super.pullTo(x, y);

		setCurved(
				!tail.contains(ctrlPoint)
						&& !head.contains(ctrlPoint)
						&& !this.contains(ctrlPoint)
		);
	}

	/**
	 * Sets the {@link #ctrlPoint} so, that {@link #oddPoint} will lie on the peak of curve.
	 * Places {@link #ctrlPoint} on double distance from center of line drawn between end points to the
	 * {@link #oddPoint}.
	 * It equals to fact that {@link #oddPoint} equals to bezier point position at t=0.5
	 */
	protected void updateGeometry() {
		int centerX = (getFromPoint().x + getToPoint().x) / 2;
		int centerY = (getFromPoint().y + getToPoint().y) / 2;

		ctrlPoint.move(
				oddPoint.x * 2 - centerX,
				oddPoint.y * 2 - centerY
		);

		curve.setCurve(
				getFromPoint(),
				curved ? ctrlPoint : getFromPoint(),
				getToPoint()
		);
		hoverShape = EDGE_STROKE_HOVERED.createStrokedShape(curve);
	}
}
