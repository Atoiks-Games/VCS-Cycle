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

    public static final float DEFAULT_SCROLL_DELAY = 0.05f;
    public static final float NO_SCROLL_DELAY = 0;

    private static final int FONT_SIZE = 20;
    private static final Font font = new Font("Monospaced", Font.PLAIN, FONT_SIZE);

    private final int[] optHeight;
    private final float scrollDelay;

    private int option;
    private float time;
    private int charProgress;
    private int lineProgress;
    private boolean renderSelector = true;

    protected final String[] lines;
    protected final String[] options;

    protected Color messageColor = Color.white;
    protected Color optionsColor = Color.white;
    protected Image background = null;

    public Page(final int lines, final int options) {
        this(DEFAULT_SCROLL_DELAY, lines, options);
    }

    public Page(float scrollDelay, int lines, int options) {
        this.lines = new String[lines];
        this.options = new String[options];
        this.optHeight = new int[options];
        this.scrollDelay = scrollDelay;
    }

    public Page(final String message, final String... options) {
        this(DEFAULT_SCROLL_DELAY, message, options);
    }

    public Page(float scrollDelay, String message, String... options) {
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
        this.scrollDelay = scrollDelay;
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

            if (charProgress > 0 || lineProgress > 0) {
                final int baseHeight = 3 * App.HEIGHT / 4 - Math.max(lines.length - 3, 1) * 20;
                final int bound = Math.min(lineProgress + 1, lines.length);
                g2d.setColor(messageColor);
                for (int i = 0; i < bound; ++i) {
                    final String s = lines[i];
                    if (s == null) {
                        // Do not render <null>
                        if (i < lineProgress) {
                            continue;
                        } else {
                            scrollNextLine();
                            break;
                        }
                    }

                    if (i < lineProgress) {
                        // Render full line, line was already scrolled through
                        g2d.drawString(s, 20, baseHeight + i * FONT_SIZE);
                    } else if (i == lineProgress) {
                        // Render partial line, line is currently being scrolled through
                        boolean flag = false;
                        int k = charProgress;
                        if (k >= s.length()) {
                            k = s.length();
                            scrollNextLine();
                            flag = true;
                        }
                        final String actualMessage = s.substring(0, k);
                        g2d.drawString(actualMessage, 20, baseHeight + i * FONT_SIZE);

                        if (flag) break;
                    }
                    // otherwise do not render, line will be scrolled through eventually
                }

                // Only render the option list if message was scrolled through entirely
                if (doneScrolling()) {
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
            }
        }

        if (doneScrolling() && renderSelector && option >= 0 && option < optHeight.length) {
            g.setColor(optionsColor);
            g.fillCircle(30, optHeight[option], 5);
        }
    }

    private void normalizeOption() {
        option = option < 0 ? optHeight.length - 1
               : option >= optHeight.length ? 0
               : option;
    }

    protected void scrollNextLine() {
        ++lineProgress;
        charProgress = 0;
    }

    protected void resetScrolling() {
        lineProgress = 0;
        charProgress = 0;
    }

    public boolean doneScrolling() {
        return lineProgress >= lines.length;
    }

    @Override
    public boolean update(float dt) {
        if ((time += dt) >= scrollDelay) {
            ++charProgress;
            time -= scrollDelay;
        }

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

        if (scene.keyboard().isKeyPressed(KeyEvent.VK_ENTER)) {
            if (doneScrolling()) {
                return optionSelected(option);
            } else {
                scrollNextLine();
            }
        }
        return true;
    }

    public abstract boolean optionSelected(int opt);

    @Override
    public void resize(int x, int y) {
        // Screen is fixed
    }
}