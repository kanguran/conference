import { EventStatus } from 'app/entities/enumerations/event-status.model';

import { IEvent, NewEvent } from './event.model';

export const sampleWithRequiredData: IEvent = {
  id: 63022,
  name: 'Cotton',
};

export const sampleWithPartialData: IEvent = {
  id: 13579,
  name: 'Concrete',
  eventStatus: EventStatus['UNPUBLISHED'],
};

export const sampleWithFullData: IEvent = {
  id: 72471,
  name: 'transform one-to-one',
  eventStatus: EventStatus['CANCELLED'],
};

export const sampleWithNewData: NewEvent = {
  name: 'Rubber SAS Officer',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
