import React from 'react';

export const Logo: React.FC<{size?: number}> = ({size = 86}) => {
  return (
    <div
      style={{
        width: size,
        height: size,
        borderRadius: 18,
        background: '#1d4ed8',
        display: 'grid',
        placeItems: 'center',
        boxShadow: '0 24px 60px rgba(29, 78, 216, 0.28)',
      }}
    >
      <div
        style={{
          width: size * 0.62,
          height: size * 0.38,
          border: `${Math.max(4, size * 0.07)}px solid white`,
          borderRadius: '50%',
          position: 'relative',
        }}
      >
        <div
          style={{
            position: 'absolute',
            width: size * 0.2,
            height: size * 0.2,
            left: '50%',
            top: '50%',
            transform: 'translate(-50%, -50%)',
            borderRadius: '50%',
            background: '#10b981',
          }}
        />
      </div>
    </div>
  );
};
