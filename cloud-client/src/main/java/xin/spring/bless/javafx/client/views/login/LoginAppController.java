package xin.spring.bless.javafx.client.views.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import xin.spring.bless.javafx.common.dialog.AlertDialog;
import xin.spring.bless.javafx.common.pojo.LogOper;
import xin.spring.bless.javafx.core.AbsInitializable;
import xin.spring.bless.javafx.db.repositories.LogOperRepository;

import java.util.UUID;

/**
 * 功能描述:
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
public class LoginAppController extends AbsInitializable {

    @FXML private Button cancel;

    @FXML private Button submit;

    @Autowired
    private LogOperRepository logOperRepository;

    @Override
    protected void beforeDatas() {

    }

    @Override
    protected void initListener() {
        submit.setOnAction(event -> {
            LogOper logOper = new LogOper();
            logOper.setContenxt(UUID.randomUUID().toString().replace("-",""));
            logOperRepository.save(logOper);
            AlertDialog.displayDelay("新增", "保存成功", 2000);
        });

    }

}
