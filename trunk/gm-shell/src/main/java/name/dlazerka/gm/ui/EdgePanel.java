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

package name.dlazerka.gm.ui;

import name.dlazerka.gm.Edge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class EdgePanel extends AbstractEdgePanel {
    private static final Logger logger = LoggerFactory.getLogger(EdgePanel.class);

    private final Edge edge;
    private final VertexPanel tail;
    private final VertexPanel head;
    private final Point ctrlPoint = new Point();
    private boolean curved = false;
    private boolean dragging;
    private Shape hoverShape = curve;
    private Color color = EDGE_COLOR;
    private Stroke stroke = EDGE_STROKE;
    private final Point oddPoint = new Point();

    public EdgePanel(Edge edge, VertexPanel tail, VertexPanel head) {
        this.edge = edge;
        this.tail = tail;
        this.head = head;

        oddPoint.x = (getFromPoint().x + getToPoint().x) / 2;
        oddPoint.y = (getFromPoint().y + getToPoint().y) / 2;

        updateGeometry();

        logger.debug("{}", ctrlPoint);

        setOpaque(false);
        addMouseMotionListener(new DragMouseListener());
        addMouseListener(new MouseListener());
        setComponentPopupMenu(new PopupMenu());
    }

    public Edge getEdge() {
        return edge;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.setStroke(stroke);

        g2.draw(curve);
    }

    @Override
    public boolean contains(int x, int y) {
        boolean b = hoverShape.contains(x, y);
//		if (b) logger.debug("{}", b);
        return b;
    }

    public GraphPanel getGraphPanel() {
        return (GraphPanel) getParent();
    }

    protected Point getFromPoint() {
        return head.getVertexCenter();
    }

    protected Point getToPoint() {
        return tail.getVertexCenter();
    }

    public void setCurved(boolean curved) {
        this.curved = curved;
    }

    /**
     * Moves control poins so that edge will contain given (x, y) point.
     * If the curve seems like line (contains(ctrlPoint)==true) then sets curved to false
     *
     * @param x which point.x the curve should contain
     * @param y which point.y the curve should contain
     */
    private void pullTo(int x, int y) {
        oddPoint.x = x;
        oddPoint.y = y;

        updateGeometry();
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    /**
     * Moves {@link #oddPoint} so like moved edge defined an affine transform.
     *
     * @param vertexPanel
     * @param moveByX
     * @param moveByY
     */
    public void onAdjacentVertexMoved(VertexPanel vertexPanel, int moveByX, int moveByY) {
        Point o; // not moved point
        Point b; // old position of moved point
        Point bb; // new position of moved point

        {// fetch initial values
            if (getFromPoint().equals(vertexPanel.getVertexCenter())) {
                o = getToPoint();
                bb = getFromPoint();
            }
            else {
                o = getFromPoint();
                bb = getToPoint();
            }

            b = new Point();
            b.setLocation(bb);
            b.translate(-moveByX, -moveByY);
        }

        affineMove(oddPoint, o, b, bb);

        updateGeometry();
        repaint();
    }

    /**
     * Moves point a accordingly as affine transform of line o-b to line o-bb.
     *
     * @param a  point to move
     * @param o  not moving point
     * @param b  old position of moved point
     * @param bb new position of moved point
     */
    static void affineMove(Point a, Point o, Point b, Point bb) {
        // make o as base
        int ax, ay;// old position of oddPoint
        int x, y;// new position of oddPoint
        int bx, by;// old position of moved point
        int b1x, b1y;// new position of moved point
        {
            ax = a.x - o.x;
            ay = a.y - o.y;
            bx = b.x - o.x;
            by = b.y - o.y;
            b1x = bb.x - o.x;
            b1y = bb.y - o.y;
        }

        // rounding because simple cast works bad
        x = Math.round(((float) ax * bx * b1x - ay * bx * b1y + ay * by * b1x + ax * by * b1y) / (bx * bx + by * by));
        y = Math.round(((float) ay * bx * b1x + ax * bx * b1y - ax * by * b1x + ay * by * b1y) / (bx * bx + by * by));

        x = x + o.x;
        y = y + o.y;

        a.move(x, y);
    }

    private void updateGeometry() {
        int centerX = (getFromPoint().x + getToPoint().x) / 2;
        int centerY = (getFromPoint().y + getToPoint().y) / 2;

        ctrlPoint.move(
            (oddPoint.x) * 2 - centerX,
            oddPoint.y * 2 - centerY
        );

        setCurved(!contains(ctrlPoint));

        curve.setCurve(
            getFromPoint(),
            curved ? ctrlPoint : getFromPoint(),
            getToPoint()
        );
        hoverShape = EDGE_HOVER_STROKE.createStrokedShape(curve);
    }

    private class DragMouseListener extends MouseMotionAdapter {

        @Override
        public void mouseDragged(MouseEvent e) {
//			logger.trace("{}", e.getPoint());

            pullTo(e.getX(), e.getY());

            repaint();
        }
    }

    private class MouseListener extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            color = EDGE_HOVER_COLOR;
            stroke = EDGE_HOVER_STROKE;
            repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!dragging) {
                color = EDGE_COLOR;
                stroke = EDGE_STROKE;
                repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            setDragging(true);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            setDragging(false);
        }
    }

    private class PopupMenu extends JPopupMenu {
        private PopupMenu() {
            add(new DeleteAction());
        }

        private class DeleteAction extends AbstractAction {
            public DeleteAction() {
                super(Main.getString("delete.edge"));
            }

            public void actionPerformed(ActionEvent e) {
                getGraphPanel().getGraph().remove(edge);
            }
        }
    }
}
