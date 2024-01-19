import { EventRegistrationStatus } from 'app/entities/enumerations/event-registration-status.model';

import { IEventRegistration, NewEventRegistration } from './event-registration.model';

export const sampleWithRequiredData: IEventRegistration = {
  id: 12905,
  name: 'seamless Louisiana',
};

export const sampleWithPartialData: IEventRegistration = {
  id: 68357,
  name: 'Jewelery Awesome',
  eventRegistrationStatus: EventRegistrationStatus['CANCELLED'],
};

export const sampleWithFullData: IEventRegistration = {
  id: 21304,
  name: 'Plastic SMTP',
  eventRegistrationStatus: EventRegistrationStatus['CANCELLED'],
};

export const sampleWithNewData: NewEventRegistration = {
  name: 'HTTP',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
