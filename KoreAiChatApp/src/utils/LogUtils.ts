export class LogUtils {
  private static isDevelopment = __DEV__;

  public static d(tag: string, message: string, ...args: any[]): void {
    if (LogUtils.isDevelopment) {
      console.log(`[${tag}] ${message}`, ...args);
    }
  }

  public static i(tag: string, message: string, ...args: any[]): void {
    if (LogUtils.isDevelopment) {
      console.info(`[${tag}] ${message}`, ...args);
    }
  }

  public static w(tag: string, message: string, ...args: any[]): void {
    if (LogUtils.isDevelopment) {
      console.warn(`[${tag}] ${message}`, ...args);
    }
  }

  public static e(tag: string, message: string, ...args: any[]): void {
    if (LogUtils.isDevelopment) {
      console.error(`[${tag}] ${message}`, ...args);
    }
  }
}