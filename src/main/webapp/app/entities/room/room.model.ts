export interface IRoom {
  id: number;
  name?: string | null;
  maxSeats?: number | null;
}

export type NewRoom = Omit<IRoom, 'id'> & { id: null };
