package com.example.pcgomes.azuredatatestdocuments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mww121 on 27/10/17.
 */

public class DocumentCollectionViewHolder extends RecyclerView.ViewHolder {

    public TextView idTextView;
    public TextView ridTextView;
    public TextView selfTextView;
    public TextView eTagTextView;

    public DocumentCollectionViewHolder(View itemView){
        super(itemView);

        idTextView = itemView.findViewById(R.id.id);
        ridTextView = itemView.findViewById(R.id.rid);
        selfTextView = itemView.findViewById(R.id.self);
        eTagTextView = itemView.findViewById(R.id.eTag);
    }
}
