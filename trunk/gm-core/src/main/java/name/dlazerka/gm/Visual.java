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

import java.awt.*;
import java.util.Observable;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Visual extends Observable {
	private double centerX = 0;
	private double centerY = 0;
    private Color color;

	public double getCenterX() {
		return centerX;
	}

	public void setCenterX(double centerX) {
		setCenter(centerX, centerY);
	}

	public double getCenterY() {
		return centerY;
	}

	public void setCenterY(double centerY) {
		setCenter(centerX, centerY);
	}

	public void setCenter(double centerX, double centerY) {
		setCenter(centerX, centerY, true);
	}

	public void setCenter(Point center) {
		setCenter(center.x, center.y);
	}

	public void setCenter(double centerX, double centerY, boolean notifyObservers) {
		this.centerX = centerX;
		this.centerY = centerY;

		this.setChanged();

		if (notifyObservers) {
			this.notifyObservers();
		}
	}

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        setColor(color, true);
    }

    public void setColor(Color color, boolean notifyObservers) {
        this.color = color;

        if (notifyObservers) {
            this.notifyObservers();
        }
    }
}
