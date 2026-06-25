# DevLens

DevLens e um aplicativo Android em Kotlin + Jetpack Compose que ajuda times de desenvolvimento a enxergar a diferenca entre valor efetivo e valor percebido das atividades tecnicas do dia a dia.

## Demo em Video

<p align="center">
  <video src="demo/final/demo.mp4" controls width="360">
    Abra o arquivo em demo/final/demo.mp4.
  </video>
</p>

Arquivo da demo renderizada: [`demo/final/demo.mp4`](demo/final/demo.mp4).

## Problema Abordado

Em times de software, muito trabalho importante nao aparece claramente para a gestao: refatoracoes, revisoes de codigo, suporte informal, investigacao de bugs, reducao de divida tecnica, melhoria de performance e documentacao. Essas tarefas podem reduzir risco, melhorar confiabilidade e acelerar entregas futuras, mas tendem a ter baixa visibilidade quando nao sao comunicadas em linguagem de negocio.

## Publico-Alvo

- Desenvolvedores de software.
- Tech leads.
- Product managers.
- Engineering managers.

## Proposta de Solucao

O DevLens permite registrar atividades diarias, analisar cada registro com IA local, comparar dois scores e gerar recomendacoes praticas:

- Valor efetivo: impacto real para produto, empresa, usuario ou equipe.
- Valor percebido: quanto a atividade tende a ser visivel ou valorizada pela gestao.

## Como a IA Resolve o Problema

Ao salvar uma atividade, uma camada local de IA calcula:

- Score de valor efetivo, de 0 a 100.
- Score de valor percebido, de 0 a 100.
- Diferenca entre os scores.
- Categoria de desalinhamento.
- Confianca da classificacao.
- Explicacao textual.
- Fatores rastreaveis.
- Recomendacoes para melhorar alinhamento.

O app nao usa OpenAI, Gemini, Claude ou qualquer API externa. A inferencia e executada no aparelho por um motor modular chamado `LocalAiEngine`.

## Arquitetura do Sistema

O projeto segue MVVM com separacao por camadas:

- `presentation`: telas Compose, componentes, navegacao e ViewModel.
- `domain`: modelos, repositorios e casos de uso.
- `data`: Room, entidades, DAO, mappers e implementacao do repositorio.
- `ai`: classificador local, gerador de explicacao e motor de recomendacao.
- `worker`: WorkManager para manutencao periodica dos insights.
- `docs`: roteiro de apresentacao e documentacao complementar.

```mermaid
flowchart TD
    UI[Compose Screens] --> VM[DevLensViewModel]
    VM --> UC[Use Cases]
    UC --> Repo[ActivityRepository]
    Repo --> Room[(Room Database)]
    UC --> AI[LocalAiEngine]
    AI --> Explain[ExplanationGenerator]
    AI --> Rec[RecommendationEngine]
    Worker[InsightWorker] --> Repo
```

## Tecnologias Utilizadas

- Kotlin.
- Jetpack Compose.
- Material Design 3.
- MVVM.
- Room Database.
- DataStore Preferences.
- WorkManager.
- Coroutines e Flow.
- JUnit para testes unitarios.
- Gradle Kotlin DSL.

## Fluxo de Uso

1. Usuario passa pelo onboarding.
2. Usuario registra uma atividade com titulo, descricao, tempo, tipo, tags e visibilidade percebida.
3. O `LocalAiEngine` classifica a atividade localmente.
4. O app persiste atividade, scores, explicacao e recomendacoes no Room.
5. Dashboard, lista, detalhe, insights e relatorio gerencial usam os dados locais.

```mermaid
flowchart LR
    A[Onboarding] --> B[Dashboard]
    B --> C[Cadastrar atividade]
    C --> D[Classificacao local]
    D --> E[Persistencia Room]
    E --> F[Detalhe explicavel]
    E --> G[Insights]
    E --> H[Relatorio gerencial]
```

## Detalhamento do Modelo de IA

A implementacao atual usa um modelo local hibrido baseado em NLP simples, regras ponderadas e fuzzy scoring. A arquitetura foi desenhada para permitir substituicao futura por ONNX Runtime Mobile ou TensorFlow Lite sem alterar UI ou banco.

