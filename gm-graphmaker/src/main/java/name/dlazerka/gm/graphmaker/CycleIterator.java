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

import java.awt.geom.Point2D;
import java.util.Iterator;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class CycleIterator implements Iterator<Point2D> {
	private final int n;
	private int i;
	private final double angleStep;
	private final double radius;

	public CycleIterator(int n) {
		this.n = n;
		angleStep = 2 * Math.PI / n;
		radius = .75;

		this.i = 0;
	}

	@Override
	public boolean hasNext() {
		return i < n;
	}

	@Override
	public Point2D next() {
		double angle = i++ * angleStep + Math.PI / 2;

		double x = -Math.cos(angle) * radius;
		double y = -Math.sin(angle) * radius;

		Point2D point2D = new Point2D.Double(x, y);
		return point2D;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
