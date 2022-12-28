package id.ac.scanaltinex.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import id.ac.scanaltinex.Models.ListWoDailyModel;
import id.ac.scanaltinex.R;

public class ListWoDailyAdapter extends BaseAdapter {
    LayoutInflater inflter;
    ArrayList<ListWoDailyModel> ListWoDailyModelArrayList = new ArrayList<>();

    public void ListWoDailyAdapter(ArrayList<ListWoDailyModel> list, LayoutInflater inflater){
        this.inflter = inflater;
        this.ListWoDailyModelArrayList = list;
    }

    @Override
    public int getCount() {
        return ListWoDailyModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return ListWoDailyModelArrayList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_list, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView opera = (TextView) view.findViewById(R.id.user);

        name.setText(ListWoDailyModelArrayList.get(i).getName());
        opera.setText(ListWoDailyModelArrayList.get(i).getEmployeeId());
        return view;
    }
}
