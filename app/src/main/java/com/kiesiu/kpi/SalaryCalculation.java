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

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SalaryCalculation {
    private float brutto, netto, zusInstallment, medInsurance, taxInstallment;
    private long startTime = 0;
    private List<String[]> detailedResults = new ArrayList<>();
    private final static float MIN_GROSS = 1750.0f;
    private final static long WORKING_HOURS = 2016;

    private float roundValue(float big, int scale) {
        return new BigDecimal(String.valueOf(big)).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    private void netto4UP(float value) {
        brutto = value;
        zusInstallment = roundValue(brutto * 0.1371f, 2);
        medInsurance = roundValue((brutto - zusInstallment) * 0.09f, 2);
        taxInstallment = roundValue(
                roundValue(brutto - zusInstallment - 111.25f, 0) *
                        0.18f - 46.33f - roundValue((brutto - zusInstallment) * 0.0775f, 2), 0);
        netto = brutto - zusInstallment - medInsurance - taxInstallment;
    }

    private void brutto4UP(float value) {
        float myNetto;
        float myBrutto = roundValue(value * 1.7151f, 2);
        netto4UP(myBrutto);
        do {
            myNetto = roundValue(netto - value, 2);
            if (myNetto > 100) {
                myBrutto -= 100;
            } else if (myNetto > 50) {
                myBrutto -= 50;
            } else if (myNetto > 10) {
                myBrutto -= 10;
            } else if (myNetto > 5) {
                myBrutto -= 5;
            } else if (myNetto > 1) {
                myBrutto -= 1;
            } else if (myNetto > 0.5) {
                myBrutto -= 0.5f;
            } else if (myNetto > 0.11f) {
                myBrutto -= 0.11f;
            } else {
                myBrutto -= 0.01f;
            }
            netto4UP(myBrutto);
        } while (myNetto > 0.01f);
    }

    public String getLiveTime() {
        int sec = (int) ((SystemClock.elapsedRealtime() - startTime) / 1000);
        return String.valueOf(sec / 3600) + ":"
                + String.format("%02d", (sec / 60) % 60) + ":"
                + String.format("%02d", sec % 60);
    }

    public String getLiveNetto() {
        int sec = (int) ((SystemClock.elapsedRealtime() - startTime) / 1000);
        return String.format("%.2f", sec * (netto * 12 / WORKING_HOURS / 3600));
    }

    public void startTimer(float value, boolean gross) throws Exception {
        if (gross) {
            netto4UP(value);
        } else {
            brutto4UP(value);
        }
        if (brutto < MIN_GROSS) {
            throw new Exception(String.format("%.2f", MIN_GROSS));
        }
        startTime = SystemClock.elapsedRealtime();
    }

    public boolean restartTimer(Bundle b) {
        startTime = b.getLong("start");
        if (startTime > 0) {
            netto4UP(b.getFloat("brutto"));
            return true;
        }
        return false;
    }

    public void stopTimer() {
        startTime = 0;
    }

    public Bundle getTimer() {
        Bundle b = new Bundle();
        b.putFloat("brutto", brutto);
        b.putLong("start", startTime);
        return b;
    }

    public List<String[]> getList(Context ctx) {
        detailedResults.add(new String[]{String.format("%.2f", brutto), ctx.getString(R.string.detailsGross)});
        detailedResults.add(new String[]{String.format("%.2f", netto), ctx.getString(R.string.detailsNet)});
        detailedResults.add(new String[]{String.format("%.2f", zusInstallment), ctx.getString(R.string.detailsSocial)});
        detailedResults.add(new String[]{String.format("%.2f", medInsurance), ctx.getString(R.string.detailsMed)});
        detailedResults.add(new String[]{String.format("%.2f", taxInstallment), ctx.getString(R.string.detailsTax)});
        return detailedResults;
    }
}
