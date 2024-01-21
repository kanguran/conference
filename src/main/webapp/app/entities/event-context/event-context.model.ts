import dayjs from 'dayjs/esm';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { IEvent } from 'app/entities/event/event.model';
import { EventContextStatus } from 'app/entities/enumerations/event-context-status.model';

export interface IEventContext {
  id: number;
  name?: string | null;
  eventContextStatus?: EventContextStatus | null;
  start?: dayjs.Dayjs | null;
  end?: dayjs.Dayjs | null;
  contextHost?: Pick<IApplicationUser, 'id' | 'appUser'> | null;
  event?: Pick<IEvent, 'id' | 'name'> | null;
}

export type NewEventContext = Omit<IEventContext, 'id'> & { id: null };
