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
import java.awt.event.KeyEvent;

import org.atoiks.games.vcsc.Player;
import org.atoiks.games.vcsc.VerticalPage;

import org.atoiks.games.framework2d.IGraphics;

public class PlayerNameScene extends VerticalPage {

    private Player cached;
    private boolean dHeart;
    private String enemyName;
    private String motherName;
    private int phase = 0;

    private final StringBuilder nameBuf = new StringBuilder();

    public PlayerNameScene() {
        super(
            "For as long as anyone can remeber, the Bloodsouls and Diamondhearts have been fighting for control over the throne.",
            "Continue"
        );
    }

    @Override
    public void enter(int from) {
        cached = (Player) scene.resources().get("player.dat");
        cached.dHeart = true;
        cached.enemyName = "Tessa Bloodsoul";
        cached.motherName = "Merideth Diamondheart";
        dHeart = cached.dHeart;
        enemyName = cached.enemyName;
        motherName = cached.motherName;
    }

    @Override
    public boolean update(final float dt) {
        final boolean r = super.update(dt);
        if (r && phase == 4) {
            final String s = scene.keyboard().getTypedChars();
            switch (s) {
                case "":
                case "\n":
                case "\r":
                    // Ignore these characters
                    break;
                case "\b": {
                    // backspace, delete buffer
                    final int len = nameBuf.length();
                    if (len > 0) {
                        nameBuf.deleteCharAt(len - 1);
                        updateMessage("Your name: " + nameBuf);
                    }
                    break;
                }
                default:
                    nameBuf.append(s);
                    updateMessage("Your name: " + nameBuf);
                    break;
            }
        }
        return r;
    }

    @Override
    public boolean optionSelected(int opt) {
        switch (phase) {
            case 0:
                updateMessage("Your mother, " + motherName + ", was a fair and kind queen. She was loved by the land.");
                resetScrolling();
                resetOptionSelection();
                phase++;
                break;
            case 1:
                updateMessage("10 years ago, " + enemyName + " murdered her in cold blood. She then took the kindom for herself, brainwashing the commonfolk into obeying her every whim.");
                resetScrolling();
                resetOptionSelection();
                phase++;
                break;
            case 2:
                updateMessage("After years of training, you are ready to take your revenge on " + enemyName + ".");
                resetScrolling();
                resetOptionSelection();
                phase++;
                break;
            case 3:
                updateMessage("What is your name?\nType it here directly");
                resetScrolling();
                phase++;
                // Next phase requires char capturing
                scene.keyboard().captureTypedChars(true);
                break;
            case 4: {
                // Disable char capturing
                scene.keyboard().captureTypedChars(false);
                final String firstName = nameBuf.toString().trim();
                if (firstName.isEmpty()) {
                    // Re-enable char capturing, no empty names allowed
                    scene.keyboard().captureTypedChars(true);
                    nameBuf.setLength(0);
                } else {
                    phase++;
                    cached.name = firstName + (dHeart ? " Diamondheart" : " Bloodsoul");

                    scene.gotoNextScene();
                }
                break;
            }
        }
        return true;
    }
}
