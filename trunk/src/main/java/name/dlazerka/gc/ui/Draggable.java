package name.dlazerka.gc.ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * @author Dzmitry Lazerka
 */
public interface Draggable extends MouseMotionListener {
	public boolean contains(Point point);
	public void startFollowingMouse(MouseEvent e);
	public void stopFollowingMouse(MouseEvent e);
}
