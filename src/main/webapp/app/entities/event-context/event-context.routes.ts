import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EventContextResolve from './route/event-context-routing-resolve.service';

const eventContextRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/event-context.component').then(m => m.EventContextComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/event-context-detail.component').then(m => m.EventContextDetailComponent),
    resolve: {
      eventContext: EventContextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/event-context-update.component').then(m => m.EventContextUpdateComponent),
    resolve: {
      eventContext: EventContextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/event-context-update.component').then(m => m.EventContextUpdateComponent),
    resolve: {
      eventContext: EventContextResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default eventContextRoute;
