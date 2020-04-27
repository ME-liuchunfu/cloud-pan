package xin.spring.bless.javafx.common.utils;

import javafx.scene.paint.Color;

/**
 * 颜色工具
 */
public class ColorUtils {

    public static Color translucent(Color c, double newAlpha) {
        double r = c.getRed();
        double g = c.getGreen();
        double b = c.getBlue();
        if (newAlpha < 0) {
            newAlpha = 0;
        }
        if (newAlpha > 255) {
            newAlpha = 255;
        }
        return new Color(r, g, b, newAlpha);
    }
}
