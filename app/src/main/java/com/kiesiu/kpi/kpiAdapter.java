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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class kpiAdapter extends ArrayAdapter {
    public kpiAdapter(Context ctx, List<String[]> data) {
        super(ctx, 0, data);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        String[] item = (String[]) getItem(position);
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.data.setText(item[0]);
        holder.desc.setText(item[1]);
        return view;
    }

    public static class Holder {
        @Bind(R.id.listRowData) TextView data;
        @Bind(R.id.listRowDesc) TextView desc;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
