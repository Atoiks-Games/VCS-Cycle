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

public class StatsGenScene extends Page {

    private Player cached;

    public StatsGenScene() {
        super(
            "You have spent every waking moment preparing for this day. What have you been doing this whole time? Did you ...",
            "Read books to expand your knowledge",
            "Live alone in the woods"
        );
    }

    @Override
    public void enter(int from) {
        cached = (Player) scene.resources().get("player.dat");
    }

    @Override
    public boolean optionSelected(int opt) {
        switch (opt) {
            default:
                return true;
            case 0:
                cached.wisdom += 1;
                cached.constitution -= 1;
                break;
            case 1:
                cached.constitution += 1;
                cached.charisma -= 1;
                break;
        }

        scene.gotoNextScene();
        return true;
    }
}