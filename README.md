# EasyPres - Sistema de Controle de Atividades Acadêmicas 🎓

[![Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com/)
[![Retrofit](https://img.shields.io/badge/API-Retrofit-blue.svg)](https://square.github.io/retrofit/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

O **EasyPres** (SCA-Eventos) é uma solução mobile completa para a gestão de eventos acadêmicos. O sistema automatiza todo o ciclo de vida de um evento, desde a criação pelos organizadores até a certificação dos alunos através de um controle de presença inteligente via QR Code.

---

## 📱 Visão Geral

O projeto resolve o problema de filas e burocracia na marcação de presença em palestras e workshops acadêmicos, utilizando geolocalização (implícita pelo scanner) e validação temporal para garantir a integridade das horas complementares.

### ✨ Funcionalidades Principais

#### 👨‍🎓 Módulo do Aluno
*   **Catálogo Inteligente:** Explore eventos disponíveis e realize inscrições com um toque.
*   **Check-in/Check-out via QR Code:** Geração de códigos dinâmicos com timestamp para evitar fraudes.
*   **Barra de Progresso de Presença:** Acompanhamento visual da carga horária cumprida em tempo real.
*   **Selo de Constância (Badge de Ouro):** Reconhecimento automático para alunos com mais de 80% de assiduidade.

#### 👨‍💼 Módulo do Organizador
*   **Painel Administrativo:** CRUD completo de eventos (Título, Local, Data, Carga Horária).
*   **Gestão de Inscritos:** Lista em tempo real de quem se inscreveu, com filtragem automática de duplicatas.

#### 🛡️ Módulo do Porteiro/Scanner
*   **Scanner Integrado:** Validação rápida de entrada e saída via câmera.
*   **Sincronização com Backend:** Registro instantâneo no banco de dados para cálculo de horas.

---

## 🛠️ Tecnologias e Arquitetura

O app foi construído seguindo as melhores práticas de desenvolvimento Android:

*   **Linguagem:** Java (JDK 11)
*   **Comunicação:** [Retrofit 2](https://square.github.io/retrofit/) & OkHttp para consumo de APIs REST.
*   **Parsing:** [Gson](https://github.com/google/gson) para serialização de dados.
*   **QR Code:** [ZXing](https://github.com/zxing/zxing) para geração e leitura.
*   **UI/UX:** Material Design 3 (M3) com componentes dinâmicos e responsivos.
*   **Backend:** API hospedada no **Railway**.

---

## 🚀 Como Executar o Projeto

1.  **Pré-requisitos:**
    *   Android Studio Jellyfish ou superior.
    *   Dispositivo físico ou Emulador com API 24+.

2.  **Clonagem:**
    ```bash
    git clone https://github.com/Pereira-gu/GA-evento-app.git
    ```

3.  **Configuração:**
    *   Abra o projeto no Android Studio.
    *   Aguarde a sincronização do Gradle.
    *   O `ApiClient` já está configurado para apontar para o servidor de produção no Railway.

4.  **Execução:**
    *   Clique em `Run` (ícone do play verde) para instalar no dispositivo.

---

## 📏 Lógica de Validação (O Diferencial)

Diferente de sistemas simples de presença, o EasyPres aplica uma regra de negócio rigorosa:
1.  **Entrada:** Registra o início da participação.
2.  **Saída:** Registra o término e calcula o Delta (Tempo total).
3.  **Cálculo de Mérito:** Se `Tempo de Permanência >= 80% da Carga Horária`, o status muda para **"CONCLUÍDO"** e o aluno recebe o **Selo de Constância 🏆**.

---

## 📁 Estrutura de Pastas

```text
app/src/main/java/com/unicid/sca_eventos_android/
├── activities/ # Controladores das telas (Dashboard, Scanner, etc)
├── adapters/   # Lógica de preenchimento das listas (RecyclerView)
├── api/        # Configuração do Retrofit e Endpoints
├── models/      # Classes de dados (POJOs)
└── utils/      # Helpers e formatadores
```

---

## 📄 Licença

Este projeto está sob a licença MIT. Consulte o arquivo [LICENSE](LICENSE) para mais detalhes.

---
<p align="center">Desenvolvido por <strong>Gustavo Pereira</strong></p>
