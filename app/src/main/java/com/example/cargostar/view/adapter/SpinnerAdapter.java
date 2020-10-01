package com.example.cargostar.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cargostar.R;

import java.util.List;

public class SpinnerAdapter<T> extends ArrayAdapter<T> {
    private final int hintSize;
    private List<T> objectList;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull T[] objects, final int hintSize) {
        super(context, resource, objects);
        this.hintSize = hintSize;
    }

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        this.objectList = objects;
        this.hintSize = 0;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view = super.getDropDownView(position, convertView, parent);
        final TextView selectedItem = (TextView) view;
        selectedItem.setTextColor(getContext().getColor(R.color.colorBlack));
        return view;
    }

    @Override
    public int getCount() {
        return objectList == null ? 0: super.getCount() - hintSize;
    }
}
