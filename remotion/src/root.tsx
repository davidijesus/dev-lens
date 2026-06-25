import React from 'react';
import {Composition} from 'remotion';
import {DevLensDemo} from './scenes/DevLensDemo';

export const Root: React.FC = () => {
  return (
    <Composition
      id="DevLensDemo"
      component={DevLensDemo}
      durationInFrames={3600}
      fps={30}
      width={1080}
      height={1920}
    />
  );
};
