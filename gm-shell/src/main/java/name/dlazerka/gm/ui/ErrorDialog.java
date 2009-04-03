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

import name.dlazerka.gm.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class ErrorDialog extends JDialog {
	private static final Logger logger = LoggerFactory.getLogger(ErrorDialog.class);

	private static final int STYLE = JRootPane.ERROR_DIALOG;
	private Object value;
	private final Throwable throwable;
	private JScrollPane stackTraceContainer;
	private GroupLayout layout;

	public ErrorDialog(Window parent, Throwable throwable) {
		super(parent);
		this.throwable = throwable;
		setupUI();
		setupControls();
	}

	public static void showError(Throwable t, Component owner) {
		ErrorDialog errorDialog;

		if (!(owner instanceof Window)) {
			owner = SwingUtilities.getWindowAncestor(owner);
		}
		errorDialog = new ErrorDialog((Window) owner, t);
		errorDialog.setResizable(true);
		errorDialog.pack();
		errorDialog.setVisible(true);
	}

	protected void setupUI() {
//		JPanel errorPanel = new JPanel();
//		getContentPane().add(errorPanel);
		layout = new GroupLayout(this.getContentPane());
//		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
//		errorPanel.setLayout(layout);
		this.getContentPane().setLayout(layout);

		JTextArea messageArea = new JTextArea(throwable.getLocalizedMessage());
		messageArea.setRows(1);
		messageArea.setPreferredSize(new Dimension(600, 20));
		messageArea.setBackground(getParent().getBackground());
		messageArea.setFont(getParent().getFont());

		String stackTraceString = Exceptions.makeStackTrace(throwable);
		JTextArea stackTraceArea = new JTextArea(stackTraceString);
		stackTraceArea.setAutoscrolls(true);
		stackTraceContainer = new JScrollPane(stackTraceArea);
		stackTraceContainer.setPreferredSize(new Dimension(600, 250));
		stackTraceContainer.setVisible(false);

		final JCheckBox showStackTrace = new JCheckBox(Main.getString("show.stack.trace"));
		showStackTrace.setForeground(Color.BLUE);
		showStackTrace.addActionListener(new ShowStackTraceListener());

		AttributedString attributedString = new AttributedString(stackTraceString);
		attributedString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
					layout.createSequentialGroup()
						.addComponent(messageArea)
						.addComponent(showStackTrace)
				)
//				.addComponent(messageArea)
//				.addComponent(topContainer)
				.addComponent(stackTraceContainer)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
					.addComponent(messageArea)
					.addComponent(showStackTrace)
			)
				.addComponent(stackTraceContainer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
	}

	protected void setupControls() {
		setTitle(Main.getString("error"));

		if (JDialog.isDefaultLookAndFeelDecorated()) {
			boolean supportsWindowDecorations =
				UIManager.getLookAndFeel().getSupportsWindowDecorations();
			if (supportsWindowDecorations) {
				setUndecorated(true);
				getRootPane().setWindowDecorationStyle(STYLE);
			}
		}
		pack();
		setLocationRelativeTo(getParent());
		addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					dispose();
				}
			}
		);
	}

	public void setValue(Object newValue) {
		Object oldValue = value;
		value = newValue;
		firePropertyChange(JOptionPane.VALUE_PROPERTY, oldValue, value);
	}

	private class ShowStackTraceListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ErrorDialog dialog = ErrorDialog.this;
			stackTraceContainer.setVisible(!stackTraceContainer.isVisible());
			double newHeight = dialog.getPreferredSize().getHeight();
			dialog.setSize(dialog.getWidth(), (int) newHeight);
//			ErrorDialog.this.pack();
//			invalidate();
		}
	}
}
