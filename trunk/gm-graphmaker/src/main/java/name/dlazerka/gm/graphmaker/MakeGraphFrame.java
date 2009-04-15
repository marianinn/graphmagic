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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class MakeGraphFrame extends JDialog {
	private JPanel contentPane = new JPanel();
	private List<GraphMakerItem> itemList;
	private JButton okButton;
	private JComboBox comboBox;

	public MakeGraphFrame(Frame owner, String title, List<GraphMakerItem> itemList) {
		super(owner, title, ModalityType.DOCUMENT_MODAL);
		this.itemList = itemList;
		setupUI();
		setupControls();
	}

	private void setupControls() {
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphMakerItem item = (GraphMakerItem) comboBox.getModel().getSelectedItem();
				item.actionPerformed(e);
			}
		});
	}

	private void setupUI() {
		this.setContentPane(contentPane);
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20, 20, 20, 20);
//		contentPane.setBackground(new Color(-986896));
//		contentPane.setPreferredSize(new Dimension(800, 600));

		comboBox = new JComboBox();
		contentPane.add(comboBox, gbc);

		for (GraphMakerItem item : itemList) {
			comboBox.addItem(item);
		}

		okButton = new JButton("OK");

		gbc.gridy = GridBagConstraints.RELATIVE;
		contentPane.add(okButton, gbc);

		pack();
	}
}
