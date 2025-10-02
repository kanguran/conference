import { EventType } from 'app/entities/enumerations/event-type.model';
import { EventStatus } from 'app/entities/enumerations/event-status.model';

import { IEvent, NewEvent } from './event.model';

export const sampleWithRequiredData: IEvent = {
  id: 63022,
  name: 'Cotton',
  eventType: EventType['CONFERENCE'],
  eventStatus: EventStatus['UNPUBLISHED'],
};

export const sampleWithPartialData: IEvent = {
  id: 2671,
  name: 'red',
  eventType: EventType['CONFERENCE'],
  eventStatus: EventStatus['PUBLISHED'],
};

export const sampleWithFullData: IEvent = {
  id: 38618,
  name: 'withdrawal',
  eventType: EventType['CONFERENCE'],
  eventStatus: EventStatus['CANCELLED'],
};

export const sampleWithNewData: NewEvent = {
  name: 'Rubber SAS Officer',
  eventType: EventType['CONFERENCE'],
  eventStatus: EventStatus['UNPUBLISHED'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
