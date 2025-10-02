import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEventContext, NewEventContext } from '../event-context.model';

export type PartialUpdateEventContext = Partial<IEventContext> & Pick<IEventContext, 'id'>;

type RestOf<T extends IEventContext | NewEventContext> = Omit<T, 'start' | 'end'> & {
  start?: string | null;
  end?: string | null;
};

export type RestEventContext = RestOf<IEventContext>;

export type NewRestEventContext = RestOf<NewEventContext>;

export type PartialUpdateRestEventContext = RestOf<PartialUpdateEventContext>;

export type EntityResponseType = HttpResponse<IEventContext>;
export type EntityArrayResponseType = HttpResponse<IEventContext[]>;

@Injectable({ providedIn: 'root' })
export class EventContextService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/event-contexts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(eventContext: NewEventContext): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventContext);
    return this.http
      .post<RestEventContext>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(eventContext: IEventContext): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventContext);
    return this.http
      .put<RestEventContext>(`${this.resourceUrl}/${this.getEventContextIdentifier(eventContext)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(eventContext: PartialUpdateEventContext): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventContext);
    return this.http
      .patch<RestEventContext>(`${this.resourceUrl}/${this.getEventContextIdentifier(eventContext)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEventContext>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEventContext[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEventContextIdentifier(eventContext: Pick<IEventContext, 'id'>): number {
    return eventContext.id;
  }

  compareEventContext(o1: Pick<IEventContext, 'id'> | null, o2: Pick<IEventContext, 'id'> | null): boolean {
    return o1 && o2 ? this.getEventContextIdentifier(o1) === this.getEventContextIdentifier(o2) : o1 === o2;
  }

  addEventContextToCollectionIfMissing<Type extends Pick<IEventContext, 'id'>>(
    eventContextCollection: Type[],
    ...eventContextsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const eventContexts: Type[] = eventContextsToCheck.filter(isPresent);
    if (eventContexts.length > 0) {
      const eventContextCollectionIdentifiers = eventContextCollection.map(
        eventContextItem => this.getEventContextIdentifier(eventContextItem)!
      );
      const eventContextsToAdd = eventContexts.filter(eventContextItem => {
        const eventContextIdentifier = this.getEventContextIdentifier(eventContextItem);
        if (eventContextCollectionIdentifiers.includes(eventContextIdentifier)) {
          return false;
        }
        eventContextCollectionIdentifiers.push(eventContextIdentifier);
        return true;
      });
      return [...eventContextsToAdd, ...eventContextCollection];
    }
    return eventContextCollection;
  }

  protected convertDateFromClient<T extends IEventContext | NewEventContext | PartialUpdateEventContext>(eventContext: T): RestOf<T> {
    return {
      ...eventContext,
      start: eventContext.start?.toJSON() ?? null,
      end: eventContext.end?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEventContext: RestEventContext): IEventContext {
    return {
      ...restEventContext,
      start: restEventContext.start ? dayjs(restEventContext.start) : undefined,
      end: restEventContext.end ? dayjs(restEventContext.end) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEventContext>): HttpResponse<IEventContext> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEventContext[]>): HttpResponse<IEventContext[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
