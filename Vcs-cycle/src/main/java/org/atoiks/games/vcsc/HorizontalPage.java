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

import org.atoiks.games.framework2d.IGraphics;

public abstract class HorizontalPage extends Page {

    public static final int LINE_BREAK_WIDTH = 50;

    private static final int MAX_OPTS_PER_SECT = 4;
    private static final int FONT_SIZE = 20;
    private static final Font font = new Font("Monospaced", Font.PLAIN, FONT_SIZE);
    private static final Font info = new Font("Monospaced", Font.PLAIN, 8);

    private int optSect;
    private boolean renderSelector = true;

    private String[] lines;
    private String[] options;
    private int[] optHeight;

    protected Color messageColor = Color.white;
    protected Color optionsColor = Color.white;
    protected Image background = null;

    protected HorizontalPage() {
        this(DEFAULT_SCROLL_DELAY);
    }

    protected HorizontalPage(float scrollDelay) {
        this.lines = new String[0];
        this.options = new String[0];
        this.optHeight = new int[0];
        updateScrollDelay(scrollDelay);
    }

    public HorizontalPage(final String message, final String... options) {
        this(DEFAULT_SCROLL_DELAY, message, options);
    }

    public HorizontalPage(float scrollDelay, String message, String... options) {
        updateMessage(message);
        updateOptions(options);
        updateScrollDelay(scrollDelay);
    }

    @Override
    protected final void updateOptions(String... options) {
        this.options = options;
        this.optHeight = new int[options.length];
    }

    @Override
    protected final void updateMessage(String message) {
        // break message down into LINE_BREAK_WIDTH char-limits lines.
        final String[] msgln = message.split("\n");
        final List<String> list = new ArrayList<>();
        for (String msg : msgln) {
            while (msg.length() > LINE_BREAK_WIDTH) {
                // try to split it at a space or tab that is the furthest away
                final int idxSpc = msg.lastIndexOf(' ', LINE_BREAK_WIDTH);
                final int idxTab = msg.lastIndexOf('\t', LINE_BREAK_WIDTH);

                int k = Math.max(idxSpc, idxTab);
                if (k < 0 || k > LINE_BREAK_WIDTH) k = Math.min(idxSpc, idxTab);
                if (k < 0 || k > LINE_BREAK_WIDTH) k = LINE_BREAK_WIDTH - 1;
                ++k;
                list.add(msg.substring(0, k));
                msg = msg.substring(k);
            }
            list.add(msg);
        }

        list.toArray(this.lines = new String[list.size()]);
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

    @Override
    protected void resetOptionSelection() {
        super.resetOptionSelection();
        optSect = 0;
    }

    @Override
    public boolean doneScrolling() {
        return lineProgress >= lines.length;
    }

    @Override
    public boolean update(float dt) {
        super.update(dt);

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
}