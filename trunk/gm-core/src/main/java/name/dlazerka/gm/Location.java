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

package name.dlazerka.gm;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Location {
	private double x;
	private double y;

	public Location(double x, double y) {
		setX(x);
		setY(y);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		if (x < -1.0 || x > 1.0) {
			throw new IllegalArgumentException("The X coordinate must belong to the [-1, 1] interval");
		}
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		if (y < -1.0 || y > 1.0) {
			throw new IllegalArgumentException("The Y coordinate must belong to the [-1, 1] interval");
		}
		this.y = y;
	}
}
