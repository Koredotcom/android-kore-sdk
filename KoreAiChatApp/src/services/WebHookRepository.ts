import { WebHookResponse } from '../types/BotConfigModel';

export class WebHookRepository {
  
  public async sendMessage(
    botId: string,
    jwtToken: string,
    payload: Record<string, any>
  ): Promise<WebHookResponse | null> {
    try {
      const response = await fetch(`https://platform.kore.ai/api/1.1/webhook/${botId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${jwtToken}`
        },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Error sending webhook message:', error);
      throw error;
    }
  }

  public async getWebhookMeta(
    token: string,
    botId: string
  ): Promise<any> {
    try {
      const response = await fetch(`https://platform.kore.ai/api/1.1/webhook/${botId}/meta`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Error getting webhook meta:', error);
      throw error;
    }
  }
}