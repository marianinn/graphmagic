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

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class GraphsContainer extends LinkedList<GraphWithUI> implements GraphMagicAPI {
	private final LinkedList<GraphsListener> listenerList = new LinkedList<GraphsListener>();
	private GraphWithUI focused;

	@Override
	public GraphWithUI getFocusedGraph() {
		return focused;
	}

	public void setFocused(GraphWithUI graph) {
		if (!contains(graph)) {
			throw new IllegalArgumentException("Does not contain the graph " + graph);
		}
		
		this.focused = graph;
	}

	@Override
	public Collection<GraphWithUI> getGraphs() {
		return this;
	}

	@Override
	public void attachListener(GraphsListener listener) {
		listenerList.add(listener);
		listener.attached();
	}

	@Override
	public void detachListener(GraphsListener listener) {
		if (listenerList.remove(listener)) {
			listener.detached();
		}
	}


}
