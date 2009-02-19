package name.dlazerka.gc.model;

import name.dlazerka.gc.bean.Graph;

/**
 * @author Dzmitry Lazerka
 */
public class Model {
	private static final Graph graph = new Graph();

	public static Graph getGraph() {
		return graph;
	}
}
