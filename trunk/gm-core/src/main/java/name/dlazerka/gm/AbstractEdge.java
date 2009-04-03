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
public abstract class AbstractEdge implements Edge {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AbstractEdge)) return false;

		AbstractEdge that = (AbstractEdge) o;

		if (getGraph() != null ? !getGraph().equals(that.getGraph()) : that.getGraph() != null) return false;
		if (getHead() != null ? !getHead().equals(that.getHead()) : that.getHead() != null) return false;
		if (getTail() != null ? !getTail().equals(that.getTail()) : that.getTail() != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = getGraph() != null ? getGraph().hashCode() : 0;
		result = 31 * result + (getHead() != null ? getHead().hashCode() : 0);
		result = 31 * result + (getTail() != null ? getTail().hashCode() : 0);
		return result;
	}
}
