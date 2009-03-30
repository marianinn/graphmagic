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

import name.dlazerka.gm.GraphMagicPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class UI {
	private static final Logger logger = LoggerFactory.getLogger(UI.class);

	private static MainFrame mainFrame;

	public static void show() {
		initLookAndFeel();

		mainFrame = new MainFrame();
//		mainFrame.pack();
		mainFrame.setExtendedState(Frame.NORMAL);
		mainFrame.setVisible(true);
	}

	private static void initLookAndFeel() {
		try {
			try {
				UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName()
				);
			}
			catch (UnsupportedLookAndFeelException e) {
				UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName()
				);
			}
		}
		catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (InstantiationException e1) {
			e1.printStackTrace();
		}
		catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
	}

	public static void addPlugin(GraphMagicPlugin plugin) {
		mainFrame.addPlugin(plugin);
		logger.info("Added plugin {} ({})", new Object[]{plugin.getName(), plugin.getClass()});
	}
}
