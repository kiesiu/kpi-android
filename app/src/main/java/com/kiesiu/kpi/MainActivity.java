/*
 * Copyright (c) 2014 Łukasz Kieś <kiesiu@kiesiu.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kiesiu.kpi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private final Handler updateHandler = new Handler();
    private TextView tvTimer, tvMoney;
    private BruttoNetto bnObject = new BruttoNetto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvMoney = (TextView) findViewById(R.id.tvMoney);
        Button btnStartStop = (Button) findViewById(R.id.btnStartStop);
        btnStartStop.setOnClickListener(listenerStartStop);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_rate:
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.kiesiu.kpi")));
                break;
            case R.id.action_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private final View.OnClickListener listenerStartStop = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText etSalary = (EditText) findViewById(R.id.etSalary);
            try {
                bnObject.startTimer(Double.parseDouble(etSalary.getText().toString()));
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), R.string.errNaNSalary,
                        Toast.LENGTH_LONG).show();
                return;
            }
            updateHandler.postDelayed(updateLiveData, 1000);
        }
    };

    private final Runnable updateLiveData = new Runnable() {
        @Override
        public void run() {
            tvTimer.setText(bnObject.getLiveTime());
            tvMoney.setText(bnObject.getLiveNetto());
            updateHandler.postDelayed(updateLiveData, 1000);
        }
    };
}
