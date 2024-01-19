import { IRoom, NewRoom } from './room.model';

export const sampleWithRequiredData: IRoom = {
  id: 302,
  name: 'repurpose RSS',
  maxSeats: 3705,
};

export const sampleWithPartialData: IRoom = {
  id: 64594,
  name: 'Fresh',
  maxSeats: 96390,
};

export const sampleWithFullData: IRoom = {
  id: 68831,
  name: 'holistic holistic multi-byte',
  maxSeats: 25559,
};

export const sampleWithNewData: NewRoom = {
  name: 'Triple-buffered Barbados Metical',
  maxSeats: 1525,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
