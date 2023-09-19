import { Deck, FirstName, LastName, Password, Username } from './validation.constants';

export const FIELD_REQUIRED: string = 'This field is required';

export const DEFAULT_ERR: string = 'Something went wrong, please try again later';

export const USERNAME_INVALID_FORMAT: string = `Username should be ${Username.MIN_LENGTH} to ${Username.MAX_LENGTH} symbols (letters, numbers, underscores, periods or dashes).`;

export const EMAIL_INVALID_FORMAT: string = 'Invalid email.';

export const BIRTH_DATE_INVALID: string = 'Date of birth is invalid.';

export const FIRSTNAME_INVALID_FORMAT: string = `Name should be between ${FirstName.MIN_LENGTH} and ${FirstName.MAX_LENGTH} characters long and start with a capital letter.`;

export const LASTNAME_INVALID_FORMAT: string = `Name should be between ${LastName.MIN_LENGTH} and ${LastName.MAX_LENGTH} characters long and start with a capital letter.`;

export const PasswordMessages: any = {
  PASSWORDS_DONT_MATCH: 'Passwords do not match.',
  PASSWORD_INVALID_FORMAT: `Password should be ${Password.MIN_LENGTH} to ${Password.MAX_LENGTH} symbols, capital letter, digit, and one special character.`,
}

export const DECK_INVALID_NAME: string = `Deck should be at between ${Deck.MIN_LENGTH} and ${Deck.MAX_LENGTH} symbols and can contain letters, numbers, - and _`;
