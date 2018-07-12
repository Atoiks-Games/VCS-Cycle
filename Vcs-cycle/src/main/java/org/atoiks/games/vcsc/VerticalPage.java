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

public abstract class VerticalPage extends Page {

    public static final int LINE_BREAK_WIDTH = 25;

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
        super(LINE_BREAK_WIDTH);
        updateMessage(message);
        updateOptions(options);
        updateScrollDelay(scrollDelay);
    }

    @Override
    protected int calcMessageX() {
        return (textOnRight ? App.WIDTH / 2 : 0) + 20;
    }

    @Override
    protected int calcMessageY() {
        return 3 * App.HEIGHT / 4 - Math.max(getLineCount() - (getOptionCount() == 0 ? 3 : 1), 1) * FONT_SIZE;
    }

    @Override
    protected int calcImageX() {
        return textOnRight ? 0 : App.WIDTH / 2;
    }

    @Override
    protected int calcImageY() {
        return 0;
    }
}