import { EventRegistrationStatus } from 'app/entities/enumerations/event-registration-status.model';

import { IEventRegistration, NewEventRegistration } from './event-registration.model';

export const sampleWithRequiredData: IEventRegistration = {
  id: 12905,
  eventRegistrationStatus: EventRegistrationStatus.ACTIVE,
};

export const sampleWithPartialData: IEventRegistration = {
  id: 61316,
  eventRegistrationStatus: EventRegistrationStatus.ACTIVE,
};

export const sampleWithFullData: IEventRegistration = {
  id: 58960,
  description: 'out-of-the-box implement',
  eventRegistrationStatus: EventRegistrationStatus.CANCELLED,
};

export const sampleWithNewData: NewEventRegistration = {
  eventRegistrationStatus: EventRegistrationStatus.ACTIVE,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
