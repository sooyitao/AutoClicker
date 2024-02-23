package com.autokeyboard.autoclicker;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Objects;

public class autoClick {
    private static Robot robot;
    private static boolean running = false;

    public static void stop() {
        running = false;
    }

    public static void start(String hours, String minutes, String seconds, String milisecond, String mouseBtn, String clickType, String repeat, String times, String positon, String xLocation, String yLocation) {
        int time = (Integer.parseInt(hours) * 3600 + Integer.parseInt(minutes) * 60 + Integer.parseInt(seconds)) * 1000 + Integer.parseInt(milisecond);
        int click;
        if (Objects.equals(clickType, "Single")) {
            click = 1;
        } else {
            click = 2;
        }

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        if (!running) {
            running = true;
            new Thread(() -> {
                int repeatTime = 0;
                while (running && repeatTime < Integer.parseInt(times)) {
                    if (Objects.equals(positon, "Pick Position")) {
                        robot.mouseMove(Integer.parseInt(xLocation), Integer.parseInt(yLocation));
                    }
                    for (int i = 0; i < click; i++) {
                        if (Objects.equals(mouseBtn, "Left")) {
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        } else {
                            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                        }
                    }
                    try {
                        Thread.sleep(time);
                        if (Objects.equals(repeat, "Repeat")) {
                            repeatTime++;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
