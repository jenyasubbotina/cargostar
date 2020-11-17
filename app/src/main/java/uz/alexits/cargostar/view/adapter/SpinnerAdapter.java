package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import uz.alexits.cargostar.R;

import java.util.List;

public class SpinnerAdapter<T> extends ArrayAdapter<T> {
    private List<T> objectList;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull T[] objects, final int hintSize) {
        super(context, resource, objects);
    }

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        this.objectList = objects;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