Classes principais:

- `ActivityClassifier`: contrato de classificacao.
- `LocalAiEngine`: motor local substituivel.
- `ExplanationGenerator`: transforma fatores em justificativa natural.
- `RecommendationEngine`: gera proximas acoes.
- `AiAnalysis`: saida estruturada da classificacao.

## Dados de Entrada

Cada atividade recebe:

- Titulo.
- Descricao.
- Tempo gasto.
- Tipo da atividade.
- Tags.
- Visibilidade percebida pelo desenvolvedor.
- Data da atividade.

## Saidas do Modelo

- `effectiveScore`.
- `perceivedScore`.
- `gap`.
- `category`.
- `confidence`.
- `explanation`.
- `effectiveFactors`.
- `perceivedFactors`.
- `recommendations`.
- `analyzedAt`.

## Estrategia de Classificacao

O classificador combina:

- Peso base por tipo de atividade.
- Palavras-chave de impacto: cliente, usuario, receita, SLA, producao, incidente, OKR.
- Palavras-chave tecnicas: refatoracao, legado, teste, arquitetura, performance, query, cache, seguranca, divida tecnica.
- Palavras-chave de urgencia: critico, urgente, p0, p1, rollback, hotfix.
- Palavras-chave de visibilidade: demo, review, release, relatorio, stakeholder, roadmap.
- Visibilidade percebida informada pelo desenvolvedor.
- Tempo gasto.
- Presenca ou ausencia de tags.

Categorias:

- Alinhado: diferenca pequena entre os scores.
- Alto valor invisivel: valor efetivo alto e percepcao baixa.
- Baixo valor superestimado: percepcao alta e valor efetivo menor.
- Atencao necessaria: diferenca relevante que nao se enquadra nos extremos.

```mermaid
flowchart TD
    Input[ActivityInput] --> Normalize[Normalizar texto e tags]
    Normalize --> Effective[Calcular sinais de valor efetivo]
    Normalize --> Perceived[Calcular sinais de valor percebido]
    Effective --> Gap[Calcular gap]
    Perceived --> Gap
    Gap --> Category[Definir categoria]
    Category --> Confidence[Estimar confianca]
    Confidence --> Explanation[Gerar explicacao]
    Explanation --> Recommendations[Gerar recomendacoes]
    Recommendations --> Output[AiAnalysis]
```

## Explicabilidade da IA

Cada resultado mostra:

- Fatores que aumentaram valor efetivo.
- Fatores que aumentaram ou reduziram valor percebido.
- Justificativa em linguagem natural.
- Sugestoes de acao.

Exemplo: uma otimizacao de query critica recebe alto valor efetivo por performance, producao e usuarios afetados. Se nao houver sinais de demo, release, gestor, OKR ou comunicacao, o valor percebido fica menor e a categoria tende a ser "Alto valor invisivel".

## Justificativa da IA Local

A atividade prioriza IA embarcada/local. Por isso, o DevLens usa um classificador deterministico local, sem rede e sem custo operacional. A escolha tambem favorece privacidade: descricoes de trabalho, incidentes e contexto interno do time ficam no aparelho.

Substituicao futura:

1. Criar uma implementacao `OnnxActivityClassifier` ou `TfliteActivityClassifier`.
2. Manter a assinatura de `ActivityClassifier`.
3. Retornar o mesmo `AiAnalysis`.
4. Trocar a instancia no `DevLensContainer`.

## Como Executar o Projeto

Requisitos:

- Android Studio.
- JDK 17.
- Android SDK com compile SDK 35.
- Gradle compativel com Android Gradle Plugin 8.5.2.
- Emulador Android ou dispositivo fisico com Android 8.0, API 26, ou superior.

Passos:

1. Abra a pasta do projeto no Android Studio.
2. Aguarde o sync do Gradle.
3. Selecione a configuracao `app`.
4. Execute o app em um emulador ou dispositivo Android.

## Como Verificar a Entrega Mobile

A entrega principal e exclusivamente mobile. A pasta `app/` contem a implementacao Android em Kotlin + Jetpack Compose.

Para verificar o aplicativo manualmente:

