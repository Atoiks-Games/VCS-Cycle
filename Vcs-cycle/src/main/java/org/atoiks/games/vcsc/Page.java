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

import java.awt.Font;
import java.awt.Color;
import java.awt.Image;

import java.awt.event.KeyEvent;

import java.util.List;
import java.util.ArrayList;

import org.atoiks.games.framework2d.Scene;
import org.atoiks.games.framework2d.IGraphics;

public abstract class Page extends Scene {

    public static final float DEFAULT_SCROLL_DELAY = 0.05f;
    public static final float NO_SCROLL_DELAY = 0;

    protected float time;
    protected float scrollDelay;

    protected int option;
    protected int charProgress;
    protected int lineProgress;

    protected abstract void updateOptions(String... options);

    protected abstract void updateMessage(String message);

    protected void updateScrollDelay(float newDelay) {
        this.scrollDelay = Math.max(newDelay, 0);
    }

    protected void scrollNextLine() {
        ++lineProgress;
        charProgress = 0;
    }

    protected void resetScrolling() {
        lineProgress = 0;
        charProgress = 0;
    }

    protected void resetOptionSelection() {
        option = 0;
    }

    protected String[] wrapText(final String text, final int lineBreakWidth) {
        if (lineBreakWidth < 1) throw new IllegalArgumentException("br-width must >= 1, got " + lineBreakWidth);

        // break text down into lineBreakWidth char-limits lines.
        final String[] msgln = text.split("\n");
        final List<String> list = new ArrayList<>();
        for (String msg : msgln) {
            while (msg.length() > lineBreakWidth) {
                // try to split it at a space or tab that is the furthest away
                final int idxSpc = msg.lastIndexOf(' ', lineBreakWidth);
                final int idxTab = msg.lastIndexOf('\t', lineBreakWidth);

                int k = Math.max(idxSpc, idxTab);
                if (k < 0 || k > lineBreakWidth) k = Math.min(idxSpc, idxTab);
                if (k < 0 || k > lineBreakWidth) k = lineBreakWidth - 1;
                ++k;
                list.add(msg.substring(0, k));
                msg = msg.substring(k);
            }
            list.add(msg);
        }

        return list.toArray(new String[list.size()]);
    }

    public abstract boolean doneScrolling();

    @Override
    public boolean update(float dt) {
        if ((time += dt) >= scrollDelay) {
            ++charProgress;
            time -= scrollDelay;
        }
        return true;
    }

    public abstract boolean optionSelected(int opt);

    @Override
    public void resize(int x, int y) {
        // Assumes screen is fixed
    }

    @Override
    public void leave() {
        resetScrolling();
        resetOptionSelection();
    }
}