# SCA - Sistema de Controle de Atividades Acadêmicas

## Descrição do Projeto
O SCA-Eventos é uma solução robusta para a gestão de eventos acadêmicos, desenvolvida para plataformas Android. O sistema visa automatizar o ciclo completo de um evento, desde a criação pelo organizador até a certificação do aluno por meio de controle de presença inteligente via QR Code.

O diferencial técnico do projeto reside na implementação de uma lógica de gamificação e validação de presença em tempo real, onde o sistema calcula automaticamente a carga horária efetiva do participante para atribuição de selos de mérito.

## Funcionalidades Principais

### Módulo do Aluno
*   **Catálogo de Eventos:** Visualização e inscrição em atividades disponíveis via integração com API REST.
*   **Check-in Dinâmico:** Geração de QR Code com timestamp criptográfico para validação de entrada e saída.
*   **Histórico de Presença:** Acompanhamento em tempo real do progresso de participação (percentual de presença baseado na carga horária total).
*   **Selo Badge de Ouro:** Implementação de lógica de reconhecimento automático para alunos que atingem mais de 80% de presença em eventos específicos.

### Módulo do Organizador
*   **Gestão de Eventos (CRUD):** Interface completa para criação, edição e exclusão de eventos com controle de carga horária.
*   **Monitoramento de Inscritos:** Visualização detalhada da lista de alunos confirmados por atividade.

### Módulo do Porteiro/Scanner
*   **Validação de Acesso:** Integração com a câmera para leitura e validação de QR Codes, comunicando-se diretamente com o backend para registro de entrada e saída.

## Tecnologias Utilizadas
*   **Linguagem:** Java para Android.
*   **Arquitetura:** Baseada em padrões de projeto para separação de responsabilidades (Model-View-Adapter).
*   **Comunicação:** Retrofit 2 para consumo de APIs RESTful.
*   **Processamento de Dados:** Gson para serialização e desserialização de objetos JSON.
*   **Interface:** Material Design Components para uma experiência de usuário padronizada e intuitiva.
*   **Utilidades:** ZXing para geração e processamento de QR Codes.

## Lógica de Negócio e Diferenciais
O sistema implementa uma regra de negócio rigorosa para a validação de horas complementares:
1.  O sistema registra o exato momento da entrada e da saída do usuário.
2.  É realizado o cálculo da diferença temporal (Saída - Entrada).
3.  Caso o tempo de permanência seja igual ou superior a 80% da carga horária prevista para o evento, o status da inscrição é atualizado para \"Concluído\" e o usuário torna-se elegível ao \"Badge de Ouro\".

## Configuração do Ambiente
1.  Certifique-se de possuir o Android Studio instalado (versão Flamingo ou superior).
2.  O projeto utiliza o Gradle como gerenciador de dependências.
3.  A API de backend está hospedada em ambiente de produção na plataforma Railway.

## Estrutura de Diretórios
*   `activities/`: Controladores de interface e fluxo de navegação.
*   `adapters/`: Gerenciamento e vinculação de dados em listas (RecyclerViews).
*   `api/`: Configurações de cliente HTTP e definição de endpoints.
*   `models/`: Classes de POJO que representam o domínio do sistema.

---
Este projeto foi desenvolvido com foco em escalabilidade e manutenibilidade, seguindo as melhores práticas de desenvolvimento Android vigentes.
