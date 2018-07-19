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
import org.atoiks.games.vcsc.VerticalPage;

import org.atoiks.games.framework2d.IGraphics;

public class StatsScene extends VerticalPage {

    @Override
    public void enter(int from) {
        final Player cached = (Player) scene.resources().get("player.dat");
        cached.shouldSave = true;

        updateMessage(
                "Stats for " + cached.name + ":\n\n" +
                "Modifiers:\n" +
                String.format("  str: %+d\n", cached.strength) +
                String.format("  int: %+d\n", cached.intelligence) +
                String.format("  dex: %+d\n", cached.dexterity) +
                String.format("  wis: %+d\n", cached.wisdom) +
                String.format("  con: %+d\n", cached.constitution) +
                String.format("  cha: %+d\n", cached.charisma) + '\n' +
                "Skills:\n" +
                "  - " + cached.skills[0] + '\n' +
                "  - " + cached.skills[1] + '\n' +
                "  - " + cached.skills[2] + '\n' +
                "  - " + cached.skills[3] + "\n\n" +
                "Weaknesses:\n" +
                "  - " + cached.weaknesses[0] + '\n' +
                "  - " + cached.weaknesses[1]);
    }

    @Override
    public boolean optionSelected(int opt) {
        return false;
    }
}
