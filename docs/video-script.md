# Roteiro de Video - DevLens (3 a 5 minutos)

## 1. Abertura e Problema

"O DevLens foi criado para resolver um problema comum em times de desenvolvimento: muitas atividades tecnicas geram alto valor real para produto e equipe, mas aparecem pouco para a gestao."

Exemplos: refatoracoes, revisoes de codigo, suporte a incidentes, otimizacao de performance e reducao de divida tecnica.

## 2. Valor Efetivo vs. Valor Percebido

"Valor efetivo e o impacto real da atividade: reducao de risco, melhoria de performance, confiabilidade, qualidade ou entrega. Valor percebido e o quanto essa atividade tende a ser vista, entendida e valorizada por gestores e stakeholders."

O desalinhamento acontece quando uma tarefa tem impacto alto, mas baixa visibilidade, ou quando uma tarefa parece muito visivel, mas gera pouco impacto real.

## 3. Demonstracao do Cadastro

Abrir o app, mostrar onboarding e ir para "Nova atividade".

Preencher exemplo:

- Titulo: "Otimizar query critica do dashboard".
- Descricao: "Reduzi latencia em producao para usuarios do dashboard executivo".
- Tempo: 210 minutos.
- Tipo: Performance.
- Tags: performance, dashboard, producao.
- Visibilidade percebida: 35.

Salvar e analisar.

## 4. Classificacao por IA Local

Mostrar a tela de detalhe.

"Ao salvar, o app executa um classificador local, sem API externa. Ele calcula score de valor efetivo, score de valor percebido, diferenca, categoria, confianca, fatores e recomendacoes."

Explicar que a IA considera tipo da atividade, palavras-chave, tempo gasto, criticidade, impacto, visibilidade, tags e sinais de comunicacao.

## 5. Divergencia Encontrada

Mostrar a categoria "Alto valor invisivel".

"Neste caso, a atividade tem alto valor efetivo porque envolve performance, producao e usuarios. Mas a percepcao fica menor se nao houver sinais de demo, relatorio, OKR, release ou comunicacao para gestao."

Mostrar os fatores e a explicacao textual.

## 6. Dashboard

Abrir a tela inicial.

Mostrar:

- Total de atividades.
- Media de valor efetivo.
- Media de valor percebido.
- Quantidade de atividades invisiveis.
- Atividades criticas.

"O dashboard transforma registros individuais em sinais agregados para o time."

## 7. Relatorio Gerencial

Abrir "Relatorio".

Mostrar resumo semanal, atividades de alto impacto, atividades invisiveis e sugestoes.

"O resumo final pode ser copiado e enviado ao gestor, conectando trabalho tecnico a impacto de produto e risco reduzido."

## 8. Justificativa da IA Local

"A atividade valoriza IA local. Por isso o DevLens usa um motor embarcado e modular chamado LocalAiEngine. Ele hoje usa regras ponderadas e NLP simples, mas a interface ActivityClassifier permite substituir por ONNX Runtime Mobile ou TensorFlow Lite sem mudar as telas."

Destacar privacidade: os dados do time ficam no aparelho.

## 9. Encerramento e Barema

"O DevLens atende ao barema porque tem app mobile funcional, arquitetura MVVM, persistencia local com Room, preferencias com DataStore, WorkManager, IA local explicavel, testes unitarios, diagramas Mermaid, dados de exemplo e relatorio gerencial."

"A principal contribuicao e tornar visivel o valor tecnico que normalmente fica escondido."
