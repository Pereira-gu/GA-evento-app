package com.unicid.sca_eventos_android.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.unicid.sca_eventos_android.R;
import com.unicid.sca_eventos_android.models.Inscricao;
import java.util.List;

/**
 * Adaptador para exibição do histórico de inscrições/presenças.
 * Atualizado para bater com os campos do novo endpoint de histórico.
 */
public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder> {

    private List<Inscricao> inscricoes;

    public HistoricoAdapter(List<Inscricao> inscricoes) {
        this.inscricoes = inscricoes;
    }

    @NonNull
    @Override
    public HistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historico, parent, false);
        return new HistoricoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricoViewHolder holder, int position) {
        Inscricao inscricao = inscricoes.get(position);
        holder.tvNome.setText(inscricao.getTituloEvento());
        holder.tvData.setText(inscricao.getDataEvento());
        
        long minutosPresente = inscricao.getMinutosPresenca();
        int cargaTotal = inscricao.getCargaHorariaEvento();
        
        if (inscricao.isConcluido()) {
            holder.tvStatus.setText("✨ Presença Confirmada (100%)");
            holder.tvStatus.setTextColor(Color.parseColor("#388E3C")); // Verde escuro
        } else if ("ATIVO".equals(inscricao.getStatusPresenca()) && minutosPresente > 0 && cargaTotal > 0) {
            double percentual = (minutosPresente * 100.0) / cargaTotal;
            String progresso = String.format(java.util.Locale.getDefault(), "Em andamento: %.1f%%", percentual);
            holder.tvStatus.setText(progresso);
            holder.tvStatus.setTextColor(Color.BLUE);
        } else if ("PENDENTE".equals(inscricao.getStatusPresenca())) {
            holder.tvStatus.setText("Aguardando entrada");
            holder.tvStatus.setTextColor(Color.GRAY);
        } else {
            holder.tvStatus.setText(inscricao.getStatusPresenca());
            holder.tvStatus.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return inscricoes.size();
    }

    static class HistoricoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvData, tvStatus;

        public HistoricoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvHistNomeEvento);
            tvData = itemView.findViewById(R.id.tvHistDataEvento);
            tvStatus = itemView.findViewById(R.id.tvStatusPresenca);
        }
    }
}