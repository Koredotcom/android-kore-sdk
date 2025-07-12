export class NetworkUtils {
  public static isNetworkAvailable(): boolean {
    // In web environment, we assume network is available if navigator is online
    // In React Native, you would use @react-native-netinfo/netinfo
    if (typeof navigator !== 'undefined' && 'onLine' in navigator) {
      return navigator.onLine;
    }
    
    // For React Native, you would implement this differently
    // For now, we'll assume network is available
    return true;
  }
}