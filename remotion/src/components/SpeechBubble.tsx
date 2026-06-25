import React from 'react';
import {interpolate, spring, useCurrentFrame, useVideoConfig} from 'remotion';

type SpeechBubbleProps = {
  title: string;
  body: string;
};

export const SpeechBubble: React.FC<SpeechBubbleProps> = ({title, body}) => {
  const frame = useCurrentFrame();
  const {fps} = useVideoConfig();
  const entrance = spring({frame, fps, config: {damping: 18, stiffness: 120}});
  const opacity = interpolate(frame, [0, 18], [0, 1], {extrapolateRight: 'clamp'});

  return (
    <div
      style={{
        position: 'absolute',
        left: 86,
        right: 86,
        bottom: 86,
        borderRadius: 34,
        padding: '32px 38px',
        background: 'rgba(255,255,255,0.82)',
        backdropFilter: 'blur(20px)',
        boxShadow: '0 26px 70px rgba(15, 23, 42, 0.18)',
        border: '1px solid rgba(148, 163, 184, 0.32)',
        opacity,
        transform: `translateY(${(1 - entrance) * 42}px)`,
      }}
    >
      <div
        style={{
          position: 'absolute',
          top: -18,
          left: 82,
          width: 38,
          height: 38,
          transform: 'rotate(45deg)',
          background: 'rgba(255,255,255,0.82)',
          borderLeft: '1px solid rgba(148, 163, 184, 0.24)',
          borderTop: '1px solid rgba(148, 163, 184, 0.24)',
        }}
      />
      <div style={{fontSize: 30, fontWeight: 800, color: '#0f172a', marginBottom: 10}}>{title}</div>
      <div style={{fontSize: 25, lineHeight: 1.32, color: '#334155'}}>{body}</div>
    </div>
  );
};
