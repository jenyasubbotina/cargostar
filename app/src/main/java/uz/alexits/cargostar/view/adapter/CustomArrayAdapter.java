package uz.alexits.cargostar.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import uz.alexits.cargostar.R;

import java.util.Collection;
import java.util.List;

public class CustomArrayAdapter<T> extends ArrayAdapter<T> {
    private List<T> objectList;

    public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
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

    @Override
    public void addAll(@NonNull Collection<? extends T> collection) {
        super.addAll(collection);
    }

    @Override
    public void clear() {
        super.clear();
    }
}
