import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { EventStatus } from 'app/entities/enumerations/event-status.model';

export interface IEvent {
  id: number;
  name?: string | null;
  eventStatus?: EventStatus | null;
  mainHost?: Pick<IApplicationUser, 'id' | 'appUser'> | null;
}

export type NewEvent = Omit<IEvent, 'id'> & { id: null };
