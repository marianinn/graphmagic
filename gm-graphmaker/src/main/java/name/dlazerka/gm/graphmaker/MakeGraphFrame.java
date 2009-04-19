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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class MakeGraphFrame extends JDialog {
	private JPanel contentPane = new JPanel();
	private List<GraphMakerItem> itemList;
	private JButton okButton;
	private JComboBox comboBox;
	private static final Logger logger = LoggerFactory.getLogger(MakeGraphFrame.class);
	private JPanel paramsPanel;

	public MakeGraphFrame(Frame owner, String title, List<GraphMakerItem> itemList) {
		super(owner, title, ModalityType.DOCUMENT_MODAL);
		this.itemList = itemList;

		setLocationRelativeTo(owner);
		
		setupUI();
		setupControls();
	}

	private void setupControls() {
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				GraphMakerItem item = (GraphMakerItem) e.getItem();
				paramsPanel.removeAll();
				item.fillParamsPanel(paramsPanel);
				pack();
			}
		});


		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphMakerItem item = (GraphMakerItem) comboBox.getModel().getSelectedItem();
				item.perform();
			}
		});
	}

	private void setupUI() {
		this.setContentPane(contentPane);
		contentPane.setPreferredSize(new Dimension(300, 200));
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

		gbc.gridy = GridBagConstraints.RELATIVE;
		paramsPanel = new JPanel();
		contentPane.add(paramsPanel, gbc);

		okButton = new JButton("OK");
		contentPane.add(okButton, gbc);

		pack();
	}
}
