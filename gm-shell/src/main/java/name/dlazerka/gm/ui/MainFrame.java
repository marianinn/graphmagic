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

import name.dlazerka.gm.Graph;
import name.dlazerka.gm.GraphMagicPlugin;
import name.dlazerka.gm.basic.ObservableBasicGraph;
import name.dlazerka.gm.pluginloader.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class MainFrame extends JFrame {
	private final static Logger logger = LoggerFactory.getLogger(MainFrame.class);

	private JPanel contentPane = new JPanel();
	private GraphPanel graphPanel;
	private JTabbedPane leftTabbedPane;
	private PluginsTable pluginsTable;
	private JButton addPluginButton = new JButton(Main.getString("add.plugin"));
	private Collection<GraphMagicPlugin> plugins = new LinkedList<GraphMagicPlugin>();

	public MainFrame() {
		ObservableBasicGraph graph = new ObservableBasicGraph();

		Main.getGraphMagicAPI().getGraphs().add(graph);
		Main.getGraphMagicAPI().setFocused(graph);

		graphPanel = new GraphPanel(graph);
		graphPanel.getGraph().getUI().setOwnerFrame(this);

		setupUI();

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

		addPluginButton.addActionListener(new AddPluginActionListener(MainFrame.this));
	}

	private void onClose() {
		dispose();
	}


	private void setupUI() {
		setTitle(Main.getString("main.title"));
		setSize(800, 600);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);

		setContentPane(contentPane);
		contentPane.setLayout(new GridBagLayout());
		contentPane.setBackground(new Color(-986896));
		contentPane.setEnabled(true);
		contentPane.setInheritsPopupMenu(false);
		contentPane.setRequestFocusEnabled(true);
		contentPane.setToolTipText("");
//		contentPane.setPreferredSize(new Dimension(800, 600));

		final JMenuBar menuBar = new JMenuBar();
		this.getRootPane().setJMenuBar(menuBar);
		setUpMenuBar(menuBar);

		final JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(200);
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(0);
		GridBagConstraints gbc = new GridBagConstraints(
			GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE,
			1, 1,
			1, 1,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0),
			0, 0
		);
		contentPane.add(splitPane, gbc);

		final JPanel graphCardLayoutPanel = new JPanel(new CardLayout());
		splitPane.setRightComponent(graphCardLayoutPanel);
		graphPanel.setBackground(new Color(-1));

		GraphScrollPane scrollPane = new GraphScrollPane(graphPanel);
		graphCardLayoutPanel.add(scrollPane, "graph1");

		leftTabbedPane = new JTabbedPane();
		splitPane.setLeftComponent(leftTabbedPane);

		final JPanel controlsPanel = new JPanel(new GridBagLayout());
		leftTabbedPane.addTab(Main.getString("controls"), controlsPanel);

		setUpControlPanel(controlsPanel);

		final JPanel pluginsPanel = new JPanel(new GridBagLayout());
		leftTabbedPane.addTab(Main.getString("plugins"), pluginsPanel);
		//leftTabbedPane.setSelectedIndex(1);

		gbc.gridx = GridBagConstraints.PAGE_START;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		pluginsPanel.add(addPluginButton, gbc);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		pluginsTable = new PluginsTable();
		pluginsPanel.add(pluginsTable, gbc);
	}

	private void setUpMenuBar(JMenuBar menuBar) {
		{
			JMenu menu = new JMenu(Main.getString("file"));
			menuBar.add(menu);
			menu.setMnemonic(KeyEvent.VK_F);
			{
				JMenuItem item = new JMenuItem(Main.getString("new.graph"));
				item.setEnabled(false);
				item.addActionListener(new NotImplementedActionListener(MainFrame.this));
				item.setMnemonic(KeyEvent.VK_N);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(Main.getString("load"));
				item.setEnabled(false);
				item.addActionListener(new NotImplementedActionListener(MainFrame.this));
				item.setMnemonic(KeyEvent.VK_L);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(Main.getString("save"));
				item.setEnabled(false);
				item.addActionListener(new NotImplementedActionListener(MainFrame.this));
				item.setMnemonic(KeyEvent.VK_S);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(Main.getString("save.as"));
				item.setEnabled(false);
				item.addActionListener(new NotImplementedActionListener(MainFrame.this));
				item.setMnemonic(KeyEvent.VK_A);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(Main.getString("print"));
				item.setEnabled(false);
				item.addActionListener(new NotImplementedActionListener(MainFrame.this));
				item.setMnemonic(KeyEvent.VK_P);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(Main.getString("export.as.image"));
				item.setEnabled(false);
				item.addActionListener(new NotImplementedActionListener(MainFrame.this));
				item.setMnemonic(KeyEvent.VK_E);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(Main.getString("load.plugin"));
				item.addActionListener(new AddPluginActionListener(MainFrame.this));
				item.setMnemonic(KeyEvent.VK_L);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(Main.getString("exit"));
				item.setMnemonic(KeyEvent.VK_X);
				item.addActionListener(
					new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							onClose();
						}
					}
				);
				menu.add(item);
			}
		}
		{
			JMenu menu = new JMenu("Settings");
			menuBar.add(menu);
			menu.setMnemonic(KeyEvent.VK_S);
			{
				JMenuItem item = new JMenuItem(Main.getString("graph"));
				item.setEnabled(false);
				item.addActionListener(new NotImplementedActionListener(MainFrame.this));
				item.setMnemonic(KeyEvent.VK_G);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(Main.getString("appearance"));
				item.setEnabled(false);
				item.addActionListener(new NotImplementedActionListener(MainFrame.this));
				item.setMnemonic(KeyEvent.VK_A);
				menu.add(item);
			}
		}
		{
			JMenu menu = new JMenu(Main.getString("about"));
			menuBar.add(menu);
			menu.setMnemonic(KeyEvent.VK_A);
			{
				JMenuItem item = new JMenuItem(Main.getString("help.topics"));
				item.setEnabled(false);
				item.addActionListener(new NotImplementedActionListener(MainFrame.this));
				item.setMnemonic(KeyEvent.VK_H);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(Main.getString("check.for.update"));
				item.setEnabled(false);
				item.addActionListener(new NotImplementedActionListener(MainFrame.this));
				item.setMnemonic(KeyEvent.VK_C);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(Main.getString("about"));
				item.setEnabled(false);
				item.addActionListener(new NotImplementedActionListener(MainFrame.this));
				item.setMnemonic(KeyEvent.VK_A);
				menu.add(item);
			}
		}
	}

	private void setUpControlPanel(JPanel controlsPanel) {
		GridBagConstraints gbc = new GridBagConstraints(
			0, GridBagConstraints.RELATIVE,
			1, 1,
			1, 0,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0),
			0, 0
		);

		final JCheckBox directedCheckBox = new JCheckBox(Main.getString("directed"));
		controlsPanel.add(directedCheckBox, gbc);
		directedCheckBox.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Graph graph = graphPanel.getGraph();
				boolean pressed = directedCheckBox.getModel().isPressed();
				graph.setDirected(pressed);
			}
		});

		final JCheckBox pseudoCheckBox = new JCheckBox(Main.getString("pseudo"));
		controlsPanel.add(pseudoCheckBox, gbc);
		directedCheckBox.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Graph graph = graphPanel.getGraph();
				boolean pressed = pseudoCheckBox.getModel().isPressed();
				graph.setPseudo(pressed);
			}
		});

		gbc.weighty = 1;
		controlsPanel.add(new JPanel(), gbc);
	}

	public JComponent getRootComponent() {
		return contentPane;
	}

	public void registerPlugin(PluginWrapper pluginWrapper) {
		pluginsTable.addPlugin(pluginWrapper);
	}

	private class NotImplementedActionListener implements ActionListener {
		public NotImplementedActionListener(
			MainFrame mainFrame
		) {
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			ErrorDialog.showError(new UnsupportedOperationException("Not Implemented Yet"), MainFrame.this);
		}
	}
}
