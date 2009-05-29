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

package name.dlazerka.gm.dijkstra;

import name.dlazerka.gm.Edge;
import name.dlazerka.gm.PluginException;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class NoWeightDefinedException extends PluginException {
	private final Edge edge;
	private Object[] keysUsed;

	public NoWeightDefinedException(Edge edge, Object ... keysUsed) {
		this.edge = edge;
		this.keysUsed = keysUsed;
	}

	@Override
	public String getLocalizedMessage() {
		StringBuilder sb = new StringBuilder("[");
		for (Object o : keysUsed) {
			if (o instanceof String) {
				sb.append("\"");
			}
			sb.append(o.toString());
			if (o instanceof String) {
				sb.append("\"");
			}
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append("]");

		return "Edge " + edge + " has no weight defined (keys used: " + sb.toString() + ")";
	}
}
