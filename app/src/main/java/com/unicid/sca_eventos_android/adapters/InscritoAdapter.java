package com.unicid.sca_eventos_android.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.JsonObject;
import com.unicid.sca_eventos_android.R;
import java.util.List;

public class InscritoAdapter extends RecyclerView.Adapter<InscritoAdapter.InscritoViewHolder> {

    private List<JsonObject> inscritos;

    public InscritoAdapter(List<JsonObject> inscritos) {
        this.inscritos = inscritos;
    }

    @NonNull
    @Override
    public InscritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inscrito, parent, false);
        return new InscritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InscritoViewHolder holder, int position) {
        JsonObject inscrito = inscritos.get(position);
        
        String nome = inscrito.has("nomeUsuario") ? inscrito.get("nomeUsuario").getAsString() : "N/A";
        String email = inscrito.has("emailUsuario") ? inscrito.get("emailUsuario").getAsString() : "N/A";
        String status = inscrito.has("statusPresenca") ? inscrito.get("statusPresenca").getAsString() : "PENDENTE";

        holder.tvNome.setText(nome);
        holder.tvEmail.setText(email);
        holder.tvStatus.setText(status);

        if ("ATIVO".equals(status) || "CONCLUIDO".equals(status)) {
            holder.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde
        } else {
            holder.tvStatus.setBackgroundColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return inscritos.size();
    }

    static class InscritoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvEmail, tvStatus;

        public InscritoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeInscrito);
            tvEmail = itemView.findViewById(R.id.tvEmailInscrito);
            tvStatus = itemView.findViewById(R.id.tvStatusInscrito);
        }
    }
}