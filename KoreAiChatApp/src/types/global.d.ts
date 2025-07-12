declare global {
  var __DEV__: boolean;
  var navigator: {
    userAgent: string;
    onLine: boolean;
  };
}

export {};