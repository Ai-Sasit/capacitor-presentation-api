import { PluginListenerHandle } from '@capacitor/core';
export interface PresentationAPIPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  openLink(options: {
    url: string;
  }): Promise<{ success?: any; error?: any; url?: any }>;
  openRawHtml(options: {
    htmlStr: string;
  }): Promise<{ success?: any; error?: any }>;
  addListener(
    eventName: 'onSuccessLoadUrl',
    listenerFunc: (data: any) => void,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
  addListener(
    eventName: 'onFailLoadUrl',
    listenerFunc: (data: any) => void,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;

  getDisplays(): Promise<{ displays: number }>;
}
