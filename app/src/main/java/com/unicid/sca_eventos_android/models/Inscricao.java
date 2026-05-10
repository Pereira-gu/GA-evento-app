package com.unicid.sca_eventos_android.models;

/**
 * Representa uma inscrição/presença de um aluno em um evento.
 * Sincronizado com o novo endpoint de histórico do backend.
 */
public class Inscricao {
    private String eventoId;
    private String tituloEvento;
    private String dataEvento;
    private String statusPresenca;
    private String entrada;
    private String saida;
    private int cargaHorariaEvento; // Adicionado para cálculo de badge

    public String getEventoId() { return eventoId; }
    public String getTituloEvento() { return tituloEvento; }
    public String getDataEvento() { return dataEvento; }
    public String getStatusPresenca() { return statusPresenca; }
    public String getEntrada() { return entrada; }
    public String getSaida() { return saida; }
    public int getCargaHorariaEvento() { return cargaHorariaEvento; }

    // Cálculo de minutos de presença
    public long getMinutosPresenca() {
        if (entrada == null || saida == null) return 0;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault());
            java.util.Date dataEntrada = sdf.parse(entrada.split("\\.")[0]);
            java.util.Date dataSaida = sdf.parse(saida.split("\\.")[0]);
            long diff = dataSaida.getTime() - dataEntrada.getTime();
            return diff / (1000 * 60);
        } catch (Exception e) {
            return 0;
        }
    }

    // Helper para verificar se está concluído
    public boolean isConcluido() {
        return "CONCLUIDO".equals(statusPresenca);
    }
}