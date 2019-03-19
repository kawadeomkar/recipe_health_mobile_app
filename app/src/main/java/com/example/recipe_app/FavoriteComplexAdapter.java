package com.example.recipe_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteComplexAdapter extends ArrayAdapter<Favorite> {

    private List<Favorite> favoriteList;
    private Context context;
    private LayoutInflater mInflater;

    // Constructors
    public FavoriteComplexAdapter(Context context, List<Favorite> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        favoriteList = objects;
    }

    @Override
    public Favorite getItem(int position) {
        return favoriteList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.fragment_favorite_row, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Favorite favorite = getItem(position);

        vh.textViewTitle.setText(favorite.getTitle());
        Picasso.with(context).load(favorite.getImage()).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(vh.imageView);

        vh.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteList.remove(position);
                notifyDataSetChanged();
            }
        });

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final ImageView imageView;
        public final TextView textViewTitle;
        public final Button btnDelete;

        private ViewHolder(RelativeLayout rootView, ImageView imageView, TextView textViewTitle,
                           Button deleteButton) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewTitle = textViewTitle;
            this.btnDelete = deleteButton;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            ImageView recipeImage = (ImageView) rootView.findViewById(R.id.iv_fav_recipe);
            TextView recipeTitle = (TextView) rootView.findViewById(R.id.tv_fav_title);
            Button deleteButton = (Button) rootView.findViewById(R.id.btn_fav_delete);

            return new ViewHolder(rootView, recipeImage, recipeTitle, deleteButton);
        }
    }
}