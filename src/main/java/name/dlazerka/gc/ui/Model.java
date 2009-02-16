package name.dlazerka.gc.ui;

import name.dlazerka.gc.model.Graph;

/**
 * @author Dzmitry Lazerka
 */
public class Model {
	private static final Graph graph = new Graph();

	public static Graph getGraph(String session) {
		return graph;
	}
}
