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

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.atoiks.games.framework2d.FrameInfo;
import org.atoiks.games.framework2d.swing.Frame;

import org.atoiks.games.vcsc.scenes.*;

public class App {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    public static void main(String[] args) {
        final FrameInfo info = new FrameInfo()
                .setTitle("Atoiks Games - VCS Cycle")
                .setResizable(false)
                .setSize(WIDTH, HEIGHT)
                .setScenes(new TitleScene(), new StatsGenScene(), new StatsScene());
        final Frame frame = new Frame(info);
        try {
            boolean skipGeneration;
            // Read user data
            try (final ObjectInputStream ois = new ObjectInputStream(new FileInputStream("player.dat"))) {
                frame.getSceneManager().resources().put("player.dat", (Player) ois.readObject());
                skipGeneration = true;
            } catch (Exception ex) {
                // Start from the beginning
                frame.getSceneManager().resources().put("player.dat", new Player());
                skipGeneration = false;
            }

            frame.init();

            if (skipGeneration) {
                // Set this to StatsScene
                frame.getSceneManager().switchToScene(2);
            }

            frame.loop();
        } finally {
            // Save user data
            try (final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("player.dat"))) {
                oos.writeObject(frame.getSceneManager().resources().get("player.dat"));
            } catch (IOException ex) {
                // Welp, pray, cuz you will start from the beginning next time
            }

            frame.close();
        }
    }
}