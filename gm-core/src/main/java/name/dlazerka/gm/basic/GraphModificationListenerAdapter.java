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

package name.dlazerka.gm.basic;

import name.dlazerka.gm.Edge;
import name.dlazerka.gm.Vertex;

/**
 * Empty implementation.
 *
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public abstract class GraphModificationListenerAdapter implements GraphModificationListener {
	@Override
	public void notifyAttached() {
	}

	@Override
	public void vertexAdded(Vertex vertex) {
	}

	@Override
	public void edgeAdded(Edge edge) {
	}

	@Override
	public void vertexDeleted(Vertex vertex) {
	}

	@Override
	public void edgeDeleted(Edge edge) {
	}

	@Override
	public void setDirected(boolean directed) {
	}

	@Override
	public void setMulti(boolean multi) {
	}

	@Override
	public void setPseudo(boolean pseudo) {
	}
}
