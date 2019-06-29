package com.csjbot.mobileshop.robot_test;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnWakeupListener;
import com.csjbot.coshandler.listener.OnWakeupVersionListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.R;

public class WakeupTestActivity extends AppCompatActivity {

    TextView wakeup_test_angle, wakeup_test_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeup_test);

        wakeup_test_angle = (TextView) findViewById(R.id.wakeup_test_angle);
        wakeup_test_version = (TextView) findViewById(R.id.wakeup_test_version);

        Robot.getInstance().addWakeupListener(angle
                -> runOnUiThread(
                () -> wakeup_test_angle.setText(String.format("wake up angle is = %s", String.valueOf(angle)))));

        Robot.getInstance().setOnWakeupVersionListener((version1, version2)
                -> runOnUiThread(
                () -> wakeup_test_version.setText(String.format("version1 is %s ; version2  %s", version1, version2))));
    }

    public void clickBack(View view) {
        onBackPressed();
    }
}
