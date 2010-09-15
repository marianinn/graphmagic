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

import java.awt.Color;
import java.awt.Point;
import java.util.Observable;

/**
 * View-independent visual properties of an object.
 *
 * Center of the screen represented by (0, 0).
 * Left (eastern) border of the screen is represented by (-1, *).
 * Right (western) border of the screen is represented by (1, *).
 * Top (north) border of the screen is represented by (*, -1).
 * Bottom (south) border of the screen is represented by (*, 1).
 *
 * Note that you may specify out-of-border values.
 *
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Visual extends Observable {
	private double centerX = 0;
	private double centerY = 0;
    private Color color;
    private boolean selected;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Visual other = (Visual) obj;
		if (Double.doubleToLongBits(this.centerX) != Double.doubleToLongBits(other.centerX)) return false;
		if (Double.doubleToLongBits(this.centerY) != Double.doubleToLongBits(other.centerY)) return false;
		if (this.color == null) {
			if (other.color != null) return false;
		}
		else if (!this.color.equals(other.color)) return false;
		if (this.selected != other.selected) return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.centerX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.centerY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((this.color == null) ? 0 : this.color.hashCode());
		result = prime * result + (this.selected ? 1231 : 1237);
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Visual [centerX=").append(this.centerX).append(", centerY=").append(this.centerY).append(
				", color=").append(this.color).append(", selected=").append(this.selected).append("]");
		return builder.toString();
	}

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

		setChanged();
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

	    setChanged();
        if (notifyObservers) {
            this.notifyObservers();
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
	    setChanged();
	    notifyObservers();
    }

	public void mergeFrom(Visual visual) {
		setCenter(visual.getCenterX(), visual.getCenterY());
	}
}
