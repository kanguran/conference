import { IEvent, NewEvent } from './event.model';

export const sampleWithRequiredData: IEvent = {
  id: 18174,
  name: 'scent yum',
  eventType: 'CONFERENCE',
  eventStatus: 'PUBLISHED',
};

export const sampleWithPartialData: IEvent = {
  id: 23864,
  name: 'acidly',
  eventType: 'CONFERENCE',
  eventStatus: 'PUBLISHED',
};

export const sampleWithFullData: IEvent = {
  id: 14744,
  name: 'know yahoo',
  eventType: 'CONFERENCE',
  eventStatus: 'PUBLISHED',
};

export const sampleWithNewData: NewEvent = {
  name: 'throughout solder aw',
  eventType: 'CONFERENCE',
  eventStatus: 'CANCELLED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
