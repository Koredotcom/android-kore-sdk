import { BotConfigModel } from '../types/BotConfigModel';

export const getBotConfig = (): BotConfigModel => {
  return {
    botName: 'Kore AI Demo Bot',
    botId: 'st-f59fda8f-e42c-5c6a-bc55-3395c109862a', // Replace with your bot ID
    clientId: 'cs-8fa81912-0b49-544a-848e-1ce84e7d2df6', // Replace with your client ID
    clientSecret: 'DnY4BIXBR0Ytmvdb3yI3Lvfri/iDc/UOsxY2tChs7SY=', // Replace with your client secret
    botUrl: 'https://platform.kore.ai/', // Replace with your bot URL
    identity: '123456789078643234567', // Replace with unique identity
    isWebHook: false, // Set to true if using webhook
    jwtServerUrl: 'https://mk2r2rmj21.execute-api.us-east-1.amazonaws.com/dev/users/sts', // Replace with your JWT server URL
    jwtToken: '', // Can be empty if using JWT server
    enablePanel: false,
  };
};

// You can also create different configurations for different environments
export const getBotConfigForEnvironment = (env: 'development' | 'staging' | 'production'): BotConfigModel => {
  switch (env) {
    case 'development':
      return {
        ...getBotConfig(),
        botName: 'Kore AI Dev Bot',
        // Override with dev-specific values
      };
    case 'staging':
      return {
        ...getBotConfig(),
        botName: 'Kore AI Staging Bot',
        // Override with staging-specific values
      };
    case 'production':
      return {
        ...getBotConfig(),
        botName: 'Kore AI Production Bot',
        // Override with production-specific values
      };
    default:
      return getBotConfig();
  }
};