package com.flickrly.flickrly.adapters;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.flickrly.flickrly.R;
import com.flickrly.flickrly.models.Photo;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {

    private List<Photo> photos;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PublishSubject<Photo> publishSubject = PublishSubject.create();
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE");


    public PhotoAdapter(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Photo item = photos.get(position);
//        LocalDate date = item.getDate();
//        String formatted = date.format(dtf);
        holder.title.setText(item.getTitle());
    }

    public PublishSubject<Photo> getPublishSubject() {
        return publishSubject;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AppCompatButton title;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
        }
    }

}