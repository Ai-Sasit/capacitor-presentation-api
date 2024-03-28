import { registerPlugin } from '@capacitor/core';

import type { PresentationAPIPlugin } from './definitions';

const PresentationAPI = registerPlugin<PresentationAPIPlugin>('PresentationAPI', {
  web: () => import('./web').then(m => new m.PresentationAPIWeb()),
});

export * from './definitions';
export { PresentationAPI };
