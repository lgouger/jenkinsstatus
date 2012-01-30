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

package de.dhiller.steelseries.extras;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

public class DefectLightbulbTest {

    public static void main(String[] args) {
	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		final JFrame frame = new JFrame() {

		    final DefectLightbulb defectLightbulb = new DefectLightbulb();
		    final JCheckBox checkBox = new JCheckBox();

		    {
			setBackground(Color.BLACK);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			getContentPane().setPreferredSize(
				new Dimension(200, 150));
			getContentPane()
				.setMinimumSize(new Dimension(200, 150));
			add(defectLightbulb);
			final AbstractAction action = new AbstractAction() {

			    @Override
			    public void actionPerformed(ActionEvent e) {
				defectLightbulb.setOn(checkBox.isSelected());
			    }
			};
			checkBox.setAction(action);
			add(checkBox, BorderLayout.SOUTH);
			pack();
		    }
		};
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	    }
	});
    }
}
