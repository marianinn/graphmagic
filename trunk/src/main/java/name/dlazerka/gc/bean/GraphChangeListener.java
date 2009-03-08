package name.dlazerka.gc.bean;

import java.util.EventListener;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public interface GraphChangeListener extends EventListener {
	void vertexAdded(Vertex vertex); 
}
