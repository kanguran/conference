import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EventContextComponent } from '../list/event-context.component';
import { EventContextDetailComponent } from '../detail/event-context-detail.component';
import { EventContextUpdateComponent } from '../update/event-context-update.component';
import { EventContextRoutingResolveService } from './event-context-routing-resolve.service';

const eventContextRoute: Routes = [
  {
    path: '',
    component: EventContextComponent,
    data: {
      defaultSort: `id,${  ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EventContextDetailComponent,
    resolve: {
      eventContext: EventContextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EventContextUpdateComponent,
    resolve: {
      eventContext: EventContextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EventContextUpdateComponent,
    resolve: {
      eventContext: EventContextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eventContextRoute)],
  exports: [RouterModule],
})
export class EventContextRoutingModule {}
