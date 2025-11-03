import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { EventType } from 'app/entities/enumerations/event-type.model';
import { EventStatus } from 'app/entities/enumerations/event-status.model';

export interface IEvent {
  id: number;
  name?: string | null;
  eventType?: keyof typeof EventType | null;
  eventStatus?: keyof typeof EventStatus | null;
  mainHost?: Pick<IApplicationUser, 'id'> | null;
}

export type NewEvent = Omit<IEvent, 'id'> & { id: null };
