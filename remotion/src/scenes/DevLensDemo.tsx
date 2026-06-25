import React from 'react';
import {AbsoluteFill, Sequence, interpolate, useCurrentFrame, useVideoConfig} from 'remotion';
import {DeviceFrame} from '../components/DeviceFrame';
import {Logo} from '../components/Logo';
import {SpeechBubble} from '../components/SpeechBubble';

const fps = 30;
const at = (seconds: number) => Math.round(seconds * fps);

const bubbles = [
  {
    from: 12,
    to: 24,
    title: 'Valor efetivo vs. valor percebido',
    body: 'O DevLens separa impacto real de visibilidade: uma tarefa pode reduzir risco, melhorar performance ou aumentar confiabilidade mesmo quando quase não aparece para a gestão.',
  },
  {
    from: 24,
    to: 38,
    title: 'Cadastro da atividade',
    body: 'No fluxo real, o desenvolvedor registra título, descrição, tempo, tipo, tags, data e a visibilidade percebida antes de salvar.',
  },
  {
    from: 38,
    to: 52,
    title: 'Exemplo técnico',
    body: 'A demo usa a otimização de uma query crítica do dashboard executivo, com impacto direto em usuários e no ambiente de produção.',
  },
  {
    from: 52,
    to: 66,
    title: 'IA local explicável',
    body: 'Ao salvar, o classificador local calcula valor efetivo, valor percebido, diferença, categoria, confiança, fatores e recomendações sem enviar dados para APIs externas.',
  },
  {
    from: 66,
    to: 80,
    title: 'Alto valor invisível',
    body: 'O caso aparece como alto valor invisível: há impacto forte em performance e produção, mas baixa percepção quando faltam sinais de demo, relatório, OKR ou comunicação.',
  },
  {
    from: 78,
    to: 90,
    title: 'Dashboard e histórico',
    body: 'O painel consolida atividades, médias de valor efetivo e percebido, itens invisíveis e registros críticos para revisão do time.',
  },
  {
    from: 90,
    to: 102,
    title: 'Relatório gerencial',
    body: 'O resumo semanal transforma registros técnicos em linguagem pronta para líderes e stakeholders, conectando esforço a impacto de produto.',
  },
  {
    from: 102,
    to: 108,
    title: 'Arquitetura e privacidade',
    body: 'O LocalAiEngine usa regras ponderadas e NLP simples hoje, mas a interface permite evoluir para ONNX Runtime Mobile ou TensorFlow Lite mantendo os dados no aparelho.',
  },
];

const proofPoints = [
  'App mobile funcional',
  'MVVM e persistência local',
  'IA local explicável',
  'Testes e dados de exemplo',
  'Relatório gerencial',
];

const summaryCards = [
  {
    label: 'Entrada',
    value: 'atividade técnica',
  },
  {
    label: 'Análise',
    value: 'valor efetivo x percebido',
  },
  {
    label: 'Saída',
    value: 'resumo para gestão',
  },
];

