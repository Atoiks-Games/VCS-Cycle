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
import org.atoiks.games.vcsc.Skill;

import org.atoiks.games.framework2d.IGraphics;

public class StatsGenScene3 extends Page {

    private Player cached;

    public StatsGenScene3() {
        super(
            "Did you ...",
            "Have a mentor",
            null,
            null,
            "Live alone"
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
                cached.charisma += 1;
                cached.skills[2] = Skill.Deception;
                break;
            case 3:
                cached.dexterity += 1;
                cached.constitution += 1;
                cached.skills[2] = Skill.Investigation;
                break;
        }

        scene.gotoNextScene();
        return true;
    }
}