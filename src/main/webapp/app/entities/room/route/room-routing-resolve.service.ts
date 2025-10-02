import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoom } from '../room.model';
import { RoomService } from '../service/room.service';

@Injectable({ providedIn: 'root' })
export class RoomRoutingResolveService implements Resolve<IRoom | null> {
  constructor(
    protected service: RoomService,
    protected router: Router,
  ) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRoom | null | never> {
    const id = route.params.id;
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((room: HttpResponse<IRoom>) => {
          if (room.body) {
            return of(room.body);
          } 
            this.router.navigate(['404']);
            return EMPTY;
          
        }),
      );
    }
    return of(null);
  }
}
