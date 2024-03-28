import { WebPlugin } from '@capacitor/core';

import type { PresentationAPIPlugin } from './definitions';

export class PresentationAPIWeb extends WebPlugin implements PresentationAPIPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
