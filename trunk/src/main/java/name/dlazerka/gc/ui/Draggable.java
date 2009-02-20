package name.dlazerka.gc.ui;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.*;

/**
 * @author Dzmitry Lazerka
 */
public interface Draggable extends MouseListener, MouseMotionListener {
	public boolean contains(Point point);
}
