package com.flickrly.flickrly.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.flickrly.flickrly.R;
import com.flickrly.flickrly.dagger.GlideApp;
import com.flickrly.flickrly.models.Photo;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {

    private List<Photo> photos;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PublishSubject<Photo> publishSubject = PublishSubject.create();


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
        holder.title.setText(item.getTitle());
        holder.itemView.setOnClickListener(view -> publishSubject.onNext(item));

        GlideApp.with(holder.itemView.getContext())
                .asBitmap()
                .load(Uri.parse(item.getMedia().getM()))
                .centerCrop()
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(holder.imageView);
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.setOnClickListener(null);
    }

    public void addAll(List<Photo> list) {
        this.photos.addAll(list);
        notifyDataSetChanged();
    }

    public PublishSubject<Photo> clickedListener() {
        return publishSubject;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView title;
        public AppCompatImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.image);
        }
    }

}