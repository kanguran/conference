import { IEventRegistration, NewEventRegistration } from './event-registration.model';

export const sampleWithRequiredData: IEventRegistration = {
  id: 20744,
  eventRegistrationStatus: 'CANCELLED',
};

export const sampleWithPartialData: IEventRegistration = {
  id: 22259,
  description: 'woot planula',
  eventRegistrationStatus: 'CANCELLED',
};

export const sampleWithFullData: IEventRegistration = {
  id: 16954,
  description: 'disinherit',
  eventRegistrationStatus: 'ACTIVE',
};

export const sampleWithNewData: NewEventRegistration = {
  eventRegistrationStatus: 'CANCELLED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
