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

import java.util.Random;

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

    private final Random rnd = new Random();

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
            // Before switching scenes, BE RANDOM!
            final Skill[] skills = Skill.values();
            Skill s = skills[rnd.nextInt(skills.length)];
            while (cached.hasSkill(s)) {
                s = skills[rnd.nextInt(skills.length)];
            }
            cached.skills[3] = s;

            final Weakness[] weaknesses = Weakness.values();
            Weakness w = weaknesses[rnd.nextInt(weaknesses.length)];
            while (cached.hasWeakness(w)) {
                w = weaknesses[rnd.nextInt(weaknesses.length)];
            }
            cached.weaknesses[1] = w;

            scene.gotoNextScene();
            return;
        }

        // If phase did not change, nothing changes
        if (phase == newPhase) return;

        switch (newPhase) {
            case 0:
                updateMessage("You have spent every waking moment preparing for\nthis day. What have you been doing this whole\ntime? Did you ...");
                updateOptions("Read books to expand your knowledge", null, null, "Survive in the woods");
                break;
            case 1:
                updateMessage("\n\nDid you ...");
                updateOptions("Learn magic", null, null, "Master the blade");
                break;
            case 2:
                // lines do not change
                updateOptions("Have a mentor", null, null, "Live alone");
                break;
            case 3:
                // lines do no change
                updateOptions("Get seriously sick", null, null, "Break your legs");
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
