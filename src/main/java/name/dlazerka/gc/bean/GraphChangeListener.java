package name.dlazerka.gc.bean;

import java.util.EventListener;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public interface GraphChangeListener extends EventListener {
	void notifyAttached();

	void vertexAdded(Vertex vertex);

	void edgeAdded(Edge edge);

	void vertexDeleted(Vertex vertex);

	void edgeDeleted(Edge edge);
}
