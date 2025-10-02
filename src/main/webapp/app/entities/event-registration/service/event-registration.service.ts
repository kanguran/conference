import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEventRegistration, NewEventRegistration } from '../event-registration.model';

export type PartialUpdateEventRegistration = Partial<IEventRegistration> & Pick<IEventRegistration, 'id'>;

export type EntityResponseType = HttpResponse<IEventRegistration>;
export type EntityArrayResponseType = HttpResponse<IEventRegistration[]>;

@Injectable({ providedIn: 'root' })
export class EventRegistrationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/event-registrations');

  create(eventRegistration: NewEventRegistration): Observable<EntityResponseType> {
    return this.http.post<IEventRegistration>(this.resourceUrl, eventRegistration, { observe: 'response' });
  }

  update(eventRegistration: IEventRegistration): Observable<EntityResponseType> {
    return this.http.put<IEventRegistration>(
      `${this.resourceUrl}/${this.getEventRegistrationIdentifier(eventRegistration)}`,
      eventRegistration,
      { observe: 'response' },
    );
  }

  partialUpdate(eventRegistration: PartialUpdateEventRegistration): Observable<EntityResponseType> {
    return this.http.patch<IEventRegistration>(
      `${this.resourceUrl}/${this.getEventRegistrationIdentifier(eventRegistration)}`,
      eventRegistration,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEventRegistration>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEventRegistration[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEventRegistrationIdentifier(eventRegistration: Pick<IEventRegistration, 'id'>): number {
    return eventRegistration.id;
  }

  compareEventRegistration(o1: Pick<IEventRegistration, 'id'> | null, o2: Pick<IEventRegistration, 'id'> | null): boolean {
    return o1 && o2 ? this.getEventRegistrationIdentifier(o1) === this.getEventRegistrationIdentifier(o2) : o1 === o2;
  }

  addEventRegistrationToCollectionIfMissing<Type extends Pick<IEventRegistration, 'id'>>(
    eventRegistrationCollection: Type[],
    ...eventRegistrationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const eventRegistrations: Type[] = eventRegistrationsToCheck.filter(isPresent);
    if (eventRegistrations.length > 0) {
      const eventRegistrationCollectionIdentifiers = eventRegistrationCollection.map(eventRegistrationItem =>
        this.getEventRegistrationIdentifier(eventRegistrationItem),
      );
      const eventRegistrationsToAdd = eventRegistrations.filter(eventRegistrationItem => {
        const eventRegistrationIdentifier = this.getEventRegistrationIdentifier(eventRegistrationItem);
        if (eventRegistrationCollectionIdentifiers.includes(eventRegistrationIdentifier)) {
          return false;
        }
        eventRegistrationCollectionIdentifiers.push(eventRegistrationIdentifier);
        return true;
      });
      return [...eventRegistrationsToAdd, ...eventRegistrationCollection];
    }
    return eventRegistrationCollection;
  }
}
