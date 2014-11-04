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

import android.os.SystemClock;

import java.math.BigDecimal;

public class BruttoNetto {
    private double brutto, netto;
    private double zusInstallment, medInsurance, taxInstallment;
    private long startTime;

    private double roundDouble(double big, int scale) {
        return new BigDecimal(String.valueOf(big)).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private void netto4UP() {
        zusInstallment = roundDouble(brutto * 0.1371, 2);
        medInsurance = roundDouble((brutto - zusInstallment) * 0.09, 2);
        taxInstallment = roundDouble(
                roundDouble(brutto - zusInstallment - 111.25, 0) *
                        0.18 - 46.33 - roundDouble((brutto - zusInstallment) * 0.0775, 2), 0);
        netto = brutto - zusInstallment - medInsurance - taxInstallment;
    }

    public String getLiveTime() {
        int sec = (int) ((SystemClock.elapsedRealtime() - startTime) / 1000);
        return String.valueOf(sec / 3600) + ":"
                + String.format("%02d", (sec / 60) % 60) + ":"
                + String.format("%02d", sec % 60);
    }

    public String getLiveNetto() {
        int sec = (int) ((SystemClock.elapsedRealtime() - startTime) / 1000);
        return String.format("%.2f", sec * (netto * 12 / 2000 / 3600));
    }

    public void startTimer(double value) {
        brutto = value;
        netto4UP();
        startTime = SystemClock.elapsedRealtime();
    }
}
