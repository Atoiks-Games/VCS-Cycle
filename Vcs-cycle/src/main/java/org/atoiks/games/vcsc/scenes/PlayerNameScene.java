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

import org.atoiks.games.vcsc.Page;
import org.atoiks.games.vcsc.Player;

import org.atoiks.games.framework2d.IGraphics;

public class PlayerNameScene extends Page {

    private Player cached;

    public PlayerNameScene() {
        super(
            "10 years ago Damian Silverstone killed your father\nto become the lord.\n\nNow is your time for revenge!",
            "Okay!"
        );
    }

    @Override
    public void enter(int from) {
        cached = (Player) scene.resources().get("player.dat");
    }

    public boolean optionSelected(int opt) {
        String name;
        while (true) {
            name = JOptionPane.showInputDialog(null, "What is your name?", "Atoiks Games - VCS Cycle", JOptionPane.PLAIN_MESSAGE);
            if (!(name == null || name.trim().isEmpty())) break;
        }

        cached.name = name;

        scene.gotoNextScene();
        return true;
    }
}