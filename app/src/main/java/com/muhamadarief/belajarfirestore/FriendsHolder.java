package com.muhamadarief.belajarfirestore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Muhamad Arief on 08/11/2017.
 */

public class FriendsHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.company)
    TextView company;


    public FriendsHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
