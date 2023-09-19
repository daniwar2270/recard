export const Username: any = {
  MIN_LENGTH: 3,
  MAX_LENGTH: 50,
  PATTERN: /^[a-zA-Z0-9_.-]{3,50}$/
};

export const Password: any = {
  MIN_LENGTH: 8,
  MAX_LENGTH: 32,
  PATTERN: /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,32}$/
};

export const FirstName: any = {
  MIN_LENGTH: 3,
  MAX_LENGTH: 30,
  PATTERN: /^[A-Z][a-z]{2,29}$/
};

export const LastName: any = {
  MIN_LENGTH: 3,
  MAX_LENGTH: 30,
  PATTERN: /^[A-Z][a-z]{2,29}$/
};

export const BIRTH_DATE_MIN: string = '1900-01-01';

export const BIRTH_DATE_MAX: string = new Date().toISOString().split('T')[0];

export const ValidationTypes: any = {
  REQUIRED: 'required',
  VALIDATION: 'validation',
  MIN_LENGTH: 'minlength',
  MAX_LENGTH: 'maxlength',
  EMAIL: 'email',
  PATTERN: 'pattern'
};

export const Card: any = {
  MIN_ATTACK: 0,
  MAX_ATTACK: 100,
  MIN_DEFENCE: 0,
  MAX_DEFENCE: 100
}

export const Deck: any = {
  MIN_LENGTH: 3,
  MAX_LENGTH: 30,
  PATTERN: /^[A-Za-z0-9\-_ ]{3,30}$/
}
