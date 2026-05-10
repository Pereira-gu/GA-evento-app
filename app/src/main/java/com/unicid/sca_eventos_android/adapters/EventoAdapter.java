package com.unicid.sca_eventos_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.unicid.sca_eventos_android.R;
import com.unicid.sca_eventos_android.models.Evento;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Adaptador para exibição de uma lista de Eventos no RecyclerView.
 */
public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private List<Evento> eventos;
    private OnEventoClickListener listener;
    private OnEventoLongClickListener longListener;

    public interface OnEventoClickListener {
        void onEventoClick(Evento evento);
    }

    public interface OnEventoLongClickListener {
        void onEventoLongClick(Evento evento);
    }

    public EventoAdapter(List<Evento> eventos, OnEventoClickListener listener) {
        this.eventos = eventos;
        this.listener = listener;
    }

    public EventoAdapter(List<Evento> eventos, OnEventoClickListener listener, OnEventoLongClickListener longListener) {
        this.eventos = eventos;
        this.listener = listener;
        this.longListener = longListener;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        Evento evento = eventos.get(position);
        holder.tvNome.setText(evento.getTitulo());
        holder.tvLocal.setText(evento.getLocal());
        
        // Formatar Data
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            // Caso venha com nanossegundos do backend, vamos tentar capturar a parte principal
            String dataString = evento.getDataInicio();
            if (dataString.contains(".")) {
                dataString = dataString.split("\\.")[0];
            }
            Date date = inputFormat.parse(dataString);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", new Locale("pt", "BR"));
            holder.tvData.setText(outputFormat.format(date));
        } catch (Exception e) {
            holder.tvData.setText(evento.getDataInicio());
        }

        // Formatar Carga Horária (Convertendo minutos para H:mm)
        int totalMinutos = evento.getCargaHoraria();
        int horas = totalMinutos / 60;
        int minutos = totalMinutos % 60;
        
        String cargaFormatada;
        if (horas > 0 && minutos > 0) {
            cargaFormatada = horas + "h " + minutos + "m";
        } else if (horas > 0) {
            cargaFormatada = horas + "h";
        } else {
            cargaFormatada = minutos + "m";
        }
        holder.tvCarga.setText(cargaFormatada);

        holder.itemView.setOnClickListener(v -> listener.onEventoClick(evento));
        
        if (longListener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                longListener.onEventoLongClick(evento);
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    static class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvData, tvLocal, tvCarga;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeEvento);
            tvData = itemView.findViewById(R.id.tvDataEvento);
            tvLocal = itemView.findViewById(R.id.tvLocalEvento);
            tvCarga = itemView.findViewById(R.id.tvCargaHoraria);
        }
    }
}