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

package org.atoiks.games.vcsc;

import java.io.Serializable;

import java.util.Arrays;

public final class Player implements Serializable {

    private static final long serialVerionUID = 12938472870L;

    public final Skill[] skills = new Skill[4];
    public final Weakness[] weaknesses = new Weakness[2];

    public int strength = 0;
    public int intelligence = 0;
    public int dexterity = 0;
    public int wisdom = 0;
    public int constitution = 0;
    public int charisma = 0;

    public String name = null;
    public String enemyName = "Tessa Bloodsoul";
    public String motherName = "Merideth Diamondheart";

    public boolean dHeart = true;
    public boolean iDoNotExsist = false;
    public boolean shouldSave = false;

    public Player() {
        Arrays.fill(skills, Skill.NONE);
        Arrays.fill(weaknesses, Weakness.NONE);
    }

    public boolean hasSkill(Skill skill) {
        for (final Skill s : skills) {
            if (s == skill) return true;
        }
        return false;
    }

    public boolean hasWeakness(Weakness weakness) {
        for (final Weakness w : weaknesses) {
            if (w == weakness) return true;
        }
        return false;
    }
}
