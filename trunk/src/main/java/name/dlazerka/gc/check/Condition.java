package name.dlazerka.gc.check;

import name.dlazerka.gc.bean.Graph;

/**
 * @author Dzmitry Lazerka
 */
public class Condition {
	public CheckResult check(Graph graph) {
		return new CheckResult(graph.getVertexSet().size() < 5);
	}
}
