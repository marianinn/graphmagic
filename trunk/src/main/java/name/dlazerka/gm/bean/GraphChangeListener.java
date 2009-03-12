package name.dlazerka.gc.bean;

import java.util.EventListener;

/**
 * @author Dzmitry Lazerka
 */
public interface GraphChangeListener extends EventListener {
	void vertexAdded(Vertex vertex); 
}
