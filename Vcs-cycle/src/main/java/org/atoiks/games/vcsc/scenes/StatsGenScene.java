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
import org.atoiks.games.vcsc.Skill;
import org.atoiks.games.vcsc.Player;
import org.atoiks.games.vcsc.Weakness;

import org.atoiks.games.framework2d.IGraphics;

public class StatsGenScene extends Page {

    private static final Delta[] phases1 = {
        new Delta(0, 0, 0, 1, -1, 0, Skill.Literacy, Weakness.NONE),
        new Delta(0, 1, 0, 1, 0, 0, Skill.Magic, Weakness.NONE),
        new Delta(0, 0, 0, 1, 0, 1, Skill.Deception, Weakness.NONE),
        new Delta(0, 0, 0, -1, -1, 0, Skill.NONE, Weakness.Sickly),
    };

    private static final Delta[] phases3 = {
        new Delta(0, 0, 0, 0, 1, -1, Skill.Nature, Weakness.NONE),
        new Delta(1, 0, 1, 0, 0, 0, Skill.MeleeWeapon, Weakness.NONE),
        new Delta(0, 0, 1, 0, 1, 0, Skill.Investigation, Weakness.NONE),
        new Delta(-1, 0, -1, 0, 0, 0, Skill.NONE, Weakness.Crippled),
    };

    private int phase = -1;
    private Player cached;

    public StatsGenScene() {
        super(3, 4);
    }

    @Override
    public void enter(int from) {
        cached = (Player) scene.resources().get("player.dat");
        updatePhase(0);
    }

    @Override
    public boolean optionSelected(int opt) {
        int saved = phase;
        final Delta delta = (opt == 0 ? phases1 : opt == 3 ? phases3 : null)[saved++];

        if (delta != null) {
            delta.applyOnto(cached);
        }

        updatePhase(saved);
        return true;
    }

    private void updatePhase(int newPhase) {
        // If we got through all the phases, go to next scene
        if (newPhase >= phases1.length) {
            scene.gotoNextScene();
            return;
        }

        // If phase did not change, nothing changes
        if (phase == newPhase) return;

        switch (newPhase) {
            case 0:
                lines[0] = "You have spent every waking moment preparing for";
                lines[1] = "this day. What have you been doing this whole";
                lines[2] = "time? Did you ...";

                options[0] = "Read books to expand your knowledge";
                options[3] = "Survive in the woods";
                break;
            case 1:
                lines[0] = "";
                lines[1] = "";
                lines[2] = "Did you ...";

                options[0] = "Learn magic";
                options[3] = "Master the blade";
                break;
            case 2:
                // lines do not change
                options[0] = "Have a mentor";
                options[3] = "Live alone";
                break;
            case 3:
                // lines do no change
                options[0] = "Get seriously sick";
                options[3] = "Break your legs";
                break;
        }
        resetScrolling();
        resetOptionSelection();
        phase = newPhase;
    }
}

final class Delta {

    public final int strength;
    public final int intelligence;
    public final int dexterity;
    public final int wisdom;
    public final int constitution;
    public final int charisma;

    public final Skill skill;
    public final Weakness weakness;

    public Delta(int strength, int intelligence, int dexterity,
                 int wisdom, int constitution, int charisma,
                 Skill skill, Weakness weakness) {
        this.strength = strength;
        this.intelligence = intelligence;
        this.dexterity = dexterity;
        this.wisdom = wisdom;
        this.constitution = constitution;
        this.charisma = charisma;

        this.skill = skill == null ? Skill.NONE : skill;
        this.weakness = weakness == null ? Weakness.NONE : weakness;
    }

    public void applyOnto(Player p) {
        p.strength += strength;
        p.intelligence += intelligence;
        p.dexterity += dexterity;
        p.wisdom += wisdom;
        p.constitution += constitution;
        p.charisma += charisma;

        // Assign to next free slot
        for (int i = 0; i < p.skills.length; ++i) {
            if (p.skills[i] == Skill.NONE) {
                p.skills[i] = skill;
                break;
            }
        }
        for (int i = 0; i < p.weaknesses.length; ++i) {
            if (p.weaknesses[i] == Weakness.NONE) {
                p.weaknesses[i] = weakness;
                break;
            }
        }
    }
}