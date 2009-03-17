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

import name.dlazerka.gm.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class MainFrame2 extends JFrame {
	private final static Logger logger = LoggerFactory.getLogger(MainFrame2.class);

	private JPanel contentPane = new JPanel();
	private GraphPanel graphPanel = new GraphPanel();
	private JTabbedPane controlsTabbedPane;
	private JTable pluginsTable;
	private JButton addPluginButton;

	public MainFrame2() {
		setupUI();

		setContentPane(contentPane);
		setTitle(Main.getString("main.title"));

		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		registerCommands();
	}

	private void registerCommands() {
		// call onClose() when cross is clicked
		addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					onClose();
				}
			}
		);

// call onClose() on ESCAPE
		contentPane.registerKeyboardAction(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onClose();
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
		);

	}

	private void onClose() {
		dispose();
	}


	private void setupUI() {
		contentPane.setLayout(new GridBagLayout());
		contentPane.setBackground(new Color(-986896));
		contentPane.setEnabled(true);
		contentPane.setInheritsPopupMenu(false);
		contentPane.setRequestFocusEnabled(true);
		contentPane.setToolTipText("");
//		contentPane.setPreferredSize(new Dimension(800, 600));

		final JMenuBar menuBar = new JMenuBar();
		JMenuItem fileMenuItem = new JMenuItem(Main.getString("file"));
		menuBar.add(fileMenuItem);
		this.getRootPane().setJMenuBar(menuBar);


		final JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(200);
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(0);
		contentPane.add(
			splitPane
			, new GridBagConstraints(
				GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE,
				1, 1,
				1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0
//				1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
//				com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
//				null, null, null, 0, false
			)
		);

		final JPanel graphCardLayoutPanel = new JPanel(new CardLayout());
		splitPane.setRightComponent(graphCardLayoutPanel);
		graphPanel.setBackground(new Color(-1));
		graphCardLayoutPanel.add(
			graphPanel, "graph1"
//			graphPanel, new com.intellij.uiDesigner.core.GridConstraints(
//				0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
//				com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
//				null, new Dimension(800, 600), null, 0, false
//			)
		);
		controlsTabbedPane = new JTabbedPane();
		splitPane.setLeftComponent(controlsTabbedPane);
		final JPanel controlsPanel = new JPanel();
//		controlsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		controlsTabbedPane.addTab(ResourceBundle.getBundle("messages").getString("controls"), controlsPanel);
		final JPanel pluginsPanel = new JPanel(new GridBagLayout());
//		pluginsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		controlsTabbedPane.addTab(ResourceBundle.getBundle("messages").getString("plugins"), pluginsPanel);
		controlsTabbedPane.setSelectedIndex(1);

		addPluginButton = new JButton();
		this.loadButtonText(addPluginButton, ResourceBundle.getBundle("messages").getString("add.plugin"));
		pluginsPanel.add(
			addPluginButton, new GridBagConstraints(
				GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE,
				1, 1,
				1, 0,
				GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0),
				0, 0
			)
//			, new com.intellij.uiDesigner.core.GridConstraints(
//				0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
//				com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
//			)
		);

		pluginsTable = new JTable();
		pluginsTable.addColumn(new TableColumn());
		pluginsPanel.add(
			pluginsTable, new GridBagConstraints(
				0, GridBagConstraints.RELATIVE,
				1, 1,
				1, 1,
				GridBagConstraints.CENTER,
				GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0
			)
//			 , new com.intellij.uiDesigner.core.GridConstraints(
//				1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
//				com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null,
//				0, false
//			)
		);
	}


	private void loadButtonText(AbstractButton component, String text) {
		StringBuffer result = new StringBuffer();
		boolean haveMnemonic = false;
		char mnemonic = '\0';
		int mnemonicIndex = -1;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '&') {
				i++;
				if (i == text.length()) break;
				if (!haveMnemonic && text.charAt(i) != '&') {
					haveMnemonic = true;
					mnemonic = text.charAt(i);
					mnemonicIndex = result.length();
				}
			}
			result.append(text.charAt(i));
		}
		component.setText(result.toString());
		if (haveMnemonic) {
			component.setMnemonic(mnemonic);
			component.setDisplayedMnemonicIndex(mnemonicIndex);
		}
	}


	public JComponent getRootComponent() {
		return contentPane;
	}
}
