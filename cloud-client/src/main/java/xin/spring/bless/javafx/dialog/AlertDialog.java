package xin.spring.bless.javafx.dialog;

import javafx.scene.control.Alert;

/**
 * 功能描述: 弹窗
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
public class AlertDialog {

    public static Alert info(String title, String contect){
        return createAlert(Alert.AlertType.INFORMATION, title, contect);
    }

    public static Alert confi(String title, String contect){
        return createAlert(Alert.AlertType.CONFIRMATION, title, contect);
    }

    public static Alert error(String title, String contect){
        return createAlert(Alert.AlertType.ERROR, title, contect);
    }

    public static Alert warn(String title, String contect){
        return createAlert(Alert.AlertType.WARNING, title, contect);
    }

    public static Alert none(String title, String contect){
        return createAlert(Alert.AlertType.NONE, title, contect);
    }

    private static Alert createAlert(Alert.AlertType alertType, String title, String contect){
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(contect);
        return alert;
    }

}
