/*
 * Copyright (c) 2015 Łukasz Kieś <kiesiu@kiesiu.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kiesiu.kpi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private final Handler updateHandler = new Handler();
    private final SalaryCalculation salary = new SalaryCalculation();
    private boolean Gross = true;
    @Bind(R.id.tvTimer) TextView tvTimer;
    @Bind(R.id.tvYourMoney) TextView tvMoney;
    @Bind(R.id.btnStartStop) Button btnStartStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBundle("salary", salary.getTimer());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (salary.restartTimer(savedInstanceState.getBundle("salary"))) {
            updateDataList();
            updateHandler.postDelayed(updateLiveData, 1000);
            btnStartStop.setText(R.string.strStop);
            btnStartStop.setTag("1");
        }
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
                startActivity(new Intent(this.getBaseContext(), AboutActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateDataList() {
        ((ListView) findViewById(R.id.listDetails)).setAdapter(
                new kpiAdapter(getBaseContext(), salary.getList(getBaseContext())));
    }

    @OnClick(R.id.btnStartStop)
    public void onStartStopClick() {
        if (btnStartStop.getTag() == "1") {
            salary.stopTimer();
            updateHandler.removeCallbacks(updateLiveData);
            btnStartStop.setText(R.string.strStart);
            btnStartStop.setTag(null);
        }
        else {
            float userValue;
            try {
                userValue = Float.parseFloat(((EditText) findViewById(R.id.etSalary)).getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), R.string.errNaNSalary,
                        Toast.LENGTH_LONG).show();
                return;
            }
            try {
                salary.startTimer(userValue, Gross);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        String.format(getString(R.string.errMinGross), e.getMessage()),
                        Toast.LENGTH_LONG).show();
                return;
            }
            updateDataList();
            updateHandler.postDelayed(updateLiveData, 1000);
            btnStartStop.setText(R.string.strStop);
            btnStartStop.setTag("1");
        }
    }

    @OnCheckedChanged(R.id.rbGross)
    public void onGrossChecked(boolean checked) {
        Gross = checked;
    }

    private final Runnable updateLiveData = new Runnable() {
        @Override
        public void run() {
            tvTimer.setText(salary.getLiveTime());
            tvMoney.setText(salary.getLiveNetto());
            updateHandler.postDelayed(updateLiveData, 1000);
        }
    };
}
