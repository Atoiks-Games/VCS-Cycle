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

public abstract class HorizontalPage extends Page {

    public static final int LINE_BREAK_WIDTH = 50;

    protected HorizontalPage() {
        this(DEFAULT_SCROLL_DELAY, null);
    }

    protected HorizontalPage(float scrollDelay) {
        this(scrollDelay, null);
    }

    public HorizontalPage(final String message, final String... options) {
        this(DEFAULT_SCROLL_DELAY, message, options);
    }

    public HorizontalPage(float scrollDelay, String message, String... options) {
        super(LINE_BREAK_WIDTH);
        updateMessage(message);
        updateOptions(options);
        updateScrollDelay(scrollDelay);
        this.usePositioningStrategy(new PositioningStrat());
    }

    private class PositioningStrat implements PositioningStrategy {

        @Override
        public int getMessageX(Page p) {
            return 0;
        }

        @Override
        public int getMessageY(Page p) {
            return 3 * App.HEIGHT / 4 - Math.max(p.getLineCount() - (p.getOptionCount() == 0 ? 3 : 1), 1) * FONT_SIZE;
        }

        @Override
        public int getImageX(Page p) {
            return 0;
        }

        @Override
        public int getImageY(Page p) {
            return 0;
        }

        @Override
        public int getOptionX(Page p) {
            return 30;
        }

        @Override
        public int getOptionY(Page p) {
            return 3 * App.HEIGHT / 4 + 3 * FONT_SIZE / 2;
        }
    }
}