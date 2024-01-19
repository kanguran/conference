import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EventRegistrationComponent } from '../list/event-registration.component';
import { EventRegistrationDetailComponent } from '../detail/event-registration-detail.component';
import { EventRegistrationUpdateComponent } from '../update/event-registration-update.component';
import { EventRegistrationRoutingResolveService } from './event-registration-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const eventRegistrationRoute: Routes = [
  {
    path: '',
    component: EventRegistrationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EventRegistrationDetailComponent,
    resolve: {
      eventRegistration: EventRegistrationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EventRegistrationUpdateComponent,
    resolve: {
      eventRegistration: EventRegistrationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EventRegistrationUpdateComponent,
    resolve: {
      eventRegistration: EventRegistrationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eventRegistrationRoute)],
  exports: [RouterModule],
})
export class EventRegistrationRoutingModule {}
