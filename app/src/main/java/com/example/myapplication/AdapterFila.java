package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterFila extends RecyclerView.Adapter<AdapterFila.ViewHolder> {

    private List<ItemForja> lista;

    public AdapterFila(List<ItemForja> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public AdapterFila.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFila.ViewHolder holder, int position) {
        holder.txt.setText((position + 1) + " ," + lista.get(position));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
