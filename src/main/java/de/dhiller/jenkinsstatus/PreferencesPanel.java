/*
 * Copyright (c) 2012, dhiller, http://www.dhiller.de Daniel Hiller, Warendorfer Str. 47, 48231 Warendorf, NRW,
 * Germany, All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer. - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided with the distribution. - Neither the
 * name of dhiller nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.dhiller.jenkinsstatus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

final class PreferencesPanel extends JPanel {

    /**
     */
    private final PreferencesDialog preferencesDialog;
    private final Main main;

    /**
     * @param preferencesDialog
     */
    PreferencesPanel(PreferencesDialog preferencesDialog) {
	this(null, preferencesDialog);
    }

    /**
     * @param preferencesDialog
     */
    PreferencesPanel(Main main) {
	this(main, null);
    }
    
    private abstract class Cancel extends AbstractAction {
	protected Cancel() {
	    super("Cancel");
	}
	
    }

    private final class DisposeDialogOnCancel extends Cancel {
	private DisposeDialogOnCancel() {
	    super();
	}

	public void actionPerformed(ActionEvent e) {
	    preferencesDialog.dispose();
	}
    }

    private abstract class Save extends AbstractAction {
	protected final JTextField serverURI;

	private Save(JTextField serverURI) {
	    super("OK");
	    this.serverURI = serverURI;
	}

	public void actionPerformed(ActionEvent e) {
	    try {
		final String uriText = serverURI.getText();
		PreferencesDialog.saveURI(uriText);
		update();
		dismiss();
	    } catch (Exception e1) {
		handle(e1);
	    }
	}

	protected abstract void handle(Exception e1);

	protected abstract void update();

	protected abstract void dismiss();
    }

    private final class DisposeDialogOnSave extends Save {

	private DisposeDialogOnSave(JTextField serverURI) {
	    super(serverURI);
	}

	protected void handle(Exception e1) {
	    e1.printStackTrace(); // TODO
	    JOptionPane.showMessageDialog(preferencesDialog, "URI invalid!");
	}

	protected void update() {
	    preferencesDialog.main.initStatus();
	}

	protected void dismiss() {
	    preferencesDialog.dispose();
	}

    }

    private PreferencesPanel(Main main, PreferencesDialog preferencesDialog) {
	if (main == null && preferencesDialog == null)
	    throw new IllegalArgumentException("Both null!?");
	setBackground(Color.BLACK);
	this.main = main;
	this.preferencesDialog = preferencesDialog;
	final JPanel preferencesMainPanel = new JPanel();
	preferencesMainPanel.setBackground(Color.BLACK);
	preferencesMainPanel.setLayout(new GridLayout(1, 2));
	preferencesMainPanel.add(new JLabel("Server URI"));
	final JTextField serverURI = new JTextField();
	serverURI.setText(Main.preferences.get(Constants.SERVER_URI, ""));
	preferencesMainPanel.add(serverURI);
	this.add(preferencesMainPanel);
	final JPanel okCancel = new JPanel();
	okCancel.setBackground(Color.BLACK);
	okCancel.setLayout(new FlowLayout(FlowLayout.RIGHT));
	if (preferencesDialog != null) {
	    okCancel.add(new JButton(new DisposeDialogOnSave(serverURI)));
	    okCancel.add(new JButton(new DisposeDialogOnCancel()));
	} else {
	    okCancel.add(new JButton(new Save(serverURI) {

		protected void handle(Exception e1) {
		    e1.printStackTrace(); // TODO
		    JOptionPane.showMessageDialog(PreferencesPanel.this.main,
			    "URI invalid!");
		}

		protected void update() {
		    PreferencesPanel.this.main.initStatus();
		}

		protected void dismiss() {
		    setVisible(false);
		}
	    }));
	    okCancel.add(new JButton(new Cancel() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    setVisible(false);
		}

	    }));
	}
	this.add(okCancel, BorderLayout.SOUTH);

    }
}