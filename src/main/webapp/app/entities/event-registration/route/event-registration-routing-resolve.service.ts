import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEventRegistration } from '../event-registration.model';
import { EventRegistrationService } from '../service/event-registration.service';

const eventRegistrationResolve = (route: ActivatedRouteSnapshot): Observable<null | IEventRegistration> => {
  const id = route.params.id;
  if (id) {
    return inject(EventRegistrationService)
      .find(id)
      .pipe(
        mergeMap((eventRegistration: HttpResponse<IEventRegistration>) => {
          if (eventRegistration.body) {
            return of(eventRegistration.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default eventRegistrationResolve;
