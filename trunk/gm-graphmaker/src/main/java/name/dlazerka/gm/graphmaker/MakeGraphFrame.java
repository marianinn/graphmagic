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

package name.dlazerka.gm.graphmaker;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class MakeGraphFrame extends JDialog {
	private JPanel contentPane = new JPanel();

	public MakeGraphFrame(Frame owner, String title) {
		super(owner, title, ModalityType.DOCUMENT_MODAL);

		setupUI();
	}

	private void setupUI() {
		contentPane.setLayout(new GridBagLayout());
//		contentPane.setBackground(new Color(-986896));
//		contentPane.setPreferredSize(new Dimension(800, 600));

		JComboBox comboBox = new JComboBox(new String[]{"asdf"});

		contentPane.add(
			comboBox
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

	}
}
