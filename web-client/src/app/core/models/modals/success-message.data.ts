import { MessageType } from './success-message.types';

export interface SuccessMessageData {
  title: string;
  content: string;
  show?: boolean;
  type?: MessageType;
  progressWidth?: number;
}
