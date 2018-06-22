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

package org.atoiks.games.vcsc.scenes;

import java.awt.Color;

import org.atoiks.games.vcsc.Page;
import org.atoiks.games.framework2d.IGraphics;

public class TitleScene extends Page {

    public TitleScene() {
        super(
            "Title message you say?\nWhat about you?",
            "Okay!", "Bad!", null, "Get me out of here plz..."
        );
    }

    public boolean optionSelected(int opt) {
        switch (opt) {
            case 0:
            case 1:
                // We didn't issue a change scene, so it stays on same page
                return true;
            case 3:
                // As the user wishes, get them out of there!
                return false;
            default:
                // We do not have null options, this will not happen
                return true;
        }
    }
}