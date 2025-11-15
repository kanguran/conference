import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEventContext } from '../event-context.model';
import { EventContextService } from '../service/event-context.service';

const eventContextResolve = (route: ActivatedRouteSnapshot): Observable<null | IEventContext> => {
  const id = route.params.id;
  if (id) {
    return inject(EventContextService)
      .find(id)
      .pipe(
        mergeMap((eventContext: HttpResponse<IEventContext>) => {
          if (eventContext.body) {
            return of(eventContext.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default eventContextResolve;
