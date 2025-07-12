import {
  JwtTokenResponse,
  BotAuthorizationResponse,
  RtmUrlResponse
} from '../types/BotConfigModel';

export class JwtRepository {
  
  public async getJwtToken(
    jwtServerUrl: string,
    clientId: string,
    clientSecret: string,
    identity: string
  ): Promise<JwtTokenResponse | null> {
    try {
      const response = await fetch(jwtServerUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          clientId,
          clientSecret,
          identity
        })
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Error getting JWT token:', error);
      throw error;
    }
  }

  public async getJwtGrant(request: Record<string, any>): Promise<BotAuthorizationResponse | null> {
    try {
      // This would be the actual Kore.ai API endpoint for JWT grant
      const response = await fetch('https://platform.kore.ai/api/1.1/oAuth/token', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(request)
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Error getting JWT grant:', error);
      throw error;
    }
  }

  public async getRtmUrl(
    accessToken: string,
    request: Record<string, any>
  ): Promise<RtmUrlResponse | null> {
    try {
      // This would be the actual Kore.ai API endpoint for RTM URL
      const response = await fetch('https://platform.kore.ai/api/1.1/rtm/start', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${accessToken}`
        },
        body: JSON.stringify(request)
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Error getting RTM URL:', error);
      throw error;
    }
  }
}