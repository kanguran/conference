import dayjs from 'dayjs/esm';

import { EventContextStatus } from 'app/entities/enumerations/event-context-status.model';

import { IEventContext, NewEventContext } from './event-context.model';

export const sampleWithRequiredData: IEventContext = {
  id: 75593,
  name: 'Computer distributed',
};

export const sampleWithPartialData: IEventContext = {
  id: 35434,
  name: 'orange payment generation',
  eventContextStatus: EventContextStatus['CANCELLED'],
  start: dayjs('2024-01-19T06:01'),
  end: dayjs('2024-01-19T07:15'),
};

export const sampleWithFullData: IEventContext = {
  id: 50801,
  name: 'Virginia',
  eventContextStatus: EventContextStatus['AVAILABLE'],
  start: dayjs('2024-01-18T23:42'),
  end: dayjs('2024-01-19T13:25'),
};

export const sampleWithNewData: NewEventContext = {
  name: 'Ergonomic extend',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
