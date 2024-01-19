import { EventStatus } from 'app/entities/enumerations/event-status.model';

export interface IEvent {
  id: number;
  name?: string | null;
  eventStatus?: EventStatus | null;
}

export type NewEvent = Omit<IEvent, 'id'> & { id: null };
