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

import org.atoiks.games.framework2d.Scene;
import org.atoiks.games.framework2d.IGraphics;

public final class DummyLoader extends Scene {

    @Override
    public void resize(int w, int h) {
        //
    }

    @Override
    public boolean update(float dt) {
        scene.gotoNextScene();
        return true;
    }

    @Override
    public void render(final IGraphics g) {
        //
    }
}