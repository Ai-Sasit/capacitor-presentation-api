import { WebPlugin } from '@capacitor/core';

import type { PresentationAPIPlugin } from './definitions';

export class PresentationAPIWeb
  extends WebPlugin
  implements PresentationAPIPlugin
{
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
  async openLink(options: {
    url: string;
  }): Promise<{ success?: any; error?: any }> {
    const presentationRequest = new (window as any).PresentationRequest([
      options.url,
    ]);
    try {
      const start = await presentationRequest.start();
      this.notifyListeners('onFailLoadUrl', start);
      return {
        success: start,
      };
    } catch (error) {
      this.notifyListeners('onSuccessLoadUrl', error);
      return {
        error: error,
      };
    }
  }

  async openRawHtml(options: {
    htmlStr: string;
  }): Promise<{ success?: any; error?: any }> {
    const presentationRequest = new (window as any).PresentationRequest([
      options.htmlStr,
    ]);
    try {
      const start = await presentationRequest.start();
      this.notifyListeners('onFailLoadUrl', start);
      return {
        success: start,
      };
    } catch (error) {
      this.notifyListeners('onSuccessLoadUrl', error);
      return {
        error: error,
      };
    }
  }

  async getDisplays(): Promise<{ displays: number }> {
    const presentationRequest = new (window as any).PresentationRequest(['']);

    try {
      await presentationRequest.getAvailability();
      return {
        displays: 1,
      };
    } catch (error) {
      return {
        displays: 0,
      };
    }
  }
}
