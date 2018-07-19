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

import java.awt.Color;

import org.atoiks.games.framework2d.IGraphics;

import org.atoiks.games.vcsc.twrappers.FixedLocationWrapStrategy;

public abstract class VerticalPage extends Page {

    public static final int LINE_BREAK_WIDTH = 35;

    protected boolean textOnRight = false;

    protected VerticalPage() {
        this(DEFAULT_SCROLL_DELAY, null);
    }

    protected VerticalPage(float scrollDelay) {
        this(scrollDelay, null);
    }

    public VerticalPage(final String message, final String... options) {
        this(DEFAULT_SCROLL_DELAY, message, options);
    }

    public VerticalPage(float scrollDelay, String message, String... options) {
        super(new FixedLocationWrapStrategy(LINE_BREAK_WIDTH, LINE_BREAK_WIDTH - 2));
        updateMessage(message);
        updateOptions(options);
        updateScrollDelay(scrollDelay);
        this.usePositioningStrategy(new PositioningStrat());
    }

    @Override
    public void render(IGraphics g) {
        super.render(g);

        g.setColor(Color.white);
        g.drawLine(App.WIDTH / 2, 0, App.WIDTH / 2, App.HEIGHT);
    }

    private class PositioningStrat implements PositioningStrategy {

        @Override
        public int getMessageX(Page p) {
            return (textOnRight ? App.WIDTH / 2 : 0) + 20;
        }

        @Override
        public int getMessageY(Page p) {
            return 3 * FONT_SIZE / 2;
        }

        @Override
        public int getImageX(Page p) {
            return textOnRight ? 0 : App.WIDTH / 2;
        }

        @Override
        public int getImageY(Page p) {
            return 0;
        }

        @Override
        public int getOptionX(Page p) {
            return getMessageX(p) + 30;
        }

        @Override
        public int getOptionY(Page p) {
            return 3 * App.HEIGHT / 4 + FONT_SIZE;
        }
    }
}