import dayjs from 'dayjs/esm';

import { IEventContext, NewEventContext } from './event-context.model';

export const sampleWithRequiredData: IEventContext = {
  id: 28713,
  description: 'er besides which',
  eventContextStatus: 'CANCELLED',
  start: dayjs('2024-01-18T21:49'),
  end: dayjs('2024-01-19T11:36'),
};

export const sampleWithPartialData: IEventContext = {
  id: 1880,
  description: 'order bug on',
  eventContextStatus: 'AVAILABLE',
  start: dayjs('2024-01-19T03:48'),
  end: dayjs('2024-01-18T21:29'),
};

export const sampleWithFullData: IEventContext = {
  id: 18500,
  description: 'glorious',
  eventContextStatus: 'AVAILABLE',
  start: dayjs('2024-01-19T12:01'),
  end: dayjs('2024-01-19T13:03'),
};

export const sampleWithNewData: NewEventContext = {
  description: 'adjudge although',
  eventContextStatus: 'CANCELLED',
  start: dayjs('2024-01-19T21:12'),
  end: dayjs('2024-01-19T10:27'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
