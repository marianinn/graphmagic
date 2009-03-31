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
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
		String stackTraceString = Exceptions.makeStackTrace(new Exception("sdf", throwable));
		JTextArea textArea = new JTextArea(stackTraceString);
		textArea.setAutoscrolls(true);
		JScrollPane stackTraceContainer = new JScrollPane(textArea);
		stackTraceContainer.setPreferredSize(new Dimension(600, 250));

		final JLabel showStackTraceLabel = new JLabel(Main.getString("show.stack.trace"));
		showStackTraceLabel.setForeground(Color.BLUE);
		showStackTraceLabel.addMouseMotionListener(new UnderlineOnHoverListener(showStackTraceLabel));

		AttributedString attributedString = new AttributedString(stackTraceString);
		attributedString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);


//				Font font1 = showStackTraceLabel.getFont().deriveFont();
//				showStackTraceLabel.setFont(font1.);
		JPanel showStackTraceContainer = new JPanel(new BorderLayout());
		showStackTraceContainer.add(showStackTraceLabel, BorderLayout.EAST);


		JTextArea messageArea = new JTextArea(throwable.getLocalizedMessage());
		messageArea.setRows(1);
		messageArea.setBackground(getParent().getBackground());
		messageArea.setFont(getParent().getFont());

		JPanel errorPanel = new JPanel();
		GroupLayout layout = new GroupLayout(errorPanel);
		layout.setAutoCreateGaps(true);
		errorPanel.setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(messageArea)
				.addComponent(showStackTraceContainer)
				.addComponent(stackTraceContainer)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(messageArea)
				.addComponent(showStackTraceContainer)
				.addComponent(stackTraceContainer)
		);

		enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);

		getContentPane().addMouseMotionListener(
			new MouseInputAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					logger.debug("asdfsdfg");
					throw new RuntimeException("asdf");
				}
			}
		);

		getContentPane().add(errorPanel);
	}

	protected void setupControls() {
		setTitle(Main.getString("error"));

//		Container contentPane = getContentPane();
//		contentPane.setLayout(new BorderLayout());
//		contentPane.add(this, BorderLayout.CENTER);
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
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				dispose();
			}
		});
	}

	public void setValue(Object newValue) {
		Object oldValue = value;
		value = newValue;
		firePropertyChange(JOptionPane.VALUE_PROPERTY, oldValue, value);
	}
}
