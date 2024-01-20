import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { IEventContext } from 'app/entities/event-context/event-context.model';
import { EventRegistrationStatus } from 'app/entities/enumerations/event-registration-status.model';

export interface IEventRegistration {
  id: number;
  name?: string | null;
  eventRegistrationStatus?: EventRegistrationStatus | null;
  eventCounterparty?: Pick<IApplicationUser, 'id'> | null;
  eventContext?: Pick<IEventContext, 'id'> | null;
}

export type NewEventRegistration = Omit<IEventRegistration, 'id'> & { id: null };
