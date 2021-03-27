package com.example.sheepflock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sheepflock.system.ContentHandler;
import com.example.sheepflock.system.SheepContentManager;

public class SettingsPage extends AppCompatActivity {

    SheepContentManager sheepContentManager;
    Settings settings;
    EditText edtFeedPeriodSetting;
    EditText edtRemindPeriodSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        setTitle(getResources().getString(R.string.settings));

        Button brnSaveSettings=findViewById(R.id.btnSaveSettings);
        edtFeedPeriodSetting=findViewById(R.id.edtFeedPeriodSetting);
        edtRemindPeriodSetting=findViewById(R.id.edtRemindPeriodSetting);

        sheepContentManager=new SheepContentManager(MainActivity.mainActivityContext);
        settings=sheepContentManager.getSetting();
        edtFeedPeriodSetting.setText(settings.feedPeriod_seconds+"");
        edtRemindPeriodSetting.setText(settings.remindPeriod/1000+"");

        final SettingsPage settingsPage=this;
        brnSaveSettings.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                settings.feedPeriod_seconds= Integer.parseInt(edtFeedPeriodSetting.getText()+"");
                settings.remindPeriod= Integer.parseInt(edtRemindPeriodSetting.getText()+"")*1000;
                sheepContentManager.saveSettings(settings);
                Toast.makeText(settingsPage,getResources().getString(R.string.saved_Successfully),Toast.LENGTH_LONG).show();
            }
        });
    }
}
