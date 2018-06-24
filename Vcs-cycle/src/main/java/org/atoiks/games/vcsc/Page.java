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

    private static final int MAX_OPTS_PER_SECT = 4;
    private static final int FONT_SIZE = 20;
    private static final Font font = new Font("Monospaced", Font.PLAIN, FONT_SIZE);
    private static final Font info = new Font("Monospaced", Font.PLAIN, 8);

    private final int[] optHeight;
    private final float scrollDelay;

    private int optSect;
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

        g.setFont(font);
        if (charProgress > 0 || lineProgress > 0) {
            // Idea is that if there are no options to render, the message can take up more space
            final int baseHeight = 3 * App.HEIGHT / 4 -
                    Math.max(lines.length - (optHeight.length == 0 ? 3 : 1), 1) * FONT_SIZE;
            final int bound = Math.min(lineProgress + 1, lines.length);
            g.setColor(messageColor);
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
                    g.drawString(s, 20, baseHeight + i * FONT_SIZE);
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
                    g.drawString(actualMessage, 20, baseHeight + i * FONT_SIZE);

                    if (flag) break;
                }
                // otherwise do not render, line will be scrolled through eventually
            }

            // Only render the option list if message was scrolled through entirely
            if (doneScrolling()) {
                final int newBase = baseHeight + (lines.length + 1) * FONT_SIZE;
                final int lower = optSect * MAX_OPTS_PER_SECT;
                final int optDispCount = Math.min(options.length - lower, MAX_OPTS_PER_SECT);
                g.setColor(optionsColor);
                for (int i = 0; i < optDispCount; ++i) {
                    final int offset = i + lower;
                    final int h = newBase + i * FONT_SIZE;
                    final String s = options[offset];
                    if (s == null) continue;    // Do not render <null>
                    g.drawString(s, 40, h);
                    optHeight[offset] = h - FONT_SIZE / 2 + 2;
                }

                if (renderSelector && option >= 0 && option < optHeight.length) {
                    g.setColor(optionsColor);
                    g.fillCircle(30, optHeight[option], 5);
                }

                final int optSectCount = (options.length - 1) / MAX_OPTS_PER_SECT;
                if (optSectCount > 0) {
                    g.setColor(messageColor);
                    g.setFont(info);
                    g.drawString("Option Page (" + (optSect + 1) + "/" + (optSectCount + 1) + ")",
                            10, App.HEIGHT - 4);
                }
            }
        }
    }

    private void normalizeOption() {
        if (option < 0) {
            option = optHeight.length - 1;
        } else if (option >= optHeight.length) {
            option = 0;
        }

        // Update optSect to display the correct list of options
        optSect = option / MAX_OPTS_PER_SECT;
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
        optSect = 0;
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
            // Only contains -1, 0, +1
            int delta = 0;

            // Modify option by 1
            if (scene.keyboard().isKeyPressed(KeyEvent.VK_UP)) delta = -1;
            if (scene.keyboard().isKeyPressed(KeyEvent.VK_DOWN)) delta = +1;
            option += delta;

            // Modify option by a full optSect
            if (scene.keyboard().isKeyPressed(KeyEvent.VK_LEFT)) {
                option = (--optSect) * MAX_OPTS_PER_SECT;
                delta = -1;
            }
            if (scene.keyboard().isKeyPressed(KeyEvent.VK_RIGHT)) {
                option = (++optSect) * MAX_OPTS_PER_SECT;
                delta = +1;
            }

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

    @Override
    public void leave() {
        resetScrolling();
        resetOptionSelection();
    }
}