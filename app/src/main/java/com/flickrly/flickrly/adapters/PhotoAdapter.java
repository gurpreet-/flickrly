package com.flickrly.flickrly.adapters;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<DaysAdapter.MyViewHolder> {

    private List<Day> days;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PublishSubject<Day> publishSubject = PublishSubject.create();
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE");


    public PhotoAdapter(List<Day> days) {
        this.days = days;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_layout, parent, false);
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Day item = days.get(position);
        item.load();
        LocalDate date = item.getDate();

        String formatted = date.format(dtf);
        holder.title.setText(formatted);
    }

    public PublishSubject<Day> getPublishSubject() {
        return publishSubject;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AppCompatButton title;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.btn);
        }
    }

}