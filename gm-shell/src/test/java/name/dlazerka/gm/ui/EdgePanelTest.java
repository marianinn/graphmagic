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

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class EdgePanelTest {
    @Test
    public void testAffine() {
        Point point = new Point(10, 10);
        Point o = new Point(0, 0);
        Point b = new Point(20, 0);
        Point bb = new Point(20, -10);

        EdgePanel.affineMove(point, o, b, bb);

        Assert.assertEquals(15, point.x, 0.0001);
        Assert.assertEquals(5, point.y, 0.0001);
    }
}
