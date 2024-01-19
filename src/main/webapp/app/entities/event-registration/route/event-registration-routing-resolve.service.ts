import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEventRegistration } from '../event-registration.model';
import { EventRegistrationService } from '../service/event-registration.service';

@Injectable({ providedIn: 'root' })
export class EventRegistrationRoutingResolveService implements Resolve<IEventRegistration | null> {
  constructor(protected service: EventRegistrationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEventRegistration | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((eventRegistration: HttpResponse<IEventRegistration>) => {
          if (eventRegistration.body) {
            return of(eventRegistration.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
