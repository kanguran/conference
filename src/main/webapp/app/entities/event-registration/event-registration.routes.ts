import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EventRegistrationResolve from './route/event-registration-routing-resolve.service';

const eventRegistrationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/event-registration.component').then(m => m.EventRegistrationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/event-registration-detail.component').then(m => m.EventRegistrationDetailComponent),
    resolve: {
      eventRegistration: EventRegistrationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/event-registration-update.component').then(m => m.EventRegistrationUpdateComponent),
    resolve: {
      eventRegistration: EventRegistrationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/event-registration-update.component').then(m => m.EventRegistrationUpdateComponent),
    resolve: {
      eventRegistration: EventRegistrationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default eventRegistrationRoute;
