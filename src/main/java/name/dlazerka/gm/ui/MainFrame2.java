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
import java.awt.*;
import java.awt.event.*;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class MainFrame2 extends JFrame {
	private final static Logger logger = LoggerFactory.getLogger(MainFrame.class);

	private JPanel contentPane = new JPanel();
	private GraphPanel graphPanel = new GraphPanel();
	private JTabbedPane tabbedPane1;
	private JTable table1;
	private JButton addPluginButton;

	public MainFrame2() {
		setupUI();

		setContentPane(contentPane);
		setTitle(Main.getString("main.title"));

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
//		final JSplitPane splitPane1 = new JSplitPane();
//		splitPane1.setDividerLocation(200);
//		splitPane1.setOrientation(1);
//		splitPane1.setResizeWeight(1.0);
//		contentPane.add(
//			splitPane1, new com.intellij.uiDesigner.core.GridConstraints(
//				1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
//				com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
//				null, null, null, 0, false
//			)
//		);
//		final JPanel panel1 = new JPanel();
//		panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
//		splitPane1.setRightComponent(panel1);
//		graphPanel.setBackground(new Color(-1));
//		panel1.add(
//			graphPanel, new com.intellij.uiDesigner.core.GridConstraints(
//				0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
//				com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
//				null, new Dimension(800, 600), null, 0, false
//			)
//		);
//		tabbedPane1 = new JTabbedPane();
//		splitPane1.setLeftComponent(tabbedPane1);
//		final JPanel panel2 = new JPanel();
//		panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
//		tabbedPane1.addTab(ResourceBundle.getBundle("messages").getString("controls"), panel2);
//		final JPanel panel3 = new JPanel();
//		panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
//		tabbedPane1.addTab(ResourceBundle.getBundle("messages").getString("plugins"), panel3);
//		table1 = new JTable();
//		panel3.add(
//			table1, new com.intellij.uiDesigner.core.GridConstraints(
//				1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
//				com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null,
//				0, false
//			)
//		);
//		addPluginButton = new JButton();
//		this.$$$loadButtonText$$$(addPluginButton, ResourceBundle.getBundle("messages").getString("add.plugin"));
//		panel3.add(
//			addPluginButton, new com.intellij.uiDesigner.core.GridConstraints(
//				0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
//				com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
//			)
//		);
//		final JToolBar toolBar1 = new JToolBar();
//		contentPane.add(
//			toolBar1, new com.intellij.uiDesigner.core.GridConstraints(
//				0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
//				com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
//				com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0,
//				false
//			)
//		);
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
