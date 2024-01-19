import dayjs from 'dayjs/esm';
import { IEvent } from 'app/entities/event/event.model';
import { EventContextStatus } from 'app/entities/enumerations/event-context-status.model';

export interface IEventContext {
  id: number;
  name?: string | null;
  eventContextStatus?: EventContextStatus | null;
  start?: dayjs.Dayjs | null;
  end?: dayjs.Dayjs | null;
  event?: Pick<IEvent, 'id'> | null;
}

export type NewEventContext = Omit<IEventContext, 'id'> & { id: null };
