import { IEventContext } from 'app/entities/event-context/event-context.model';

export interface IRoom {
  id: number;
  name?: string | null;
  maxSeats?: number | null;
  roomEventContext?: Pick<IEventContext, 'id'> | null;
}

export type NewRoom = Omit<IRoom, 'id'> & { id: null };
