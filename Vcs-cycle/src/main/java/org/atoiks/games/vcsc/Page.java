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

    public static final int MAX_OPTS_PER_SECT = 4;
    public static final int FONT_SIZE = 20;

    private static final Font font = new Font("Monospaced", Font.PLAIN, FONT_SIZE);
    private static final Font info = new Font("Monospaced", Font.PLAIN, 8);

    protected Color bgColor = Color.black;
    protected Color msgColor = Color.white;
    protected Color optColor = Color.white;

    protected Image image = null;

    private final TextWrapStrategy twrapStrat;

    private PositioningStrategy posStrat;

    private float scrollTimer;
    private float scrollDelay;

    private int option;
    private int charProgress;
    private int lineProgress;

    private int optSect;
    private boolean renderSelector = true;

    private String[] lines;
    private String[][] options;
    private int[] optHeight;

    protected Page(TextWrapStrategy twrapStrat) {
        this.twrapStrat = twrapStrat;
    }

    protected void usePositioningStrategy(PositioningStrategy p) {
        this.posStrat = p;
    }

    public void updateOptions(String... opts) {
        this.options = new String[opts.length][];
        for (int i = 0; i < opts.length; ++i) {
            this.options[i] = twrapStrat.wrapOptionText(opts[i]); 
        }

        this.optHeight = new int[Math.min(MAX_OPTS_PER_SECT, opts.length)];
    }

    public void updateMessage(String message) {
        this.lines = twrapStrat.wrapMessageText(message);
    }

    public void updateScrollDelay(float newDelay) {
        this.scrollDelay = Math.max(newDelay, 0);
    }

    public int getLineCount() {
        return lines.length;
    }

    public int getOptionCount() {
        return options.length;
    }

    public void scrollNextLine() {
        ++lineProgress;
        charProgress = 0;
    }

    public void resetScrolling() {
        lineProgress = 0;
        charProgress = 0;
        scrollTimer = 0;
    }

    public void resetOptionSelection() {
        option = 0;
        optSect = 0;
    }

    public boolean doneScrolling() {
        return lineProgress >= lines.length;
    }

    @Override
    public void render(IGraphics g) {
        g.setClearColor(bgColor);
        g.clearGraphics();
        if (image != null) g.drawImage(image, posStrat.getImageX(this), posStrat.getImageY(this));

        final int msgX = posStrat.getMessageX(this);
        final int msgY = posStrat.getMessageY(this);

        g.setFont(font);
        if (charProgress > 0 || lineProgress > 0) {
            // Idea is that if there are no options to render, the message can take up more space
            final int bound = Math.min(lineProgress + 1, lines.length);
            g.setColor(msgColor);
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
                    g.drawString(s, msgX, msgY + i * FONT_SIZE);
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
                    g.drawString(actualMessage, msgX, msgY + i * FONT_SIZE);

                    if (flag) break;
                }
                // otherwise do not render, line will be scrolled through eventually
            }

            // Only render the option list if message was scrolled through entirely
            if (doneScrolling()) {
                final int optY = posStrat.getOptionY(this);
                final int optX = posStrat.getOptionX(this);

                final int lower = optSect * MAX_OPTS_PER_SECT;
                final int optDispCount = Math.min(options.length - lower, MAX_OPTS_PER_SECT);
                g.setColor(optColor);

                int h = optY;
                for (int i = 0; i < optDispCount; ++i) {
                    final int offset = i + lower;
                    if (options[offset].length > 0) {
                        optHeight[offset] = h - FONT_SIZE / 2 + 2;
                        for (final String optLine : options[offset]) {
                            g.drawString(optLine, optX, h);
                            h += FONT_SIZE;
                        }
                    } else {
                        h += FONT_SIZE;
                    }
                }

                if (renderSelector && option >= 0 && option < optHeight.length) {
                    g.setColor(optColor);
                    g.fillCircle(optX - 10, optHeight[option], 5);
                }

                final int optSectCount = (options.length - 1) / MAX_OPTS_PER_SECT;
                if (optSectCount > 0) {
                    g.setColor(msgColor);
                    g.setFont(info);
                    g.drawString("Option Page (" + (optSect + 1) + "/" + (optSectCount + 1) + ")",
                            optX - 20, App.HEIGHT - 4);
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
    public boolean update(float dt) {
        if ((scrollTimer += dt) >= scrollDelay) {
            ++charProgress;
            scrollTimer -= scrollDelay;
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

            // guard against all empty options which would have caused infinte loop
            final int start = option;
            while (options[option].length == 0) {
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
        // Assumes screen is fixed
    }

    @Override
    public void leave() {
        resetScrolling();
        resetOptionSelection();
    }
}