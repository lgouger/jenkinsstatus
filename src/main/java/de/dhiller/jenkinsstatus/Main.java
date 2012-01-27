/*
 * Copyright (c) 2011, dhiller, http://www.dhiller.de Daniel Hiller, Warendorfer Str. 47, 48231 Warendorf, NRW,
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
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.prefs.Preferences;

import javax.swing.*;

import org.jdom.JDOMException;

import com.apple.eawt.FullScreenListener;
import com.apple.eawt.AppEvent.FullScreenEvent;

public class Main extends JFrame {

    private final OSXUtils osXUtils = new OSXUtils();
    private StatusProvider statusProvider = new DefaultStatusProvider();

    private final class UndecoratedButton extends JButton {

	private UndecoratedButton(Action a) {
	    super(a);
	    UndecoratedButton.this.setBorderPainted(false);
	}
    }

    private final class CloseFrame extends AbstractAction {
	private CloseFrame() {
	    super("", new ImageIcon(Main.class.getResource("/off.png"), ""));
	    putValue(SHORT_DESCRIPTION, "Exit");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    Main.this.dispose();
	    System.exit(0);
	}
    }

    private final class EditPreferences extends AbstractAction {
	private EditPreferences() {
	    super("",
		    new ImageIcon(Main.class.getResource("/settings.png"), ""));
	    putValue(SHORT_DESCRIPTION, "Edit preferences");
	}

	public void actionPerformed(ActionEvent e) {
	    new PreferencesDialog(Main.this, "Settings", true).setVisible(true);
	}
    }

    static final Preferences preferences = Preferences
	    .userNodeForPackage(Main.class);

    private final StatusPanel status = new StatusPanel();

    private final Timer statusUpdateTimer = new Timer(1000,
	    new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    initStatus();
		}
	    });

    Main() {
	this(false);
	setBackground(Color.BLACK);
    }

    Main(boolean disableTimer) {
	super("Jenkins Status");
	setUpOSXFullScreen();
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.getContentPane().setLayout(new BorderLayout());
	status.setName("statusPanel");
	final JScrollPane comp = new JScrollPane(status);
	comp.setBorder(BorderFactory.createEmptyBorder());
	comp.getVerticalScrollBar().setBackground(Color.black);
	comp.getHorizontalScrollBar().setBackground(Color.black);
	this.getContentPane().add(comp);
	final JPanel buttons = new JPanel();
	buttons.setLayout(new GridLayout(1, 1));
	buttons.setBackground(Color.BLACK);
	buttons.add(new UndecoratedButton(new EditPreferences()));
	final JPanel east = new JPanel();
	east.setBackground(Color.BLACK);
	east.add(buttons, BorderLayout.NORTH);
	this.getContentPane().add(east, BorderLayout.EAST);
	this.setExtendedState(Frame.MAXIMIZED_BOTH);
	if (!disableTimer)
	    statusUpdateTimer.start();
    }

    void setUpOSXFullScreen() {
	try {
	    osXUtils.markWindowAsFullScreen(this);
	} catch (Exception e) {
	    System.out.println("Bad luck, no os x fullscreen support"); //$NON-NLS-1$ // TODO: Remove
	}
    }

    void addFullScreenListener() {
	osXUtils.addFullScreenListener(this, new FullScreenListener() {

	    @Override
	    public void windowEnteredFullScreen(FullScreenEvent arg0) {
	    }

	    @Override
	    public void windowEnteringFullScreen(FullScreenEvent arg0) {
	    }

	    @Override
	    public void windowExitedFullScreen(FullScreenEvent arg0) {
	    }

	    @Override
	    public void windowExitingFullScreen(FullScreenEvent arg0) {
	    }
	});
    }

    StatusUpdater initStatus() {
	final StatusUpdater statusUpdater = new StatusUpdater(preferences,
		status, Main.this);
	statusUpdater.setStatusProvider(statusProvider);
	statusUpdater.execute();
	return statusUpdater;
    }

    public static void main(String[] args) throws URISyntaxException {

	System.setProperty("apple.laf.useScreenMenuBar", "true"); //$NON-NLS-1$ //$NON-NLS-2$
	System.setProperty("apple.awt.showGrowBox", "false"); //$NON-NLS-1$ //$NON-NLS-2$

	if (args.length > 0 && args[0].matches("http:\\/\\/[^:](:[0-9]+)?/.*"))
	    PreferencesDialog.saveURI(args[0]);
	SwingUtilities.invokeLater(new Runnable() {

	    public void run() {
		new Main().setVisible(true);
	    }
	});
    }

    StatusProvider getStatusProvider() {
	return statusProvider;
    }

    void setStatusProvider(StatusProvider statusProvider) {
	this.statusProvider = statusProvider;
    }

}
