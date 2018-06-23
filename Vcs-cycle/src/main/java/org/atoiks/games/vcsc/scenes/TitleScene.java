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

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import java.awt.Color;
import java.awt.Image;

import java.awt.event.KeyEvent;

import javax.imageio.ImageIO;

import org.atoiks.games.vcsc.Player;

import org.atoiks.games.framework2d.Scene;
import org.atoiks.games.framework2d.IGraphics;

public class TitleScene extends Scene {

    private Player cached;
    private boolean skipGeneration;
    private boolean firstRun;
    private Image background;

    public TitleScene() {
        firstRun = true;
    }

    @Override
    public void render(IGraphics g) {
        g.setClearColor(Color.black);
        g.clearGraphics();
        g.drawImage(background, 0, 0);
    }

    @Override
    public void enter(int from) {
        if (firstRun) {
            try {
                background = ImageIO.read(this.getClass().getResourceAsStream("/title.png"));
            } catch (Exception ex) {
            }

            // Read user data
            try (final ObjectInputStream ois = new ObjectInputStream(new FileInputStream("player.dat"))) {
                scene.resources().put("player.dat", (Player) ois.readObject());
                skipGeneration = true;
            } catch (Exception ex) {
                // Start from the beginning
                scene.resources().put("player.dat", new Player());
                skipGeneration = false;
            }
        }
    }

    @Override
    public boolean update(float dt) {
        if (scene.keyboard().isKeyPressed(KeyEvent.VK_ENTER)) {
            if (skipGeneration) scene.switchToScene(3);
            else scene.gotoNextScene();
        }
        return true;
    }

    @Override
    public void resize(int w, int h) {
        // Fixed screen size
    }
}