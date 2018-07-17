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

package org.atoiks.games.vcsc.twrappers;

import java.util.List;
import java.util.ArrayList;

import org.atoiks.games.vcsc.TextWrapStrategy;

public class FixedLocationWrapStrategy implements TextWrapStrategy {

    private final int msgWidth;
    private final int optWidth;

    public FixedLocationWrapStrategy(int msgWidth, int optWidth) {
        this.msgWidth = msgWidth;
        this.optWidth = optWidth;
    }

    public String[] wrapMessageText(String text) {
        return helper(text, msgWidth);
    }

    public String[] wrapOptionText(String text) {
        return helper(text, optWidth);
    }

    private static String[] helper(final String text, final int width) {
        if (text == null || width < 1) return new String[0];

        // break text down into width char-limits lines.
        final String[] msgln = text.split("\n");
        final List<String> list = new ArrayList<>();
        for (String msg : msgln) {
            while (msg.length() > width) {
                // try to split it at a space or tab that is the furthest away
                final int idxSpc = msg.lastIndexOf(' ', width);
                final int idxTab = msg.lastIndexOf('\t', width);

                int k = Math.max(idxSpc, idxTab);
                if (k < 0 || k > width) k = Math.min(idxSpc, idxTab);
                if (k < 0 || k > width) k = width - 1;
                ++k;
                list.add(msg.substring(0, k));
                msg = msg.substring(k);
            }
            list.add(msg);
        }

        return list.toArray(new String[list.size()]);
    }
}