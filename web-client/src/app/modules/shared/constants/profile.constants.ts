import { environment } from '../../../../environments/environment';

export enum ProfileNavigation {
  ACCOUNT = 'ACCOUNT',
  DECKS = 'DECKS',
  CARDS = 'CARDS'
}

export const DEFAULT_AVATAR_PATH : string = `${environment.STATIC_FILE_HOST}/images/default_avatar.png`;