export const DevLensDemo: React.FC = () => {
  const frame = useCurrentFrame();
  const {durationInFrames} = useVideoConfig();
  const progress = interpolate(frame, [0, durationInFrames], [0, 100], {
    extrapolateLeft: 'clamp',
    extrapolateRight: 'clamp',
  });
  const introOpacity = interpolate(frame, [0, at(1), at(5.5), at(7)], [0, 1, 1, 0], {
    extrapolateLeft: 'clamp',
    extrapolateRight: 'clamp',
  });
  const problemOpacity = interpolate(frame, [at(6.5), at(8), at(14), at(16)], [0, 1, 1, 0], {
    extrapolateLeft: 'clamp',
    extrapolateRight: 'clamp',
  });
  const deviceOpacity = interpolate(frame, [at(14), at(16), at(103), at(107)], [0, 1, 1, 0], {
    extrapolateLeft: 'clamp',
    extrapolateRight: 'clamp',
  });
  const summaryOpacity = interpolate(frame, [at(108), at(110), at(114), at(116)], [0, 1, 1, 0], {
    extrapolateLeft: 'clamp',
    extrapolateRight: 'clamp',
  });
  const outroOpacity = interpolate(frame, [at(116), at(118), at(120)], [0, 1, 1], {
    extrapolateLeft: 'clamp',
    extrapolateRight: 'clamp',
  });

  return (
    <AbsoluteFill
      style={{
        background: 'linear-gradient(180deg, #f8fafc 0%, #e8edf5 100%)',
        fontFamily: 'Inter, ui-sans-serif, system-ui, Segoe UI, sans-serif',
      }}
    >
      <DeviceFrame opacity={deviceOpacity} />

      <div
        style={{
          position: 'absolute',
          top: 48,
          left: 72,
          right: 72,
          height: 8,
          borderRadius: 8,
          background: 'rgba(148, 163, 184, 0.22)',
          overflow: 'hidden',
          opacity: interpolate(frame, [at(8), at(10), at(114), at(116)], [0, 1, 1, 0], {
            extrapolateLeft: 'clamp',
            extrapolateRight: 'clamp',
          }),
        }}
      >
        <div
          style={{
            width: `${progress}%`,
            height: '100%',
            borderRadius: 8,
            background: 'linear-gradient(90deg, #1d4ed8, #10b981)',
          }}
        />
      </div>

      <AbsoluteFill style={{opacity: introOpacity, justifyContent: 'center', alignItems: 'center'}}>
        <Logo size={120} />
        <div style={{fontSize: 74, fontWeight: 900, color: '#0f172a', marginTop: 34}}>DevLens</div>
        <div style={{fontSize: 30, color: '#475569', marginTop: 14}}>Tornando visível o valor real da engenharia</div>
      </AbsoluteFill>

      <AbsoluteFill style={{opacity: problemOpacity, justifyContent: 'flex-start', padding: '190px 80px 0'}}>
        <div style={{fontSize: 54, lineHeight: 1.05, fontWeight: 900, color: '#0f172a'}}>
          Nem todo trabalho importante aparece para a gestão.
        </div>
        <div style={{fontSize: 29, lineHeight: 1.28, color: '#475569', marginTop: 22, maxWidth: 840}}>
          Refatorações, suporte técnico e otimizações geram valor real, mas frequentemente ficam invisíveis.
        </div>
      </AbsoluteFill>

      {bubbles.map((bubble) => (
        <Sequence key={bubble.title} from={at(bubble.from)} durationInFrames={at(bubble.to - bubble.from)}>
          <SpeechBubble title={bubble.title} body={bubble.body} />
        </Sequence>
      ))}

      <AbsoluteFill
        style={{
          opacity: summaryOpacity,
          justifyContent: 'flex-start',
          padding: '132px 76px 0',
          pointerEvents: 'none',
        }}
      >
        <div
          style={{
            background: 'rgba(255,255,255,0.9)',
            border: '1px solid rgba(148, 163, 184, 0.32)',
            boxShadow: '0 26px 70px rgba(15, 23, 42, 0.14)',
            borderRadius: 28,
            padding: '28px 34px',
          }}
        >
          <div style={{fontSize: 32, fontWeight: 900, color: '#0f172a'}}>Entrega alinhada ao barema</div>
          <div style={{display: 'grid', gridTemplateColumns: '1fr', gap: 12, marginTop: 20}}>
            {proofPoints.map((item) => (
              <div key={item} style={{display: 'flex', alignItems: 'center', gap: 14, fontSize: 24, color: '#334155'}}>
                <div style={{width: 12, height: 12, borderRadius: 6, background: '#10b981'}} />
                {item}
              </div>
            ))}
          </div>
        </div>

        <div style={{display: 'grid', gridTemplateColumns: '1fr', gap: 14, marginTop: 24}}>
          {summaryCards.map((card) => (
            <div
              key={card.label}
              style={{
                background: 'rgba(15,23,42,0.88)',
                color: 'white',
                borderRadius: 22,
                padding: '20px 28px',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                gap: 24,
              }}
            >
              <div style={{fontSize: 22, color: '#cbd5e1', fontWeight: 700}}>{card.label}</div>
              <div style={{fontSize: 25, color: '#ffffff', fontWeight: 900, textAlign: 'right'}}>{card.value}</div>
            </div>
          ))}
        </div>
      </AbsoluteFill>

      <AbsoluteFill
        style={{
          opacity: outroOpacity,
          background: '#f8fafc',
          justifyContent: 'center',
          alignItems: 'center',
          textAlign: 'center',
          padding: 80,
        }}
      >
        <Logo size={104} />
        <div style={{fontSize: 58, fontWeight: 900, color: '#0f172a', marginTop: 26}}>DevLens</div>
        <div style={{fontSize: 30, color: '#334155', marginTop: 16, maxWidth: 780}}>
          A principal contribuição é tornar visível o valor técnico que normalmente fica escondido.
        </div>
      </AbsoluteFill>
    </AbsoluteFill>
  );
};
