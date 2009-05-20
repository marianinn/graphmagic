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


/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public interface GraphMagicAPI {
	/**
	 * Returns on of the graphs that is of most interest, usually last used.
	 *
	 * @return one of the graphs that is of most interest, usually last used.
	 */
	Graph getFocusedGraph();

	/**
	 * Returns all of the graphs in the scope of this API.
	 *
	 * @return all of the graphs in the scope of this API.
	 */
	Collection<Graph> getGraphs();

	void attachListener(GraphsListener listener);

	void detachListener(GraphsListener listener);
}
