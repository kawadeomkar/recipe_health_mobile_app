package com.example.recipe_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeComplexAdapter extends ArrayAdapter<RecipeTemp> {

    private List<RecipeTemp> recipeList;
    private Context context;
    private LayoutInflater mInflater;

    // Constructors
    public RecipeComplexAdapter(Context context, List<RecipeTemp> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        recipeList = objects;
    }

    @Override
    public RecipeTemp getItem(int position) {
        return recipeList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.fragment_recipe_row, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        RecipeTemp recipe = getItem(position);

        String info = "Calories: " + recipe.getCalories() + " Carbs: " + recipe.getCarbs() +
        " Protein: " + recipe.getProtein() + " Fat: " + recipe.getFat();

        vh.textViewTitle.setText(recipe.getTitle());
        vh.textViewInformation.setText(info);
        Picasso.with(context).load(recipe.getImage()).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(vh.imageView);

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final ImageView imageView;
        public final TextView textViewTitle;
        public final TextView textViewInformation;

        private ViewHolder(RelativeLayout rootView, ImageView imageView, TextView textViewName,
                           TextView textViewEmail) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewTitle = textViewName;
            this.textViewInformation = textViewEmail;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.iv_recipe_results);
            TextView textViewName = (TextView) rootView.findViewById(R.id.tv_recipeFrag_title);
            TextView textViewEmail = (TextView) rootView.findViewById(R.id.tv_recipeFrag_info);
            return new ViewHolder(rootView, imageView, textViewName, textViewEmail);
        }
    }
}