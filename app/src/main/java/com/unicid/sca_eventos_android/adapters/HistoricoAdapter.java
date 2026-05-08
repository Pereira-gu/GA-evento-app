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
        holder.tvNome.setText(inscricao.getEvento().getNome());
        holder.tvData.setText(inscricao.getEvento().getData());
        
        if (inscricao.isPresencaConfirmada()) {
            holder.tvStatus.setText("Presente");
            holder.tvStatus.setTextColor(Color.parseColor("#388E3C"));
        } else {
            holder.tvStatus.setText("Ausente / Pendente");
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