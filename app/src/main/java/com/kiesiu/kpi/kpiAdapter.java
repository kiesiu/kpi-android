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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class kpiAdapter extends ArrayAdapter {
    public kpiAdapter(Context ctx, List<String[]> data) {
        super(ctx, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String[] item = (String[]) getItem(position);
        Holder h;
        if (convertView == null) {
            h = new Holder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
            h.data = (TextView) convertView.findViewById(R.id.listRowData);
            h.desc = (TextView) convertView.findViewById(R.id.listRowDesc);
            convertView.setTag(h);
        } else {
            h = (Holder) convertView.getTag();
        }
        h.data.setText(item[0]);
        h.desc.setText(item[1]);
        return convertView;
    }

    private static class Holder {
        TextView data;
        TextView desc;
    }
}
