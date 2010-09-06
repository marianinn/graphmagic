/*
 * GraphMagic, package for scientists working in graph theory.
 * Copyright (C) 2009 Dzmitry Lazerka www.dlazerka.name
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
 * Author: Dzmitry Lazerka www.dlazerka.name
 */

package name.dlazerka.gm.ui;

import name.dlazerka.gm.Graph;
import name.dlazerka.gm.GraphMagicPlugin;
import name.dlazerka.gm.GraphUI;
import name.dlazerka.gm.basic.BasicGraph;
import name.dlazerka.gm.basic.GraphModificationListenerAdapter;
import name.dlazerka.gm.pluginloader.PluginWrapper;
import name.dlazerka.gm.shell.ResourceBundle;
import name.dlazerka.gm.shell.store.LoadGraphActionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class MainFrame extends JFrame {
	private final static Logger logger = LoggerFactory.getLogger(MainFrame.class);

	private JPanel contentPane = new JPanel();
	private GraphPanel graphPanel;
	private JTabbedPane leftTabbedPane;
	private PluginsTable pluginsTable;
	private JButton addPluginButton = new JButton(ResourceBundle.getString("add.plugin"));
	private JCheckBox directedCheckBox;
	private JCheckBox multiCheckBox;
	private JCheckBox pseudoCheckBox;
	private JMenu pluginsMenu;

	private final NotImplementedActionListener notImplementedActionListener;
	private final NewGraphActionListener newGraphActionListener;
	private final SaveGraphActionListener saveGraphActionListener;
	private final LoadGraphActionListener loadGraphActionListener;

	public MainFrame() {
		BasicGraph graph = new BasicGraph();
//		graph.setDirected(false);
//		graph.setMulti(false);
//		graph.setPseudo(false);

		GraphUI ui = graph.getUI();
		ui.setOwnerFrame(this);

		Main.getGraphMagicAPI().getGraphs().add(graph);
		Main.getGraphMagicAPI().setFocused(graph);

		graphPanel = new GraphPanel(graph);

		saveGraphActionListener = new SaveGraphActionListener(this, graph);
		newGraphActionListener = new NewGraphActionListener();
		loadGraphActionListener = new LoadGraphActionListener(this, graphPanel.getGraph());
		notImplementedActionListener = new NotImplementedActionListener(this);

		setupUI();

		registerCommands();

		setUpGraphListener(graph);
	}

	private void setUpGraphListener(final BasicGraph graph) {
		graph.addChangeListener(
				new GraphModificationListenerAdapter() {
					@Override
					public void notifyAttached() {
						directedCheckBox.getModel().setSelected(
								graph.isDirected()
						);// will be true when it will be implemented in gm-core:Basic* classes
						multiCheckBox.getModel().setSelected(
								graph.isMulti()
						);// will be true when it will be implemented in gm-core:Basic* classes
						pseudoCheckBox.getModel().setSelected(
								graph.isPseudo()
						);// will be true when it will be implemented in gm-core:Basic* classes
					}

					@Override
					public void setDirected(boolean directed) {
						directedCheckBox.getModel().setSelected(directed);
					}

					@Override
					public void setMulti(boolean multi) {
						multiCheckBox.getModel().setSelected(multi);
					}

					@Override
					public void setPseudo(boolean pseudo) {
						pseudoCheckBox.getModel().setSelected(pseudo);
					}
				}
		);
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

		// Save Graph on Ctrl-S
		contentPane.registerKeyboardAction(
				saveGraphActionListener, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
		);

		addPluginButton.addActionListener(new AddPluginActionListener(MainFrame.this));
	}

	private void onClose() {
		dispose();
	}


	private void setupUI() {
		setTitle(ResourceBundle.getString("main.title"));
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
		leftTabbedPane.addTab(ResourceBundle.getString("controls"), controlsPanel);

		setUpControlPanel(controlsPanel);

		final JPanel pluginsPanel = new JPanel(new GridBagLayout());
		leftTabbedPane.addTab(ResourceBundle.getString("plugins"), pluginsPanel);
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
			JMenu menu = new JMenu(ResourceBundle.getString("file"));
			menuBar.add(menu);
			menu.setMnemonic(KeyEvent.VK_F);
			{
				JMenuItem item = new JMenuItem(ResourceBundle.getString("new.graph"));
				item.addActionListener(newGraphActionListener);
				item.setMnemonic(KeyEvent.VK_N);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(ResourceBundle.getString("load.graph"));
				item.addActionListener(loadGraphActionListener);
				item.setMnemonic(KeyEvent.VK_L);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(ResourceBundle.getString("save.graph"));
				item.addActionListener(saveGraphActionListener);
				item.setMnemonic(KeyEvent.VK_S);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(ResourceBundle.getString("save.as"));
				item.setEnabled(false);
				item.addActionListener(notImplementedActionListener);
				item.setMnemonic(KeyEvent.VK_A);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(ResourceBundle.getString("print"));
				item.setEnabled(false);
				item.addActionListener(notImplementedActionListener);
				item.setMnemonic(KeyEvent.VK_P);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(ResourceBundle.getString("export.as.image"));
				item.setEnabled(false);
				item.addActionListener(notImplementedActionListener);
				item.setMnemonic(KeyEvent.VK_E);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(ResourceBundle.getString("load.plugin"));
				item.addActionListener(new AddPluginActionListener(MainFrame.this));
				item.setMnemonic(KeyEvent.VK_L);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(ResourceBundle.getString("exit"));
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
			JMenu menu = new JMenu(ResourceBundle.getString("settings"));
			menuBar.add(menu);
			menu.setMnemonic(KeyEvent.VK_S);
			{
				JMenuItem item = new JMenuItem(ResourceBundle.getString("graph"));
				item.setEnabled(false);
				item.addActionListener(notImplementedActionListener);
				item.setMnemonic(KeyEvent.VK_G);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(ResourceBundle.getString("appearance"));
				item.setEnabled(false);
				item.addActionListener(notImplementedActionListener);
				item.setMnemonic(KeyEvent.VK_A);
				menu.add(item);
			}
		}
		{
			pluginsMenu = new JMenu(ResourceBundle.getString("plugins"));
//			menuBar.add(pluginsMenu);
			pluginsMenu.setMnemonic(KeyEvent.VK_P);
		}
		{
			JMenu menu = new JMenu(ResourceBundle.getString("about"));
			menuBar.add(menu);
			menu.setMnemonic(KeyEvent.VK_A);
			{
				JMenuItem item = new JMenuItem(ResourceBundle.getString("help.topics"));
				item.setEnabled(false);
				item.addActionListener(notImplementedActionListener);
				item.setMnemonic(KeyEvent.VK_H);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(ResourceBundle.getString("check.for.update"));
				item.setEnabled(false);
				item.addActionListener(notImplementedActionListener);
				item.setMnemonic(KeyEvent.VK_C);
				menu.add(item);
			}
			{
				JMenuItem item = new JMenuItem(ResourceBundle.getString("about"));
				item.setEnabled(false);
				item.addActionListener(notImplementedActionListener);
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

		directedCheckBox = new JCheckBox(ResourceBundle.getString("directed"));
		controlsPanel.add(directedCheckBox, gbc);
		directedCheckBox.setEnabled(false);// will be true when it will be implemented in gm-core:Basic* classes
		directedCheckBox.getModel().addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Graph graph = graphPanel.getGraph();
						boolean pressed = directedCheckBox.getModel().isSelected();
//						graph.setDirected(pressed);
					}
				}
		);

		multiCheckBox = new JCheckBox(ResourceBundle.getString("multigraph"));
		controlsPanel.add(multiCheckBox, gbc);
		multiCheckBox.setEnabled(false);// will be true when it will be implemented in gm-core:Basic* classes
		multiCheckBox.getModel().addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Graph graph = graphPanel.getGraph();
						boolean pressed = multiCheckBox.getModel().isSelected();
//						graph.setMulti(pressed);
					}
				}
		);

		pseudoCheckBox = new JCheckBox(ResourceBundle.getString("pseudo"));
		controlsPanel.add(pseudoCheckBox, gbc);
		pseudoCheckBox.setEnabled(false);// will be true when it will be implemented in PseudoEdgePanel#updateGeometry()
		pseudoCheckBox.getModel().addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Graph graph = graphPanel.getGraph();
						boolean pressed = pseudoCheckBox.getModel().isSelected();
//						graph.setPseudo(pressed);
					}
				}
		);

		gbc.weighty = 1;
		controlsPanel.add(new JPanel(), gbc);
	}

	public JComponent getRootComponent() {
		return contentPane;
	}

	public void registerPlugin(PluginWrapper pluginWrapper) {
		pluginsTable.addPlugin(pluginWrapper);

		GraphMagicPlugin plugin = pluginWrapper.getPlugin();
		java.util.List<Action> actionList = plugin.getActions();
		actionList.size();
		pluginsMenu.add(
				new JMenuItem(
						new AbstractAction() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// todo
							}
						}
				)
		);
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

	private class NewGraphActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Graph graph = graphPanel.getGraph();
			graph.clear();
		}
	}

	public Graph getGraph() {
		return graphPanel.getGraph();
	}
}
