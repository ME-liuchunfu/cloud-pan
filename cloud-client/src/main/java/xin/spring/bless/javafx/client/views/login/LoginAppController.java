package xin.spring.bless.javafx.client.views.login;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import xin.spring.bless.javafx.client.session.ApplicationSession;
import xin.spring.bless.javafx.client.views.index.IndexApplication;
import xin.spring.bless.javafx.common.pojo.User;
import xin.spring.bless.javafx.common.utils.StringUtils;
import xin.spring.bless.javafx.core.AbsInitializable;
import xin.spring.bless.javafx.db.repositories.LogOperRepository;
import xin.spring.bless.javafx.db.repositories.UserRepository;
import xin.spring.bless.javafx.dialog.AlertDialog;

import java.util.Optional;

/**
 * 功能描述:
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
public class LoginAppController extends AbsInitializable {

    @FXML private Button register;

    @FXML private Button submit;

    @FXML private TextField loginname;

    @FXML private TextField password;

    @Autowired
    private LogOperRepository logOperRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void beforeDatas() {

    }

    @Override
    protected void initListener() {
        //new Alert()
        submit.setOnAction(event -> {
            String loginN = StringUtils.trim(loginname.getText());
            String pwd = StringUtils.trim(password.getText());
            Alert alert = AlertDialog.warn("账号或密码为空","请认真输入密码账号" );
            if((null != loginN && !"".equals(loginN)) && (null != pwd && !"".equals(pwd))){
                User user = userRepository.findByLoginName(loginN);
                if(user != null && user.isEnables()){
                    if(user.getPassword().equals(pwd)){
                        ApplicationSession.newInstance().putUser(user);
                        // 关闭当前，并跳转主页面
                        Stage stage = (Stage)register.getScene().getWindow();
                        stage.close();
                        try {
                            new IndexApplication().start(new Stage());
                        } catch (Exception e) {
                            e.printStackTrace();
                            alert = AlertDialog.error("启动失败了", "程序发生错误，退出");
                            alert.showAndWait();
                        }
                    }else{
                        alert = AlertDialog.error("登录提示", "账号密码不匹配或该账号被禁用。");
                        alert.showAndWait();
                    }
                }else{
                    alert = AlertDialog.error("登录提示", "账号密码不匹配或该账号被禁用。");
                    alert.showAndWait();
                }
            }else{
                alert.showAndWait();
            }
        });

        register.setOnAction(event -> {
            String loginN = StringUtils.trim(loginname.getText());
            String pwd = StringUtils.trim(password.getText());
            Alert alert = AlertDialog.warn("账号或密码为空", "请认真输入密码账号");
            if((null != loginN && !"".equals(loginN)) && (null != pwd && !"".equals(pwd))){
                User user = userRepository.findByLoginName(loginN);
                if(null != user){
                    alert = AlertDialog.error("注册失败提示", "当前账号已被使用，请更换。");
                    alert.showAndWait();
                }else{
                    user = new User();
                    user.setLoginName(loginN);
                    user.setPassword(pwd);
                    user.setEnables(true);
                    User save = userRepository.save(user);
                    if(null != user){
                        alert = AlertDialog.confi("注册成功提示","注册成功是否登录。");
                        Optional<ButtonType> buttonType = alert.showAndWait();
                        if(buttonType.isPresent() && buttonType.get() == ButtonType.OK){
                            ApplicationSession.newInstance().putUser(save);
                            // 关闭当前，并跳转主页面
                            Stage stage = (Stage)register.getScene().getWindow();
                            stage.close();
                            try {
                                new IndexApplication().start(new Stage());
                            } catch (Exception e) {
                                alert = AlertDialog.error("启动失败了", "程序发生错误，退出");
                                alert.showAndWait();
                                e.printStackTrace();
                            }
                        }
                    }else{
                        alert = AlertDialog.error("注册失败提示", "注册出错，稍后请重试");
                        alert.showAndWait();
                    }
                }
            }else{
                alert.showAndWait();
            }

        });
    }

}
