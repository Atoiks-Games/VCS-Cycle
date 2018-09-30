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
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import java.awt.Font;
import java.awt.FontFormatException;

import org.atoiks.games.framework2d.FrameInfo;
import org.atoiks.games.framework2d.swing.Frame;

import org.atoiks.games.vcsc.scenes.*;

public class App {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    public static final Font MONOSPACE_FONT;
    
    static {
        Font local = null;
        try {
            local = Font.createFont(Font.PLAIN, App.class.getResourceAsStream("/VT323-Regular.ttf"));
        } catch (IOException | FontFormatException ex) {
            // Fallback to using a generic Monospace font
            local = new Font("Monospace", Font.PLAIN, 16);
        } finally {
            MONOSPACE_FONT = local;
        }
    }

    public static void main(String[] args) {
        final FrameInfo info = new FrameInfo()
                .setTitle("Atoiks Games - VCS Cycle")
                .setResizable(false)
                .setSize(WIDTH, HEIGHT)
                .setLoader(new DummyLoader())
                .setGameScenes(new TitleScene(), new PlayerNameScene(), new StatsGenScene(), new StatsScene());
        final Frame frame = new Frame(info);
        try {
            frame.init();
            frame.loop();
        } finally {
            // Save user data
            final Player p = (Player) frame.getSceneManager().resources().get("player.dat");
            if (p != null && p.shouldSave) {
                try (final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("player.dat"))) {
                    oos.writeObject(p);
                } catch (IOException ex) {
                    // Welp, pray, cuz you will start from the beginning next time
                }
            }

            frame.close();
        }
    }
}
