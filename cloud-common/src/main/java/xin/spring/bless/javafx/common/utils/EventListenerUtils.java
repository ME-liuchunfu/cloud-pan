package xin.spring.bless.javafx.common.utils;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 * 事件监听工具类
 */
public class EventListenerUtils {

    /**
     * 背景
     * @param node
     * @param colorEnter
     * @param colorOver
     * @param callBack
     */
    public static void handerEventBack(Region node, Color colorEnter, Color colorOver, CallBack callBack){
        if(node != null){
            try {
                if (colorOver != null){
                    // 鼠标移出
                    node.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
                        node.setBackground(new Background(new BackgroundFill(colorOver, null, null)));
                    });
                }
                if(colorEnter != null) {
                    // 鼠标移入
                    node.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
                        node.setBackground(new Background(new BackgroundFill(colorEnter, null, null)));
                    });
                }
                if (callBack != null) {
                    node.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                        callBack.hander(node, event);
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 背景
     * @param node
     * @param colorEnter
     * @param colorOver
     */
    public static void handerEventBack(Region node, Color colorEnter, Color colorOver){
        handerEventBack(node, colorEnter, colorOver, null);
    }

    /**
     * 圆角背景
     * @param node
     * @param radii
     * @param colorEnter
     * @param colorOver
     * @param callBack
     */
    public static void handerEventBackFill(Region node, CornerRadii radii, Color colorEnter, Color colorOver, CallBack callBack){
        if(node != null){
            try {
                if (colorOver != null){
                    // 鼠标移出
                    node.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
                        node.setBackground(new Background(new BackgroundFill(colorOver, radii, null)));
                    });
                }
                if(colorEnter != null) {
                    // 鼠标移入
                    node.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
                        node.setBackground(new Background(new BackgroundFill(colorEnter, radii, null)));
                    });
                }
                if (callBack != null) {
                    node.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                        callBack.hander(node, event);
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 圆角背景
     * @param node
     * @param radii
     * @param colorEnter
     * @param colorOver
     */
    public static void handerEventBackFill(Region node, CornerRadii radii, Color colorEnter, Color colorOver){
        handerEventBackFill(node, radii, colorEnter, colorOver, null);
    }

    /**
     * 设置颜色
     * @param node
     * @param colorEnter
     * @param colorOver
     */
    public static void handerEventColor(Labeled node, Color colorEnter, Color colorOver, CallBack callBack) {
        try{
            if(node != null){
                if(colorEnter != null){
                    node.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
                        node.setTextFill(colorEnter);
                    });
                }
                if(colorOver != null){
                    node.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
                        node.setTextFill(colorOver);
                    });
                }
                if(callBack != null){
                    node.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                        callBack.hander(node, event);
                    });
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设置颜色
     * @param node
     * @param colorEnter
     */
    public static void handerEventColor(Labeled node, Color colorEnter, Color colorOver) {
        handerEventColor(node, colorEnter, colorOver);
    }

    /**
     * 点击监听回调
     */
    public static interface CallBack{
        public void hander(Node node, MouseEvent event);
    }

}
