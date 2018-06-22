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
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.KeyEvent;

import java.util.List;
import java.util.ArrayList;

import org.atoiks.games.framework2d.Scene;
import org.atoiks.games.framework2d.IGraphics;

public abstract class Page extends Scene {

    private static final int FONT_SIZE = 20;
    private static final Font font = new Font("Monospaced", Font.PLAIN, FONT_SIZE);

    private final int[] optHeight;
    private int option;
    private boolean renderSelector = true;

    protected final String[] lines;
    protected final String[] options;

    protected Color messageColor = Color.white;
    protected Color optionsColor = Color.white;
    protected Image background;

    public Page(int lines, int options) {
        this.lines = new String[lines];
        this.options = new String[options];
        this.optHeight = new int[options];
    }

    public Page(String message, String... options) {
        // break message down into and 50 char limits lines.
        final String[] msgln = message.split("\n");
        final List<String> list = new ArrayList<>();
        for (String msg : msgln) {
            while (msg.length() > 50) {
                // try to split it at a space
                int k = msg.lastIndexOf(' ', 50);
                if (k < 0 || k > 50) k = msg.lastIndexOf('\t', 50);
                if (k < 0 || k > 50) k = 49; // minus 1 because ++k later
                ++k;
                list.add(msg.substring(0, k));
                msg = msg.substring(k);
            }
            if (!msg.isEmpty()) list.add(msg);
        }

        list.toArray(this.lines = new String[list.size()]);
        this.options = options;
        this.optHeight = new int[options.length];
    }

    @Override
    public void render(IGraphics g) {
        g.setClearColor(Color.black);
        g.clearGraphics();
        if (background != null) g.drawImage(background, 0, 0);

        {
            final Graphics2D g2d = (Graphics2D) g.getRawGraphics();
            g2d.setFont(font);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            final int baseHeight = 3 * App.HEIGHT / 4 - Math.max(lines.length - 3, 1) * 20;
            g2d.setColor(messageColor);
            for (int i = 0; i < lines.length; ++i) {
                final String s = lines[i];
                if (s == null) continue;    // Do not render <null>
                g2d.drawString(s, 20, baseHeight + i * FONT_SIZE);
            }

            final int newBase = baseHeight + (lines.length + 1) * FONT_SIZE;
            g2d.setColor(optionsColor);
            for (int i = 0; i < options.length; ++i) {
                final int h = newBase + i * FONT_SIZE;
                final String s = options[i];
                if (s == null) continue;    // Do not render <null>
                g2d.drawString(s, 40, h);
                optHeight[i] = h - FONT_SIZE / 2 + 2;
            }
        }

        if (renderSelector && option > 0 && option < optHeight.length) {
            g.setColor(optionsColor);
            g.fillCircle(30, optHeight[option], 5);
        }
    }

    private void normalizeOption() {
        option = option < 0 ? optHeight.length - 1
               : option >= optHeight.length ? 0
               : option;
    }

    @Override
    public boolean update(float dt) {
        if (options.length > 0) {
            int delta = 0;
            if (scene.keyboard().isKeyPressed(KeyEvent.VK_UP)) delta = -1;
            if (scene.keyboard().isKeyPressed(KeyEvent.VK_DOWN)) delta = +1;

            option += delta;
            normalizeOption();

            // guard against all null options which would have caused infinte loop
            final int start = option;
            while (options[option] == null) {
                // Skip that index, it is non-selectable
                option += delta;
                normalizeOption();
                if (start == option) {
                    renderSelector = false;
                    option = -1;
                    break;
                }
            }
        } else {
            renderSelector = false;
            option = -1;
        }

        return scene.keyboard().isKeyPressed(KeyEvent.VK_ENTER) ? optionSelected(option) : true;
    }

    public abstract boolean optionSelected(int opt);

    @Override
    public void resize(int x, int y) {
        // Screen is fixed
    }
}