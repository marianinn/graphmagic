/*
 * GraphMagic, package for scientists working in graph theory.
 * Copyright (C) 2009 Dzmitry Lazerka www.dlazerka.name
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
 * Author: Dzmitry Lazerka www.dlazerka.name
 */

package name.dlazerka.gm;

/**
 * Denotes either vertices or edges.
 * 
 * @author Dzmitry Lazerka www.dlazerka.name
 */

public interface GraphElement {
	/**
	 * Graph elements must belong to a graph, they cannot exist without any graph.
	 * @return owner graph
	 */
	Graph getGraph();

	/**
	 * @return properties that usually affect only visual representation of graph.
	 */
	Visual getVisual();

	/**
	 * @return element properties like weight, flow etc.
	 */
	Mark getMark();
}
