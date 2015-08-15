package com.basketbomb;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CoryN on 15/08/2015.
 */
public class BasketBomb extends JPanel implements Runnable {

	private final double GAME_HERTZ = 30.0;
	private final double TARGET_FPS = 60;
	private final double MAX_UPDATES_BEFORE_RENDER = 5;
	private final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
	private final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

    private boolean running = false;
    private boolean paused = false;
    private int fps = 60;
    private int frameCount = 0;
    private float interpolation;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            BasketBomb basketBomb = new BasketBomb();
			basketBomb.setPreferredSize(new Dimension(800, 600));

            JFrame frame = new JFrame();
            frame.add(basketBomb);
            frame.pack();
            frame.setVisible(true);

            new Thread(basketBomb).start();
        });
    }

    @Override
    public void run() {
        double lastUpdateTime = System.nanoTime();
        double lastRenderTime;

        int lastSecondTime = (int) (lastUpdateTime / 1000000000);

        while (this.running) {

            double now = System.nanoTime();
            int updateCount = 0;

            if (this.paused) {

            } else {
                while(now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                    updateGame();
                    lastUpdateTime += TIME_BETWEEN_UPDATES;
                    updateCount++;
                }
                if (now - lastUpdateTime <= TIME_BETWEEN_UPDATES) {
                    this.interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
                    repaint();
                    lastRenderTime = now;

                    int thisSecond = (int) (lastUpdateTime / 1000000000);
                    if (thisSecond > lastSecondTime) {
                        this.fps = this.frameCount;
                        this.frameCount = 0;
                        lastSecondTime = thisSecond;
                    }

                    while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                        Thread.yield();
                        try {
                            Thread.sleep(1);
                        } catch(Exception ignore) {}
                        now = System.nanoTime();
                    }
                } else {
                    lastUpdateTime = now - TIME_BETWEEN_UPDATES;
                }
            }
        }
    }

    private void updateGame() {

    }

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
