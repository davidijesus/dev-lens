import React from 'react';
import {AbsoluteFill, OffthreadVideo, interpolate, staticFile, useCurrentFrame, useVideoConfig} from 'remotion';

type DeviceFrameProps = {
  opacity?: number;
};

export const DeviceFrame: React.FC<DeviceFrameProps> = ({opacity = 1}) => {
  const frame = useCurrentFrame();
  const {durationInFrames} = useVideoConfig();
  const scale = interpolate(
    frame,
    [0, 240, 760, 1280, 1840, 2460, 3120, durationInFrames],
    [0.92, 1.02, 0.96, 1.04, 0.98, 1.05, 1.01, 0.92],
    {
      extrapolateLeft: 'clamp',
      extrapolateRight: 'clamp',
    },
  );
  const x = interpolate(frame, [0, 760, 1280, 1840, 2460, durationInFrames], [0, -34, 26, -22, 30, 0], {
    extrapolateLeft: 'clamp',
    extrapolateRight: 'clamp',
  });
  const y = interpolate(frame, [0, 720, 1460, 2260, 3000, durationInFrames], [-50, -86, -36, -92, -46, -58], {
    extrapolateLeft: 'clamp',
    extrapolateRight: 'clamp',
  });

  return (
    <AbsoluteFill style={{alignItems: 'center', justifyContent: 'center', opacity}}>
      <div
        style={{
          width: 500,
          height: 1082,
          borderRadius: 68,
          background: 'linear-gradient(145deg, #111827, #020617)',
          padding: 18,
          boxShadow: '0 54px 120px rgba(15, 23, 42, 0.36), inset 0 0 0 2px rgba(255,255,255,0.08)',
          transform: `translate(${x}px, ${y}px) scale(${scale})`,
          position: 'relative',
        }}
      >
        <div
          style={{
            position: 'absolute',
            top: 18,
            left: '50%',
            transform: 'translateX(-50%)',
            width: 132,
            height: 32,
            borderRadius: 999,
            background: '#020617',
            zIndex: 3,
          }}
        />
        <div
          style={{
            width: '100%',
            height: '100%',
            overflow: 'hidden',
            borderRadius: 52,
            background: '#f8fafc',
          }}
        >
          <OffthreadVideo
            src={staticFile('assets/devlens-real-flow.mp4')}
            playbackRate={0.195}
            style={{
              width: '100%',
              height: '100%',
              objectFit: 'cover',
            }}
          />
        </div>
      </div>
    </AbsoluteFill>
  );
};
