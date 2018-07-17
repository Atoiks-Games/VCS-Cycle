/**
 *  VCS-Cycle
 *  Copyright (C) 2017-2018  Atoiks-Games <atoiks-games@outlook.com>

 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.atoiks.games.vcsc.scenes;

import java.awt.Color;

import javax.swing.JOptionPane;

import org.atoiks.games.vcsc.Player;
import org.atoiks.games.vcsc.HorizontalPage;

import org.atoiks.games.framework2d.IGraphics;

public class PlayerNameScene extends HorizontalPage {

    private Player cached;
    private boolean dHeart;
    private String enemyName;
    private String motherName;
    private int phase = 0;

    public PlayerNameScene() {
        super(
            "For as long as anyone can remeber, the Bloodsouls and Diamondhearts have been fighting for control over the throne.",
            "Contine"
        );
    }

    @Override
    public void enter(int from) {
        cached = (Player) scene.resources().get("player.dat");
        if(cached.iDoNotExsist){
            cached.dHeart = true;
            cached.enemyName = "Tessa Bloodsoul";
            cached.motherName = "Merideth Diamondheart";
        }
        dHeart = cached.dHeart;
        enemyName = cached.enemyName;
        motherName = cached.motherName;
    }

    @Override
    public boolean optionSelected(int opt) {
        switch (phase) {
            case 0:
                resetScrolling();
                resetOptionSelection();
                updateMessage("Your mother, " + motherName + ", was a fair and kind queen. She was loved by the land.");
                phase++;
            break;

            case 1:
                resetScrolling();
                resetOptionSelection();
                updateMessage("10 years ago, " + enemyName + " murdered her in cold blood. She then took the kindom for herself, brainwashing the commonfolk into obeying her every whim.");
                phase++;
            break;

            case 2:
                resetScrolling();
                resetOptionSelection();
                updateMessage("After years of training, you are ready to take your revenge on " + enemyName + ".");
                phase++;
            break;

            case 3:
                String name;
                while (true) {
                    name = JOptionPane.showInputDialog(null, "What is your first name?", "Atoiks Games - VCS Cycle", JOptionPane.PLAIN_MESSAGE);
                    if (!(name == null || name.trim().isEmpty())) break;
                }

                if(dHeart){
                    cached.name = name + " Diamondheart";
                }else{
                    cached.name = name + " Bloodsoul";
                }

                scene.gotoNextScene();
            break;
        }
    return true;
    }
}