1. Abra o projeto no Android Studio pela raiz do repositorio.
2. Confirme que o modulo `:app` sincronizou sem erros.
3. Inicie um emulador Android ou conecte um dispositivo fisico.
4. Execute a configuracao `app`.
5. No aplicativo, avance pelo onboarding e confira o dashboard inicial.
6. Cadastre uma nova atividade tecnica informando titulo, descricao, tempo, tipo, tags e visibilidade percebida.
7. Salve a atividade e verifique se a analise local retorna valor efetivo, valor percebido, categoria, confianca, explicacao, fatores e recomendacoes.
8. Navegue pelas telas de dashboard, historico, detalhe, insights e relatorio gerencial para confirmar que os dados persistidos aparecem corretamente.
9. Feche e abra o aplicativo novamente para conferir a persistencia local via Room e DataStore.

Para verificar os testes unitarios:

- Pelo Android Studio, abra a aba Gradle e execute `:app:testDebugUnitTest`.
- Se o wrapper Gradle tiver sido gerado pela IDE, rode `gradlew.bat testDebugUnitTest` no Windows ou `./gradlew testDebugUnitTest` no macOS/Linux.

O fluxo esperado e que o app funcione sem chamadas externas de IA: a classificacao acontece localmente pelo `LocalAiEngine`, e os dados continuam no aparelho.

## Estrutura de Pastas

```text
app/src/main/java/com/devlens
  ai/
  data/
    dao/
    entities/
    local/
    mappers/
    repository/
  domain/
    models/
    repositories/
    usecases/
  presentation/
    components/
    navigation/
    screens/
    theme/
    viewmodels/
  worker/
docs/
```

## Diagramas

### Diagrama de Entidades

```mermaid
erDiagram
    ActivityEntity {
        long id PK
        string title
        string description
        int minutesSpent
        string type
        string tags
        int selfPerceivedVisibility
        long activityDate
        long createdAt
        int effectiveScore
        int perceivedScore
        int gap
        string category
        float confidence
        string explanation
        string effectiveFactors
        string perceivedFactors
        string recommendations
        long analyzedAt
    }
```

### Fluxo de Dados

```mermaid
sequenceDiagram
    participant User as Usuario
    participant UI as Compose
    participant VM as ViewModel
    participant UC as SaveActivityUseCase
    participant AI as LocalAiEngine
    participant DB as Room
    User->>UI: Preenche atividade
    UI->>VM: saveActivity()
    VM->>UC: ActivityInput
    UC->>AI: classify(input)
    AI-->>UC: AiAnalysis
    UC->>DB: DeveloperActivity + AiAnalysis
    DB-->>VM: Flow atualizado
    VM-->>UI: Dashboard, detalhe e insights
```

## Roteiro do Video de Apresentacao

O video final esta renderizado em [`demo/final/demo.mp4`](demo/final/demo.mp4), e o roteiro completo esta em [`docs/video-script.md`](docs/video-script.md).

## Limitacoes e Melhorias Futuras

- O modelo atual e heuristico e interpretavel, nao estatistico.
- A classificacao pode ser calibrada com dados reais do time.
- Uma versao futura pode usar ONNX/TFLite com embeddings locais.
- O relatorio pode exportar PDF.
- O app pode ter filtros por squad, sprint e periodo.

## Relacao com o Barema da Atividade

- Clareza da solucao: telas e README explicam problema, publico e proposta.
- IA pertinente: classificador compara valor efetivo e valor percebido.
- IA local: nenhuma API externa e usada.
- Arquitetura realista: MVVM, Room, DataStore, WorkManager e Compose.
- Fluxo de dados claro: diagramas Mermaid e separacao por camadas.
- Explicabilidade: fatores, justificativa e confianca aparecem no detalhe.
- Rastreabilidade: `AiAnalysis` persiste scores, fatores e recomendacoes.
- Relatorio gerencial: tela propria com resumo copiavel.
- Dados de exemplo: repository popula cinco atividades iniciais.
- Testes unitarios: cobrem calculos, desalinhamento, explicacao, recomendacao e mapper.
- Video: demo renderizada em `demo/final/demo.mp4` e roteiro em `docs/video-script.md`.
