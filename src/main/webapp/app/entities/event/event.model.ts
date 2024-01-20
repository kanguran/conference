import { IUser } from 'app/entities/user/user.model';
import { EventStatus } from 'app/entities/enumerations/event-status.model';

export interface IEvent {
  id: number;
  name?: string | null;
  eventStatus?: EventStatus | null;
  mainHost?: Pick<IUser, 'id'> | null;
}

export type NewEvent = Omit<IEvent, 'id'> & { id: null };
