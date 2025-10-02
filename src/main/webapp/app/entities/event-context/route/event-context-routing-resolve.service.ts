import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEventContext } from '../event-context.model';
import { EventContextService } from '../service/event-context.service';

@Injectable({ providedIn: 'root' })
export class EventContextRoutingResolveService implements Resolve<IEventContext | null> {
  constructor(
    protected service: EventContextService,
    protected router: Router,
  ) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEventContext | null | never> {
    const id = route.params.id;
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((eventContext: HttpResponse<IEventContext>) => {
          if (eventContext.body) {
            return of(eventContext.body);
          } 
            this.router.navigate(['404']);
            return EMPTY;
          
        }),
      );
    }
    return of(null);
  }
}
