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

import name.dlazerka.gm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
class ShortestPath extends AbstractAction {
    private static final Logger logger = LoggerFactory.getLogger(ShortestPath.class);

    private final GraphMagicAPI api;

    ShortestPath(GraphMagicAPI api) {
        super("Shortest path for single source");
        this.api = api;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Graph graph = api.getFocusedGraph();
        Set<Vertex> vertexSet = graph.getVertexSet();
        Vertex startVertex = null;
        for (Vertex vertex : vertexSet) {
            Visual visual = vertex.getVisual();
            boolean selected = visual.isSelected();
            if (selected) {
                startVertex = vertex;
                break;
            }
        }

        if (startVertex == null) {
            try {
                throw new NoStartVertexException();
            }
            catch (NoStartVertexException e1) {
                api.showMessage(e1, MessageLevel.ERROR);
            }
        }
    }
}
