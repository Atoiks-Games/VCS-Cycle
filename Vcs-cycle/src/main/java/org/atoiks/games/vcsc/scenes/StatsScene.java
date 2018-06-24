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

public class StatsScene extends Page {

    private Player cached;

    public StatsScene() {
        super(19, 0);
    }

    @Override
    public void enter(int from) {
        cached = (Player) scene.resources().get("player.dat");

        lines[0]  = "Stats for " + cached.name + ':';
        lines[2]  = "Modifiers:";
        lines[3]  = String.format("  str: %+d", cached.strength);
        lines[4]  = String.format("  int: %+d", cached.intelligence);
        lines[5]  = String.format("  dex: %+d", cached.dexterity);
        lines[6]  = String.format("  wis: %+d", cached.wisdom);
        lines[7]  = String.format("  con: %+d", cached.constitution);
        lines[8]  = String.format("  cha: %+d", cached.charisma);
        lines[10] = "Skills:";
        lines[11] = "  - " + cached.skills[0];
        lines[12] = "  - " + cached.skills[1];
        lines[13] = "  - " + cached.skills[2];
        lines[14] = "  - " + cached.skills[3];
        lines[16] = "Weaknesses:";
        lines[17] = "  - " + cached.weaknesses[0];
        lines[18] = "  - " + cached.weaknesses[1];
    }

    @Override
    public boolean optionSelected(int opt) {
        return false;
    }
}