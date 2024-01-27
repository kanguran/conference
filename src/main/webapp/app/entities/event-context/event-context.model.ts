import dayjs from 'dayjs/esm';
import { IRoom } from 'app/entities/room/room.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { IEvent } from 'app/entities/event/event.model';
import { EventContextStatus } from 'app/entities/enumerations/event-context-status.model';

export interface IEventContext {
  id: number;
  description?: string | null;
  eventContextStatus?: EventContextStatus | null;
  start?: dayjs.Dayjs | null;
  end?: dayjs.Dayjs | null;
  eventContextRoom?: Pick<IRoom, 'id' | 'name'> | null;
  contextHost?: Pick<IApplicationUser, 'id' | 'appUser'> | null;
  event?: Pick<IEvent, 'id' | 'name'> | null;
}

export type NewEventContext = Omit<IEventContext, 'id'> & { id: null };
