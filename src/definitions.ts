export interface PresentationAPIPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
